package com.jando.fitness_app;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.LOCATION_SERVICE;

public class HomeScreen extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    LocationListener locationListener;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

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
        setTitle(getString(R.string.fragment_home_label));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = database.getReference("Location");


        //location changes and adds latitude and longitude to database
        //************************************************************************************************************************************
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(HomeScreen.this, "Location changed", Toast.LENGTH_SHORT).show();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String childValue = "555";
                mRef.child(user.getUid()).child("Latitude").setValue(latitude);
                mRef.child(user.getUid()).child("Longitude").setValue(longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, locationListener);
//**************************************************************************************************************************************************


    }

    protected void onPause() {
        super.onPause();
        Toast.makeText(HomeScreen.this, "Paused", Toast.LENGTH_SHORT).show();

        locationManager.removeUpdates(locationListener);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 3, locationListener);

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
                            setTitle(getString(R.string.fragment_home_label));
                            break;
                        case R.id.bot_nav_account:
                            selectedFragment = new AccountFragment();
                            setTitle(getString(R.string.fragment_account_label));
                            break;
                        case R.id.bot_nav_health:
                            selectedFragment = new HealthFragment();
                            setTitle(getString(R.string.fragment_health_label));
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
