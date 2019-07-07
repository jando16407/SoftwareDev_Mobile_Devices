package com.jando.fitness_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class WeatherActivity extends AppCompatActivity {

    String j1 = null;
    String j2 = null;
    String j3 = null;
    String key;
    TextView currentWeatherTitle;
    TextView currentWeatherDisplay;
    TextView forecastTitle;
    TextView forecastDisplay;
    String longitude;
    String latitude;
    private FirebaseAuth firebaseAuth;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        currentWeatherTitle = findViewById(R.id.currentWeatherTitle);
        currentWeatherDisplay = findViewById(R.id.currentWeatherDisplay);
        forecastTitle = findViewById(R.id.forecastTitle);
        forecastDisplay = findViewById(R.id.forecastDisplay);
        latitude = "28.07061679";
        longitude = "-82.41369035";
        context = WeatherActivity.this;

        /** Get the longitude and latitude */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = database.getReference("Location");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                latitude= (dataSnapshot.child(user.getUid()).child("Latitude").getValue()).toString();
                longitude= (dataSnapshot.child(user.getUid()).child("Longitude").getValue()).toString();
                weatherCheck();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        //Adds back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void weatherCheck (){
        WeatherTask task = new WeatherTask();

        try {
            // API key 1
            Toast.makeText(WeatherActivity.this, "Latitude: "+latitude+"\nLongitude: "+longitude, Toast.LENGTH_SHORT).show();
            j1 = task.execute("https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=ybpRsMsZ0gudQicb50c9Pgv793X2HeLH&q="+latitude+"%2C"+longitude+"&language=en-us&details=false&toplevel=false").get();

        } catch(InterruptedException e) {
            Toast.makeText(WeatherActivity.this, "Error trying loc interruption", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(WeatherActivity.this, "Error trying loc exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            }

    }

    public class WeatherTask extends AsyncTask<String, Void, String> {

        /** Get the location "Key" from accuweather Location API */
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpsURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while( data != -1 ){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;
            }
            catch (Exception e){
                e.printStackTrace();
                return "Failed :"+e;
            }
        }

        @Override
        protected void onPostExecute(String j1) {
            super.onPostExecute(j1);

            try {
                /** Extract the actual location key */
                JSONObject object = new JSONObject(j1);
                // get the location key
                for (int i = 0; i<object.length(); i++) {
                    key = object.getString("Key");
                }

                /** Current weather */

                try {
                    WeatherTask currentWeather = new WeatherTask();
                    // API key 1
                    j2 = currentWeather.execute("https://dataservice.accuweather.com/currentconditions/v1/"+key+"?apikey=ybpRsMsZ0gudQicb50c9Pgv793X2HeLH&language=en-us&details=false").get();// API key 2

                writeCurrentWeather(j2);
                    /** Build the output */
                    JSONArray weatherArray = new JSONArray(j2);
                    SpannableStringBuilder output = new SpannableStringBuilder();
                    for (int i = 0; i<weatherArray.length(); i++) {

                        JSONObject weatherPart = weatherArray.getJSONObject(i);
                        String icon_name = "weather"+weatherPart.getString("WeatherIcon");
                        int icon_id = getResources().getIdentifier(icon_name, "drawable", "com.jando.fitness_app");
                        output.append("\t\t\t\t\t\t\t\t\t\t\t\t\t" + weatherPart.getString("WeatherText") + "\n");
                        output.append("\t\t\t\t\t\t\t\t\t\t\t\t\t" + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Imperial").getString("Value") + " " + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Imperial").getString("Unit") + " / " + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Metric").getString("Value") + " " + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Metric").getString("Unit")+"\t\t\t\t\t\t");
                        output.append(" ");
                        output.setSpan(new ImageSpan(WeatherActivity.this, icon_id), output.length()-1, output.length(), 0);
                        output.append("\n");
                    }

                    /** Output to current weather display */
                    currentWeatherDisplay.setText(output);

                    /** Close the try block */
                 } catch (InterruptedException e) {
                 e.printStackTrace();
                 } catch (ExecutionException e) {
                 e.printStackTrace();
                 }

                /** 12 Hour Forecast weather */
                try {
                    WeatherTask forecast = new WeatherTask();
                    // API key 1
                    j3 = forecast.execute("https://dataservice.accuweather.com/forecasts/v1/hourly/12hour/"+key+"?apikey=7WX6jWHRE1KlP7ueFS8BePxjeq9pFMQG&language=en-us&details=false&metric=false").get();

                    writeForecast(j3);
                    /** Format the output */
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String epochString = "1562443200";
                    long epoch = Long.parseLong( epochString );
                    /** Build the output */
                    JSONArray forecastArray = new JSONArray(j3);
                    SpannableStringBuilder outputForecast = new SpannableStringBuilder();

                    for (int i = 0; i<forecastArray.length(); i++){
                        Date date = new Date(Long.parseLong(forecastArray.getJSONObject(i).getString("EpochDateTime"))*1000);
                        outputForecast.append(sdf.format(date)+"\n\n");
                        outputForecast.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+forecastArray.getJSONObject(i).getString("IconPhrase")+"\n");
                        outputForecast.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + forecastArray.getJSONObject(i).getJSONObject("Temperature").getString("Value") + " "
                                        + forecastArray.getJSONObject(i).getJSONObject("Temperature").getString("Unit")+"\t\t\t\t\t\t\t\t\t\t\t\t");
                        outputForecast.append(" ");
                        String icon_name = "weather"+forecastArray.getJSONObject(i).getString("WeatherIcon");
                        int icon_id = getResources().getIdentifier(icon_name, "drawable", "com.jando.fitness_app");
                        outputForecast.setSpan(new ImageSpan(WeatherActivity.this, icon_id), outputForecast.length()-1, outputForecast.length(), 0);
                        outputForecast.append("\n\n");

                    }


                /** Output to current weather display */
                    forecastDisplay.setText(outputForecast);

                 } catch (InterruptedException e) {
                 e.printStackTrace();
                 } catch (ExecutionException e) {
                 e.printStackTrace();
                 }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void writeCurrentWeather(String j2) {
        try {

            FileOutputStream fileOutputStream =
                    context.openFileOutput("CurrentWeather.txt", MODE_PRIVATE);
            fileOutputStream.write(j2.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void writeForecast(String j3) {
        try {

            FileOutputStream fileOutputStream =
                    context.openFileOutput("Forecast.txt", MODE_PRIVATE);
            fileOutputStream.write(j3.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private String readCurrentWeather() {
        try {

            FileInputStream fileInputStream = context.openFileInput("CurrentWeather.txt");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();

            String number;
            while ((number = bufferedReader.readLine()) != null){
                stringBuffer.append(number);
            }

            return stringBuffer.toString();

        } catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

    private String readForecast() {
        try {

            FileInputStream fileInputStream = context.openFileInput("Forecast.txt");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();

            String number;
            while ((number = bufferedReader.readLine()) != null){
                stringBuffer.append(number);
            }

            return stringBuffer.toString();

        } catch (FileNotFoundException e){
            e.printStackTrace();
            return null;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
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
