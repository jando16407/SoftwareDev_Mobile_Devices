package com.jando.fitness_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class WeatherActivity extends AppCompatActivity {

    String j1 = null;
    String j2 = null;
    EditText locationName;
    String key;
    TextView weatherReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        locationName = findViewById(R.id.locationName);
        weatherReport = findViewById(R.id.resultView);

        //Adds back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void weatherCheck (View view){
        WeatherTask task = new WeatherTask();

  //      InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
  //      mgr.hideSoftInputFromWindow(locationName.getWindowToken(), 0);
        /** Uncomment to use when API key is available
        try {
            // API key 1
            //j1 = task.execute("https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=ybpRsMsZ0gudQicb50c9Pgv793X2HeLH&q="+locationName.getText().toString()+"&language=en-us&details=false&toplevel=false").get();
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
   /*     } catch(InterruptedException e) {
            Toast.makeText(WeatherActivity.this, "Error trying loc interruption", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(WeatherActivity.this, "Error trying loc exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*/
    }

    public class WeatherTask extends AsyncTask<String, Void, String> {

        /** Get the location "Key"*/
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
                JSONObject object = new JSONObject(j1);
                // get the location key
                for (int i = 0; i<object.length(); i++) {
                    key = object.getString("Key");
                }
//                weatherReport.setText(key);
                /** Uncomment to use when API key is available
                try {
                    WeatherTask weather = new WeatherTask();
                    // API key 1
                    j2 = weather.execute("https://dataservice.accuweather.com/currentconditions/v1/locationKey="+key+"?apikey=ybpRsMsZ0gudQicb50c9Pgv793X2HeLH&language=en-us&details=false").get();
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
                    JSONArray weatherArray = new JSONArray(j2);
                String output = "";
                    for (int i = 0; i<weatherArray.length(); i++) {

                        JSONObject weatherPart = weatherArray.getJSONObject(i);
                        output += "Weather: " + weatherPart.getString("WeatherText") + "\n";
                        output += "Temperature: " + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Imperial").getString("Value") + " " + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Imperial").getString("Unit") + " / " + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Metric").getString("Value") + " " + weatherPart.getJSONObject("Temperature").
                                getJSONObject("Metric").getString("Unit")+"\n";
                    }

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                String epochString = "1562443200";
                long epoch = Long.parseLong( epochString );
                Date expiry = new Date( epoch*1000 );
                output+="Date: "+sdf.format(expiry);
                weatherReport.setText(output);
                  //  weatherReport.setText(j2);



                    // API key 2
                    // j2 = weather.execute("https://dataservice.accuweather.com/currentconditions/v1/locationKey="+key+"?apikey=7WX6jWHRE1KlP7ueFS8BePxjeq9pFMQG&language=en-us&details=false").get();
                    // API key 3
                    //j2 = weather.execute("https://dataservice.accuweather.com/currentconditions/v1/locationKey="+key+"?apikey=ai7pJYg85quTbfX7pJZd7KTLyodA3oYG&language=en-us&details=false").get();
                /** Uncomment to use when API key is available
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                */
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
