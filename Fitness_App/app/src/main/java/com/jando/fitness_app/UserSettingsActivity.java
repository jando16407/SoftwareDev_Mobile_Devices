package com.jando.fitness_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jando.fitness_app.Model.User;

public class UserSettingsActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserAge;
    private TextView textViewUserWeight;
    private TextView textViewUserHeight1;
    private TextView textViewUserHeight2;
    private Switch   switchSexMale;
    private Switch   switchSexFemale;
    private TextView textViewUserFirstName;
    private TextView textViewUserLastName;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private Button buttonAge;
    private Button buttonWeight;
    private Button buttonHeight;
    private Button buttonSex;
    private Button buttonFirstName;
    private Button buttonLastName;
    final User userInfo = new User("", "", "", "", "");
    FirebaseUser user = null;

    /** What happens when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        //FireBase info to log out
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"getCurrentUser == null",Toast.LENGTH_SHORT).show();
            //finish();
            //startActivity(new Intent(this, LoginActivity.class));
        }


        user = firebaseAuth.getCurrentUser();

        textViewUserAge = findViewById(R.id.ageEdit);
        textViewUserWeight = findViewById(R.id.weightEdit);
        textViewUserHeight1 = findViewById(R.id.heightEdit1);
        textViewUserHeight2 = findViewById(R.id.heightEdit2);
        switchSexMale = findViewById(R.id.sexSwitchMale);
        switchSexFemale = findViewById(R.id.sexSwitchFemale);
        textViewUserFirstName = findViewById(R.id.firstEdit);
        textViewUserLastName = findViewById(R.id.lastEdit);
        buttonAge = findViewById(R.id.ageButton);
        buttonWeight = findViewById(R.id.weightButton);
        buttonHeight = findViewById(R.id.heightButton);
        buttonSex = findViewById(R.id.sexButton);
        buttonFirstName = findViewById(R.id.firstButton);
        buttonLastName = findViewById(R.id.lastButton);


        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        displayUserInfo();

        String firstname = textViewUserFirstName.getText().toString().trim();
        String lastname = textViewUserLastName.getText().toString().trim();
        String age = textViewUserAge.getText().toString().trim();
        final String weight = textViewUserWeight.getText().toString().trim();
        final String height1 = textViewUserHeight1.getText().toString().trim();
        final String height2 = textViewUserHeight2.getText().toString().trim();
        final String sex;




        // Update Age
        buttonAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String age = textViewUserAge.getText().toString().trim();
                if (TextUtils.isEmpty(age)) {
                    textViewUserAge.setError("Please type the age to change");
                    textViewUserAge.requestFocus();
                    return;
                }
                else if(userInfo.getEmail().equals(age)){
                    textViewUserAge.setError("This is your current age");
                    textViewUserAge.requestFocus();
                }
                final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                usersRef.child(f_user.getUid()).child("age").setValue(age);
                Toast.makeText(UserSettingsActivity.this,
                        "User age is updated",Toast.LENGTH_SHORT).show();

            }
        });

        // Update Weight
        buttonWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String weight = textViewUserWeight.getText().toString().trim();
                if (TextUtils.isEmpty(weight)) {
                    textViewUserWeight.setError("Please type the weight to change");
                    textViewUserWeight.requestFocus();
                    return;
                }
                else if(userInfo.getEmail().equals(weight)){
                    textViewUserWeight.setError("This is your current weight");
                    textViewUserWeight.requestFocus();
                }
                final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                usersRef.child(f_user.getUid()).child("weight").setValue(weight);
                Toast.makeText(UserSettingsActivity.this,
                        "User weight is updated",Toast.LENGTH_SHORT).show();
            }
        });

        //Update height
        buttonHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int height1;
                final int height2;
                final int height;
                if( textViewUserHeight1.getText() != null && textViewUserHeight2.getText() != null && !textViewUserHeight1.getText().toString().trim().equals("") && !textViewUserHeight2.getText().toString().trim().equals("")) {
                    Toast.makeText(UserSettingsActivity.this,
                            "height1: "+textViewUserHeight1.getText().toString().trim()+", height2: "+textViewUserHeight2.getText().toString().trim(),Toast.LENGTH_SHORT).show();
                    height1 = Integer.parseInt(textViewUserHeight1.getText().toString().trim());
                    height2 = Integer.parseInt(textViewUserHeight2.getText().toString().trim());
                    height = height1*12 + height2;
                    if (TextUtils.isEmpty(Integer.toString(height1))) {
                        textViewUserHeight1.setError("Please type the weight to change");
                        textViewUserHeight1.requestFocus();
                        return;
                    }
                    else if (TextUtils.isEmpty(Integer.toString(height2))) {
                        textViewUserHeight2.setError("Please type the weight to change");
                        textViewUserHeight2.requestFocus();
                        return;
                    }
                     final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                     usersRef.child(f_user.getUid()).child("height").setValue(height);
                    Toast.makeText(UserSettingsActivity.this,
                            "User height is updated: "+height,Toast.LENGTH_SHORT).show();

                }
                else {
                    textViewUserHeight1.setError("Please type the height to change");
                    textViewUserHeight1.requestFocus();

                    return;
                }


            }
        });

        // Update Sex
        buttonSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sex;// = textViewUserWeight.getText().toString().trim();
                if(switchSexMale.isChecked()==true){
                    if(switchSexFemale.isChecked()==true){
                        switchSexMale.setError("Please check only one");
                        switchSexMale.requestFocus();
                        switchSexFemale.setError("Please check only one");
                        switchSexFemale.requestFocus();
                        return;
                    }
                    else {
                        sex = "M";
                    }
                }
                else {
                    if(switchSexFemale.isChecked()==false) {
                        switchSexMale.setError("Please check male or female");
                        switchSexMale.requestFocus();
                        switchSexFemale.setError("Please check male or female");
                        switchSexFemale.requestFocus();
                        return;
                    }
                    else {
                        sex = "F";
                    }
                }
                final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                usersRef.child(f_user.getUid()).child("sex").setValue(sex);
                Toast.makeText(UserSettingsActivity.this,
                        "User sex is updated",Toast.LENGTH_SHORT).show();
            }
        });

        // Update First Name
        buttonFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String firstname = textViewUserFirstName.getText().toString().trim();
                if (TextUtils.isEmpty(firstname)) {
                    textViewUserFirstName.setError("Please type the first name to change");
                    textViewUserFirstName.requestFocus();
                    return;
                }
                else if(userInfo.getEmail().equals(firstname)){
                    textViewUserFirstName.setError("This is your current first name");
                    textViewUserFirstName.requestFocus();
                }
                final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                usersRef.child(f_user.getUid()).child("firstname").setValue(firstname);
                Toast.makeText(UserSettingsActivity.this,
                        "User first name is updated",Toast.LENGTH_SHORT).show();

            }
        });

        // Update Last Name
        buttonLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String lastname = textViewUserLastName.getText().toString().trim();
                if (TextUtils.isEmpty(lastname)) {
                    textViewUserLastName.setError("Please type the last name to change");
                    textViewUserLastName.requestFocus();
                    return;
                }
                else if(userInfo.getEmail().equals(lastname)){
                    textViewUserLastName.setError("This is your current last name");
                    textViewUserLastName.requestFocus();
                }
                final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();

                Toast.makeText(UserSettingsActivity.this,
                        "User last name is updated",Toast.LENGTH_SHORT).show();
                usersRef.child(f_user.getUid()).child("lastname").setValue(lastname);
            }
        });
        //Adds back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    /** Display user info function */
    public void displayUserInfo(){
        //Display user information
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(UserSettingsActivity.this,
                        "Trying to find the user",Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataSnapshot email = ds.child("email");
                    if (email.getValue().toString().equals(user.getEmail())) {
                        Toast.makeText(UserSettingsActivity.this,
                                "Found the user, "+ds.child("age").getValue().toString()+", "
                                + ds.child("weight").getValue().toString()
                                + ", "+ds.child("firstname").getValue().toString()+", "
                                + ds.child("lastname").getValue().toString(),
                                Toast.LENGTH_SHORT).show();
                        textViewUserAge.setText(ds.child("age").getValue().toString());
                        textViewUserWeight.setText(ds.child("weight").getValue().toString());
                        int height;
                        if( ds.child("height").getValue() != null ) {
                            height = Integer.parseInt(ds.child("height").getValue().toString());
                            textViewUserHeight1.setText(Integer.toString(height/12));
                            textViewUserHeight2.setText(Integer.toString(height%12));
                            userInfo.setHeight(Integer.toString(height));
                        }

                        String sex;
                        if( ds.child("sex").getValue() != null ) {
                            sex = ds.child("sex").getValue().toString();
                            if (sex.equals("M")) {
                                switchSexMale.setChecked(true);
                                switchSexMale.setChecked(false);
                            } else if (sex.equals("F")) {
                                switchSexMale.setChecked(true);
                                switchSexFemale.setChecked(false);
                            }
                            userInfo.setSex(sex);
                        }
                        textViewUserFirstName.setText(ds.child("firstname").getValue().toString());
                        textViewUserLastName.setText(ds.child("lastname").getValue().toString());


                        userInfo.setFirstname(textViewUserFirstName.getText().toString());
                        userInfo.setLastname(textViewUserLastName.getText().toString());
                        userInfo.setAge(textViewUserAge.getText().toString());
                        userInfo.setWeight(textViewUserWeight.getText().toString());


                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /** Check if the given email is already registered on FireBase */
    private boolean userEmailExists(DataSnapshot dataSnapshot, String email_address) {
        //Iterate thorough children to find email matching
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            DataSnapshot email = ds.child("email");
            if (email.getValue().toString().equals(email_address)) {
                return true;
            }
        }
        return false;
    }
}
