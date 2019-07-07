package com.jando.fitness_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResourcesActivity extends AppCompatActivity {

    private TextView TextViewGoogleMaps;
    private TextView TextViewWeather;
    private TextView TextViewFirebase;

    /** What happens when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        //Adds back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextViewGoogleMaps = findViewById(R.id.textViewGoogleMaps);
        TextViewWeather = findViewById(R.id.textViewWeather);
        TextViewFirebase = findViewById(R.id.textViewFirebase);




    }

    /**Back button functionality */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}
