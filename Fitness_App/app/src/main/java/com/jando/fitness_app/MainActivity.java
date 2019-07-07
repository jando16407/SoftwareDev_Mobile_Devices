package com.jando.fitness_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jando.fitness_app.Model.User;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegister;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private EditText editTextWeight;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextCaretakerPhone;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            goToHome();
        }

        progressDialog = new ProgressDialog(this);

        //views
        buttonRegister = findViewById(R.id.buttonSignin);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextInputPassword);
        editTextCaretakerPhone = findViewById(R.id.editTextCaretakerPhone);

        textViewSignin = findViewById(R.id.textViewSignUp);

        //closes keyboard when password editText is entered
        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editTextPassword.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        //buttons
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Gets all information to be stored in database
                    final User user = new User(editTextEmail.getText().toString(),
                            editTextFirstName.getText().toString(),
                            editTextLastName.getText().toString(),
                            editTextAge.getText().toString(),
                            editTextWeight.getText().toString(),
                            editTextCaretakerPhone.getText().toString());

                    //Stores User object inside database using email as label
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            /*
                            if(dataSnapshot.child(user.getUsername()).exists()){
                                Toast.makeText(MainActivity.this,
                                        "Username Already Registered!",
                                        Toast.LENGTH_SHORT).show();

                            }
                            else */if(userEmailExists(dataSnapshot, user)){
                                Toast.makeText(MainActivity.this,
                                        "Email Already Registered!\nPlease Login from Login Page",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //Registers user in FireBase authorization
                                registerUser();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this,
                                    "Database Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });

        textViewSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                //finish();
                startActivity(intent);
            }
        });
    }

    /** Function that is called to add new user to FireBase Authorization **/
    private void registerUser() {
        String firstname = editTextFirstName.getText().toString().trim();
        String lastname = editTextLastName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String weight = editTextWeight.getText().toString().trim();
        //String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String caretakerPhone = editTextCaretakerPhone.getText().toString().trim();

        //Gets all information to be stored in database
        final User user = new User(editTextEmail.getText().toString(),
                editTextFirstName.getText().toString(),
                editTextLastName.getText().toString(),
                editTextAge.getText().toString(),
                editTextWeight.getText().toString(),
                editTextCaretakerPhone.getText().toString());

        //Errors if any field is incorrectly filled out
        if (TextUtils.isEmpty(firstname)) {
            editTextFirstName.setError("First Name Required");
            editTextFirstName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            editTextLastName.setError("Last Name Required");
            editTextLastName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(age)) {
            editTextAge.setError("Age Required");
            editTextAge.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(weight)) {
            editTextWeight.setError("Weight Required");
            editTextWeight.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email Required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid Email");
            editTextEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            editTextPassword.setError("Password must be 6 characters long");
            editTextPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(caretakerPhone)) {
            editTextCaretakerPhone.setError("Caretaker Phone Required");
            editTextCaretakerPhone.requestFocus();
            return;
        }

        //Registers user with email and password
        progressDialog.setMessage("Registering Please wait");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,
                            "Registered Successfully",
                            Toast.LENGTH_SHORT).show();
                    //Stores User object inside database using UID as label
                    firebaseAuth = FirebaseAuth.getInstance();
                    if(firebaseAuth.getCurrentUser() == null){
                        Toast.makeText(MainActivity.this,
                                "getCurrentUser == null",Toast.LENGTH_SHORT).show();
                    }
                    FirebaseUser f_user = firebaseAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this,
                            "UID: "+f_user.getUid(),
                            Toast.LENGTH_SHORT).show();
                    usersRef.child(f_user.getUid()).setValue(user);
                    finish();
                    goToHome();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Could not register. Please try again",
                            Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }

    private boolean userEmailExists(DataSnapshot dataSnapshot, User user) {
        //Iterate thorough children to find email matching
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            DataSnapshot email = ds.child("email");
            if (email.getValue().toString().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
