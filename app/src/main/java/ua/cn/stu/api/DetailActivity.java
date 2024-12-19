package ua.cn.stu.api;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView detailTextView = findViewById(R.id.detailTextView);

        // Retrieve the data passed from MainActivity
        String country = getIntent().getStringExtra("country");
        String capital = getIntent().getStringExtra("capital");
        String iso2Code = getIntent().getStringExtra("iso2Code");  // Retrieve iso2Code

        // Display country details
        String details = "Country: " + country + "\nCapital: " + capital + "\nISO Code: " + iso2Code;
        detailTextView.setText(details);
    }
}
