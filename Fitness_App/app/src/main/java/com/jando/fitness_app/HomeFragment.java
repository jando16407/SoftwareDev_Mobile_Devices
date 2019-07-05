package com.jando.fitness_app;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                numSteps = 0;
                writeFile();
                TvSteps.setText(TEXT_NUM_STEPS + readFile());
                sensorManager.registerListener(HomeFragment.this, accel,
                        SensorManager.SENSOR_DELAY_FASTEST);
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                writeFile();
                sensorManager.unregisterListener(HomeFragment.this);
            }
        });

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

            FileOutputStream fileOutputStream = context.openFileOutput("LocalInfo.txt", MODE_PRIVATE);
            fileOutputStream.write(intToSave.getBytes());
            fileOutputStream.close();

            Toast.makeText(getContext(), "Step saved successfully", Toast.LENGTH_LONG).show();

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
            StringBuffer stringBuffer = new StringBuffer();

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
}
