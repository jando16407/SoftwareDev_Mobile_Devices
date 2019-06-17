package com.jando.fitness_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

public class AccountSettingsActivity extends AppCompatActivity {

    /** What happens when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

    //Adds back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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