package ua.cn.stu.api;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import ua.cn.stu.api.model.API.ApiService;
import ua.cn.stu.api.model.API.CountriesResponse;
import ua.cn.stu.api.model.API.Country;
import ua.cn.stu.api.model.db.CountryEntity;
import ua.cn.stu.api.model.db.AppDatabase;

import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CountryViewModel countryViewModel;
    private List<CountryEntity> countries = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ініціалізуємо ViewModel
        countryViewModel = new ViewModelProvider(this).get(CountryViewModel.class);

        // Спостерігаємо за змінами в базі даних
        countryViewModel.getAllCountries().observe(this, new Observer<List<CountryEntity>>() {
            @Override
            public void onChanged(List<CountryEntity> countryEntities) {
                if (countryEntities != null && !countryEntities.isEmpty()) {
                    countries = countryEntities;
                    updateRecyclerView();
                } else {
                    // Якщо в базі немає даних, намагаємось завантажити їх з мережі
                    fetchCountriesFromNetwork();
                }
            }
        });

        // Створюємо адаптер, щоб відобразити дані
        adapter = new MyAdapter(countries, item -> {
            // Передаємо дані в DetailActivity
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("country", item.getName());
            intent.putExtra("capital", item.getCapitalCity());
            intent.putExtra("iso2Code", item.getIso2Code());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Завантажуємо дані з мережі, якщо в локальній базі немає
        if (isNetworkAvailable()) {
            fetchCountriesFromNetwork();
        } else {
            // Якщо немає мережі, показуємо повідомлення та використовуємо локальні дані
            Toast.makeText(this, "No internet connection, loading from local database.", Toast.LENGTH_SHORT).show();
        }
    }

    // Перевірка наявності інтернет-з'єднання
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void fetchCountriesFromNetwork() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.worldbank.org/")
                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        apiService.getCountries().enqueue(new Callback<CountriesResponse>() {
            @Override
            public void onResponse(Call<CountriesResponse> call, Response<CountriesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CountriesResponse countriesResponse = response.body();
                    List<Country> fetchedCountries = countriesResponse.getCountries();
                    // Конвертуємо отримані дані в об'єкти CountryEntity для Room
                    List<CountryEntity> countryEntities = new ArrayList<>();
                    for (Country country : fetchedCountries) {
                        countryEntities.add(new CountryEntity(country.getIso2Code(), country.getName(), country.getCapitalCity()));
                    }

                    // Вставляємо дані в локальну базу
                    new Thread(() -> AppDatabase.getInstance(MainActivity.this).countryDao().insertAll(countryEntities)).start();
                    countries = countryEntities;
                    updateRecyclerView();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to load data from network", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CountriesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView() {
        adapter.updateData(countries);
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<CountryEntity> data;
        private OnItemClickListener listener;

        interface OnItemClickListener {
            void onItemClick(CountryEntity item);
        }

        MyAdapter(List<CountryEntity> data, OnItemClickListener listener) {
            this.data = data;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_view, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CountryEntity item = data.get(position);
            holder.textView.setText(item.getName());
            holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void updateData(List<CountryEntity> newData) {
            this.data = newData;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
            }
        }
    }
}
