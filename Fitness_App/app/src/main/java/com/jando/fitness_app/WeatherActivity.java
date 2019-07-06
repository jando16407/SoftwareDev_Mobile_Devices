package com.jando.fitness_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
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
    EditText locationName;
    String key;
    TextView weatherReport;
    TextView currentWeatherTitle;
    TextView currentWeatherDisplay;
    TextView forecastTitle;
    TextView forecastDisplay;
    String longitude;
    String latitude;
    private FirebaseAuth firebaseAuth;

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

        /** Get the longitude and latitude */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = database.getReference("Location");
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                latitude= (dataSnapshot.child(user.getUid()).child("Latitude").getValue()).toString();//.toString();
                longitude= (dataSnapshot.child(user.getUid()).child("Longitude").getValue()).toString();//.toString();
                Toast.makeText(WeatherActivity.this, "Latitude: "+latitude+"\nLongitude: "+longitude, Toast.LENGTH_SHORT).show();
                weatherCheck();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        //locationName = findViewById(R.id.locationName);
        //weatherReport = findViewById(R.id.forecastDisplay);




        //Adds back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void weatherCheck (){
        WeatherTask task = new WeatherTask();

        /** Uncomment to use when API key is available
        try {
            // API key 1
            //j1 = task.execute("https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=ybpRsMsZ0gudQicb50c9Pgv793X2HeLH&q="+latitude+","+longitude+"&language=en-us&details=false&toplevel=false").get();
            } catch(InterruptedException e) {
                Toast.makeText(WeatherActivity.this, "Error trying loc interruption", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (ExecutionException e) {
                Toast.makeText(WeatherActivity.this, "Error trying loc exception", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        */
            // API key 2 */
            //j1 = task.execute("https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=7WX6jWHRE1KlP7ueFS8BePxjeq9pFMQG="+locationName.getText().toString()+"&language=en-us&details=false&toplevel=false").get();
            // API key 3 */
           // j1 = task.execute("https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=ai7pJYg85quTbfX7pJZd7KTLyodA3oYG="+locationName.getText().toString()+"&language=en-us&details=false&toplevel=false").get();
            /** Return value from URL tried */
            j1 = "{\n" +
                    "  \"Version\": 1,\n" +
                    "  \"Key\": \"2621246\",\n" +
                    "  \"Type\": \"City\",\n" +
                    "  \"Rank\": 65,\n" +
                    "  \"LocalizedName\": \"East Lake-Orient Park\",\n" +
                    "  \"EnglishName\": \"East Lake-Orient Park\",\n" +
                    "  \"PrimaryPostalCode\": \"33610\",\n" +
                    "  \"Region\": {\n" +
                    "    \"ID\": \"NAM\",\n" +
                    "    \"LocalizedName\": \"North America\",\n" +
                    "    \"EnglishName\": \"North America\"\n" +
                    "  },\n" +
                    "  \"Country\": {\n" +
                    "    \"ID\": \"US\",\n" +
                    "    \"LocalizedName\": \"United States\",\n" +
                    "    \"EnglishName\": \"United States\"\n" +
                    "  },\n" +
                    "  \"AdministrativeArea\": {\n" +
                    "    \"ID\": \"FL\",\n" +
                    "    \"LocalizedName\": \"Florida\",\n" +
                    "    \"EnglishName\": \"Florida\",\n" +
                    "    \"Level\": 1,\n" +
                    "    \"LocalizedType\": \"State\",\n" +
                    "    \"EnglishType\": \"State\",\n" +
                    "    \"CountryID\": \"US\"\n" +
                    "  },\n" +
                    "  \"TimeZone\": {\n" +
                    "    \"Code\": \"EDT\",\n" +
                    "    \"Name\": \"America/New_York\",\n" +
                    "    \"GmtOffset\": -4,\n" +
                    "    \"IsDaylightSaving\": true,\n" +
                    "    \"NextOffsetChange\": \"2019-11-03T06:00:00Z\"\n" +
                    "  },\n" +
                    "  \"GeoPosition\": {\n" +
                    "    \"Latitude\": 27.997,\n" +
                    "    \"Longitude\": -82.365,\n" +
                    "    \"Elevation\": {\n" +
                    "      \"Metric\": {\n" +
                    "        \"Value\": 10,\n" +
                    "        \"Unit\": \"m\",\n" +
                    "        \"UnitType\": 5\n" +
                    "      },\n" +
                    "      \"Imperial\": {\n" +
                    "        \"Value\": 32,\n" +
                    "        \"Unit\": \"ft\",\n" +
                    "        \"UnitType\": 0\n" +
                    "      }\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"IsAlias\": false,\n" +
                    "  \"SupplementalAdminAreas\": [\n" +
                    "    {\n" +
                    "      \"Level\": 2,\n" +
                    "      \"LocalizedName\": \"Hillsborough\",\n" +
                    "      \"EnglishName\": \"Hillsborough\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"DataSets\": [\n" +
                    "    \"Alerts\",\n" +
                    "    \"DailyAirQualityForecast\",\n" +
                    "    \"DailyPollenForecast\",\n" +
                    "    \"ForecastConfidence\",\n" +
                    "    \"MinuteCast\",\n" +
                    "    \"Radar\"\n" +
                    "  ]\n" +
                    "}";
            task.onPostExecute(j1);


            //weatherReport.setText(j1);
   /**     } catch(InterruptedException e) {
            Toast.makeText(WeatherActivity.this, "Error trying loc interruption", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(WeatherActivity.this, "Error trying loc exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
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
//                weatherReport.setText(key);

                /** Current weather */
                /** Uncomment to use when API key is available
                try {
                    WeatherTask currentWeather = new WeatherTask();
                    // API key 1
                    j2 = currentWeather.execute("https://dataservice.accuweather.com/currentconditions/v1/locationKey="+key+"?apikey=ybpRsMsZ0gudQicb50c9Pgv793X2HeLH&language=en-us&details=false").get();
                */
                j2 = "[\n" +
                        "  {\n" +
                        "    \"LocalObservationDateTime\": \"2019-07-06T14:22:00-04:00\",\n" +
                        "    \"EpochTime\": 1562437320,\n" +
                        "    \"WeatherText\": \"Mostly cloudy\",\n" +
                        "    \"WeatherIcon\": 6,\n" +
                        "    \"HasPrecipitation\": false,\n" +
                        "    \"PrecipitationType\": null,\n" +
                        "    \"IsDayTime\": true,\n" +
                        "    \"Temperature\": {\n" +
                        "      \"Metric\": {\n" +
                        "        \"Value\": 31.1,\n" +
                        "        \"Unit\": \"C\",\n" +
                        "        \"UnitType\": 17\n" +
                        "      },\n" +
                        "      \"Imperial\": {\n" +
                        "        \"Value\": 88,\n" +
                        "        \"Unit\": \"F\",\n" +
                        "        \"UnitType\": 18\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/current-weather/2621246?lang=en-us\",\n" +
                        "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/current-weather/2621246?lang=en-us\"\n" +
                        "  }\n" +
                        "]";

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
                /** Uncomment to use when API key is available
                 } catch (InterruptedException e) {
                 e.printStackTrace();
                 } catch (ExecutionException e) {
                 e.printStackTrace();
                 }
          //       */

                /** 12 Hour Forecast weather */
                /** Uncomment to use when API key is available
                try {
                    WeatherTask forecast = new WeatherTask();
                    // API key 1
                    j3 = forecast.execute("http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/"+key+"?apikey=7WX6jWHRE1KlP7ueFS8BePxjeq9pFMQG&language=en-us&details=false&metric=false").get();
              //  */
                    j3 = "[\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T16:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562443200,\n" +
                            "    \"WeatherIcon\": 7,\n" +
                            "    \"IconPhrase\": \"Cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": true,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 90,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 48,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=16&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=16&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T17:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562446800,\n" +
                            "    \"WeatherIcon\": 7,\n" +
                            "    \"IconPhrase\": \"Cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": true,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 88,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 44,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=17&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=17&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T18:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562450400,\n" +
                            "    \"WeatherIcon\": 7,\n" +
                            "    \"IconPhrase\": \"Cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": true,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 86,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 47,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=18&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=18&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T19:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562454000,\n" +
                            "    \"WeatherIcon\": 15,\n" +
                            "    \"IconPhrase\": \"Thunderstorms\",\n" +
                            "    \"HasPrecipitation\": true,\n" +
                            "    \"PrecipitationType\": \"Rain\",\n" +
                            "    \"PrecipitationIntensity\": \"Moderate\",\n" +
                            "    \"IsDaylight\": true,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 84,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 51,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=19&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=19&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T20:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562457600,\n" +
                            "    \"WeatherIcon\": 3,\n" +
                            "    \"IconPhrase\": \"Partly sunny\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": true,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 81,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 40,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=20&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=20&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T21:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562461200,\n" +
                            "    \"WeatherIcon\": 35,\n" +
                            "    \"IconPhrase\": \"Partly cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": false,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 79,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 34,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=21&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=21&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T22:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562464800,\n" +
                            "    \"WeatherIcon\": 35,\n" +
                            "    \"IconPhrase\": \"Partly cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": false,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 78,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 40,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=22&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=22&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-06T23:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562468400,\n" +
                            "    \"WeatherIcon\": 41,\n" +
                            "    \"IconPhrase\": \"Partly cloudy w/ t-storms\",\n" +
                            "    \"HasPrecipitation\": true,\n" +
                            "    \"PrecipitationType\": \"Rain\",\n" +
                            "    \"PrecipitationIntensity\": \"Moderate\",\n" +
                            "    \"IsDaylight\": false,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 77,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 51,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=23&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=1&hbhhour=23&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-07T00:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562472000,\n" +
                            "    \"WeatherIcon\": 35,\n" +
                            "    \"IconPhrase\": \"Partly cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": false,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 76,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 47,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=0&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=0&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-07T01:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562475600,\n" +
                            "    \"WeatherIcon\": 35,\n" +
                            "    \"IconPhrase\": \"Partly cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": false,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 76,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 36,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=1&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=1&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-07T02:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562479200,\n" +
                            "    \"WeatherIcon\": 35,\n" +
                            "    \"IconPhrase\": \"Partly cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": false,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 75,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 20,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=2&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=2&lang=en-us\"\n" +
                            "  },\n" +
                            "  {\n" +
                            "    \"DateTime\": \"2019-07-07T03:00:00-04:00\",\n" +
                            "    \"EpochDateTime\": 1562482800,\n" +
                            "    \"WeatherIcon\": 35,\n" +
                            "    \"IconPhrase\": \"Partly cloudy\",\n" +
                            "    \"HasPrecipitation\": false,\n" +
                            "    \"IsDaylight\": false,\n" +
                            "    \"Temperature\": {\n" +
                            "      \"Value\": 74,\n" +
                            "      \"Unit\": \"F\",\n" +
                            "      \"UnitType\": 18\n" +
                            "    },\n" +
                            "    \"PrecipitationProbability\": 20,\n" +
                            "    \"MobileLink\": \"http://m.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=3&lang=en-us\",\n" +
                            "    \"Link\": \"http://www.accuweather.com/en/us/east-lake-orient-park-fl/33610/hourly-weather-forecast/2621246?day=2&hbhhour=3&lang=en-us\"\n" +
                            "  }\n" +
                            "]";

                    /** Format the output */
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String epochString = "1562443200";
                    long epoch = Long.parseLong( epochString );
                    /** Build the output */
                    JSONArray forecastArray = new JSONArray(j3);
                   // String outputForecast = "";
                    SpannableStringBuilder outputForecast = new SpannableStringBuilder();

                    for (int i = 0; i<forecastArray.length(); i++){
                        //outputForecast =
                        Date date = new Date(Long.parseLong(forecastArray.getJSONObject(i).getString("EpochDateTime"))*1000);
//                        outputForecast += sdf.format(date)+"\n\n";
  //                      outputForecast += "\t\t\t\t\t\t\t\t\t\t\t\t"+forecastArray.getJSONObject(i).getString("IconPhrase")+"\n";
    //                    outputForecast += "\t\t\t\t\t\t\t\t\t\t\t\t" + forecastArray.getJSONObject(i).getJSONObject("Temperature").getString("Value") + " "
      //                          + forecastArray.getJSONObject(i).getJSONObject("Temperature").getString("Unit")+"\n\n";
                        outputForecast.append(sdf.format(date)+"\n\n");
                        outputForecast.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+forecastArray.getJSONObject(i).getString("IconPhrase")+"\n");
                        outputForecast.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + forecastArray.getJSONObject(i).getJSONObject("Temperature").getString("Value") + " "
                                        + forecastArray.getJSONObject(i).getJSONObject("Temperature").getString("Unit")+"\t\t\t\t\t\t\t\t\t\t\t\t");
                       // ImageSpan img = new ImageSpan(WeatherActivity.this, R.drawable.weather1);
                        //builder.setSpan(" "+img, builder.length()-1, builder.length(), 0);
                        outputForecast.append(" ");
                        String icon_name = "weather"+forecastArray.getJSONObject(i).getString("WeatherIcon");
                        int icon_id = getResources().getIdentifier(icon_name, "drawable", "com.jando.fitness_app");
                        outputForecast.setSpan(new ImageSpan(WeatherActivity.this, icon_id), outputForecast.length()-1, outputForecast.length(), 0);
                        outputForecast.append("\n\n");

                    }


                /** Output to current weather display */
                    forecastDisplay.setText(outputForecast);

                /** Uncomment to use when API key is available
                 } catch (InterruptedException e) {
                 e.printStackTrace();
                 } catch (ExecutionException e) {
                 e.printStackTrace();
                 }
                 //*/

                  //  weatherReport.setText(j2);
                    // API key 2
                    // j2 = weather.execute("https://dataservice.accuweather.com/currentconditions/v1/locationKey="+key+"?apikey=7WX6jWHRE1KlP7ueFS8BePxjeq9pFMQG&language=en-us&details=false").get();
                    // API key 3
                    //j2 = weather.execute("https://dataservice.accuweather.com/currentconditions/v1/locationKey="+key+"?apikey=ai7pJYg85quTbfX7pJZd7KTLyodA3oYG&language=en-us&details=false").get();




            } catch (JSONException e) {
                e.printStackTrace();
            }
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
