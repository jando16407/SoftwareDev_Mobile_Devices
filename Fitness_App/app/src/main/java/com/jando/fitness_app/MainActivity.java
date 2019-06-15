package com.jando.fitness_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            goToHome();
        }

        progressDialog = new ProgressDialog(this);

        //views
        buttonRegister = findViewById(R.id.buttonSignin);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextInputPassword);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        textViewSignin = findViewById(R.id.textViewSignup);


        //buttons
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
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

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final Bundle userInfo = new Bundle();
        userInfo.putString("EMAIL", email);
        userInfo.putString("PW", password);
        userInfo.putString("FIRST_NAME", editTextFirstName.getText().toString().trim());
        userInfo.putString("LAST_NAME", editTextLastName.getText().toString().trim());
        addUserToDatabase(userInfo);
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering Please wait");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                    addUserToDatabase(userInfo);
                    finish();
                    goToHome();
                } else {
                    Toast.makeText(MainActivity.this, "Could not register.. Please try again", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void goToHome() {
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        startActivity(intent);
    }

    private void addUserToDatabase(Bundle userInfo){
        String email = userInfo.getString("EMAIL");
        String password = userInfo.getString("PW");
        String firstName = userInfo.getString("FIRST_NAME");
        String lastName = userInfo.getString("LAST_NAME");
        //progressDialog.setMessage("email:"+email+" pw:"+password+" name:"+firstName+" name:"+lastName);
        //progressDialog.show();
        /* Access firebase and push data */
    }
}
