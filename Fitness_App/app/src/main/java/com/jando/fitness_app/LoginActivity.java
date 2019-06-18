package com.jando.fitness_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextInputPassword;
    private Button buttonSignin;
    private Button Signout_btn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewForgotpassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private SignInButton sign_in_button;
    private int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Views
        progressDialog = new ProgressDialog(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextInputPassword = findViewById(R.id.editTextInputPassword);
        buttonSignin = findViewById(R.id.buttonSignin);
        textViewSignup = findViewById(R.id.textViewSignUp);
        textViewForgotpassword = findViewById(R.id.textViewforgotPassword);


        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();


        //Buttons listeners
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                //finish();
                startActivity(intent);
            }
        });

        textViewForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent3);
            }
        });

        //closes keyboard when password editText is entered
        editTextInputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(editTextInputPassword.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextInputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in ...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateUI(FirebaseUser user) {
        finish();
        Intent intent = new Intent(LoginActivity.this,HomeScreen.class);
        startActivity(intent);
    }
    //Signs out of google and FireBase Auth

}

