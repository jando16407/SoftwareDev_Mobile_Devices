package com.jando.fitness_app;

import android.content.Intent;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.IntBuffer;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.SENSOR_SERVICE;

public class HomeFragment extends Fragment implements SensorEventListener, StepCounterActivity{

    private TextView TvSteps;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps = 0;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        TvSteps = v.findViewById(R.id.textViewSteps);
        Button BtnStart = v.findViewById(R.id.buttonStartSteps);
        Button BtnStop = v.findViewById(R.id.buttonStopSteps);
        Button BtnReset = v.findViewById(R.id.buttonResetSteps);
        Button WeatherButton = v.findViewById(R.id.button_weather);

        if (readFile() != null) {
            numSteps = Integer.parseInt(readFile());
            TvSteps.setText(TEXT_NUM_STEPS + readFile());
        } else {
            numSteps = 0;
            TvSteps.setText(TEXT_NUM_STEPS + numSteps);
        }

        sensorManager.registerListener(HomeFragment.this, accel,
                SensorManager.SENSOR_DELAY_FASTEST);

        BtnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sensorManager.registerListener(HomeFragment.this, accel,
                        SensorManager.SENSOR_DELAY_FASTEST);
            }
        });

        BtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                numSteps = 0;
                writeFile();
                TvSteps.setText(TEXT_NUM_STEPS + readFile());
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                writeFile();
                sensorManager.unregisterListener(HomeFragment.this);
            }
        });

        WeatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                Toast.makeText(getContext(),
                        "Weather Clicked",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
            }
        });

        getButtonWeather(WeatherButton);


        return v;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    //every time a step is detected
    @Override
    public void step(long timeNs) {
        numSteps++;
        writeFile();
        if (HomeFragment.this.isVisible())
            TvSteps.setText(TEXT_NUM_STEPS + readFile());
    }

    private void writeFile() {
        String intToSave = numSteps + "";

        try {

            FileOutputStream fileOutputStream =
                    context.openFileOutput("LocalInfo.txt", MODE_PRIVATE);
            fileOutputStream.write(intToSave.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private String readFile() {
        try {

            FileInputStream fileInputStream = context.openFileInput("LocalInfo.txt");

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

    private String readCurrentWeather() {
        try {
            //Context con = WeatherActivity.this;
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

    private void getButtonWeather(Button WeatherButton ){
        /** Display the current weather in the weather button */
        //Toast.makeText(getContext(), "j2 = "+readCurrentWeather(), Toast.LENGTH_SHORT).show();
        String j2 = null;
        j2 = readCurrentWeather();
        if( j2 != null ) {
            try {
                /** Build the output */
                JSONArray weatherArray = new JSONArray(j2);
                SpannableStringBuilder output = new SpannableStringBuilder();
                for (int i = 0; i < weatherArray.length(); i++) {

                    JSONObject weatherPart = weatherArray.getJSONObject(i);
                    String icon_name = "weather" + weatherPart.getString("WeatherIcon");
                    int icon_id = getResources().getIdentifier(icon_name, "drawable", "com.jando.fitness_app");
                    output.append("\t" + weatherPart.getString("WeatherText") + "\n");
                    output.append("\t\t\t" + weatherPart.getJSONObject("Temperature").
                            getJSONObject("Imperial").getString("Value") + " " + weatherPart.getJSONObject("Temperature").
                            getJSONObject("Imperial").getString("Unit") + " / " + weatherPart.getJSONObject("Temperature").
                            getJSONObject("Metric").getString("Value") + " " + weatherPart.getJSONObject("Temperature").
                            getJSONObject("Metric").getString("Unit") + "\t\t\t\t\t\t");
                    output.append(" ");
                    output.setSpan(new ImageSpan(getContext(), icon_id), output.length() - 1, output.length(), 0);
                    output.append("\n");
                    WeatherButton.setText(output);
                    /** Set the background */
                    int iconId = Integer.parseInt(weatherPart.getString("WeatherIcon"));
                    String backgroundId = "sky";
                    if( (1<=iconId && iconId <= 5) || iconId==30 || iconId==31){
                        backgroundId+="1";
                        WeatherButton.setBackgroundResource(getResources().getIdentifier(backgroundId, "drawable", "com.jando.fitness_app"));
                        WeatherButton.setTextColor(Color.parseColor("#FF6400"));
                    }
                    else if( (6<=iconId && iconId<=11) || (19<=iconId && iconId<=21) || iconId==32){
                        backgroundId+="2";
                        WeatherButton.setBackgroundResource(getResources().getIdentifier(backgroundId, "drawable", "com.jando.fitness_app"));
                        WeatherButton.setTextColor(Color.parseColor("#ffffff"));
                    }
                    else if( (12<=iconId && iconId<=14) || (16<iconId && iconId<=18) || (22<=iconId && iconId<=29) ){
                        backgroundId+="3";
                        WeatherButton.setBackgroundResource(getResources().getIdentifier(backgroundId, "drawable", "com.jando.fitness_app"));
                        WeatherButton.setTextColor(Color.parseColor("#ffffff"));
                    }
                    else if( iconId == 15 || iconId==41 || iconId==42 ){
                        backgroundId+="4";
                        WeatherButton.setBackgroundResource(getResources().getIdentifier(backgroundId, "drawable", "com.jando.fitness_app"));
                        WeatherButton.setTextColor(Color.parseColor("#19FF00"));
                    }
                    else if( iconId==33 || iconId==34 ){
                        backgroundId+="5";
                        WeatherButton.setBackgroundResource(getResources().getIdentifier(backgroundId, "drawable", "com.jando.fitness_app"));

                        WeatherButton.setTextColor(Color.parseColor("#ffffff"));
                    }
                    else if( (35<=iconId && iconId<=38) || iconId==43 || iconId==44 ){
                        backgroundId+="6";
                        WeatherButton.setBackgroundResource(getResources().getIdentifier(backgroundId, "drawable", "com.jando.fitness_app"));
                        WeatherButton.setTextColor(Color.parseColor("#ffffff"));
                    }
                    else if( iconId==39 || iconId==40 ){
                        backgroundId+="7";
                        WeatherButton.setBackgroundResource(getResources().getIdentifier(backgroundId, "drawable", "com.jando.fitness_app"));
                        WeatherButton.setTextColor(Color.parseColor("#ffffff"));
                    }
                }

                /** Output to current weather display */
                // WeatherButton.setText(output);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
