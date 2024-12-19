package ua.cn.stu.api;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ua.cn.stu.api.model.db.AppDatabase;
import ua.cn.stu.api.model.db.CountryEntity;

import java.util.List;

public class CountryViewModel extends AndroidViewModel {

    private AppDatabase db;

    public CountryViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application); // Make sure to initialize the database properly
    }

    public LiveData<List<CountryEntity>> getAllCountries() {
        return db.countryDao().getAllCountriesLive();
    }
}
