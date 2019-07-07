package com.jando.fitness_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NutritionActivity extends AppCompatActivity {

    TextView textViewBMI, textViewCalorieM, textViewCalorieU, textViewCalorieO;
    private final double CALORIES_PER_POUND_FAT = 3500;
    private final double CALORIES_PER_DAY_PER_POUND = CALORIES_PER_POUND_FAT / 7;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseUser = null;

    /** What happens when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        textViewBMI = findViewById(R.id.textViewBMI);
        textViewCalorieM = findViewById(R.id.textViewCalorieM);
        textViewCalorieU = findViewById(R.id.textViewCalorieU);
        textViewCalorieO = findViewById(R.id.textViewCalorieO);

        //FireBase checks to ensure user is logged in
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,
                    "getCurrentUser == null",
                    Toast.LENGTH_SHORT).show();
        }

        firebaseUser = firebaseAuth.getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        displayUserInfo();

        //Adds back button
        assert getSupportActionBar() != null;
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

    /**Function that gets user info from database, calculates necessary information */
    private void displayUserInfo() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bmi = "";
                String sex;
                int age, days, height, weight;
                double calories = 0;
                double under, over;

                try {
                    //Searches through users, finding user associated with fireBaseUser email
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        DataSnapshot email = ds.child("email");
                        if (email.getValue().toString().equals(firebaseUser.getEmail())){

                            //gets all information necessary to calculate calories
                            bmi = ds.child("bmi").getValue().toString();
                            sex = ds.child("sex").getValue().toString();
                            age = Integer.parseInt(ds.child("age").getValue().toString());
                            days = Integer.parseInt(ds.child("exerciseDays").getValue().toString());
                            height = Integer.parseInt(ds.child("height").getValue().toString());
                            weight = Integer.parseInt(ds.child("weight").getValue().toString());

                            //calculate calories
                            calories = calculateCalories(sex, height, weight, age, days);
                        }
                    }

                    under = calories - CALORIES_PER_DAY_PER_POUND;
                    over = calories + CALORIES_PER_DAY_PER_POUND;

                    textViewBMI.setText("Your current BMI is: " + bmi);
                    textViewCalorieM.setText("Daily calories to maintain weight: " + calories);
                    textViewCalorieU.setText("Daily calories to lose 1 pound per week: " + under);
                    textViewCalorieO.setText("Daily calories to gain 1 pound per week: " + over);

                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private double calculateCalories(String sex, int height, int weight, int age, int days) {
        double BMR, returnCalories;
        if (sex.equals("M")){
            //male calorie calculation
            BMR = 66 + (6.3 * weight) + (12.9 * height) - (6.8 * age);
        } else {
            //female calorie calculation
            BMR = 655 + (4.3 * weight) + (4.7 * height) - (4.7 * age);
        }
        returnCalories = BMR * calculateActivityMultiplier(days);
        return returnCalories;
    }

    private double calculateActivityMultiplier(int days) {
        switch(days){
            case 1:
            case 2:
                return 1.375;
            case 3:
            case 4:
            case 5:
                return 1.55;
            case 6:
            case 7:
                return 1.725;
        }
        return 1.2;
    }

}

