package com.jando.fitness_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class HomeScreen extends AppCompatActivity {

    /** What happens when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        //Declarations regarding bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        //Display home fragment on app start
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        setTitle("Home");
    }

    /** Creates functionality of buttons on bottom navigation bar */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment;
                    switch(menuItem.getItemId()){
                        case R.id.bot_nav_home:
                            selectedFragment = new HomeFragment();
                            setTitle("Home");
                            break;
                        case R.id.bot_nav_account:
                            selectedFragment = new AccountFragment();
                            setTitle("Account");
                            break;
                        case R.id.bot_nav_health:
                            selectedFragment = new HealthFragment();
                            setTitle("Health");
                            break;
                        default:
                            return false;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    /**Top navigation bar functionality */
    @Override
    // Inflate the menu items for use in the action bar
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.top_settings:
                openSettings();
                return true;
            case R.id.top_resources:
                openResources();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openResources() {
        Intent intent = new Intent(this, ResourcesActivity.class);
        startActivity(intent);
    }
}
