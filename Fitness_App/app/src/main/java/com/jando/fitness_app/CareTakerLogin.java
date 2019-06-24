package com.jando.fitness_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class CareTakerLogin extends AppCompatActivity {

    private TextInputEditText editTextInputPassword;
    private Button buttonSignin;
    private Button Signout_btn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewForgotpassword;
    private TextView textViewSignup;
    private TextView textView_findElder;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private SignInButton sign_in_button;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_login);
        //Views
        sign_in_button = findViewById(R.id.sign_in_button);
        progressDialog = new ProgressDialog(this);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextInputPassword = findViewById(R.id.editTextInputPassword);
        buttonSignin = findViewById(R.id.buttonSignin);
        textViewSignup = findViewById(R.id.textViewSignUp);
        textViewForgotpassword = findViewById(R.id.textViewforgotPassword);
        textView_findElder = findViewById(R.id.textView_backtologin);
        //change
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("434381172772-nab3ojvfhn78s3s6en73mdbmg9pk30ak.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        firebaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //Google Sign in button
        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        //Buttons listeners
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
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

    //protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        //the GoogleSignInAccount will be non-null.
        //super.onStart();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
       // if(account != null) {
        //    finish();
         //   startActivity(new Intent(getApplicationContext(),HomeScreen.class));
        //    updateUI2(account);
        //    Toast.makeText(CareTakerLogin.this,"ALREADY LOG IN",Toast.LENGTH_SHORT).show();
       // }
    //}

    protected void onDestroy() {
        super.onDestroy();
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();
        //Intent intent = new Intent(v.getContext(), LoginActivity.class);
        finish();
        //startActivity(intent);
    }

    //email and password
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
                    startActivity(new Intent(getApplicationContext(), ElderMapLocation.class));
                } else {
                    Toast.makeText(CareTakerLogin.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //signs in to Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(CareTakerLogin.this,"FAILED",Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately

                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    //Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CareTakerLogin.this,"Login Successful",Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Toast.makeText(CareTakerLogin.this,"Login Failed",Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        finish();
        Intent intent = new Intent(CareTakerLogin.this,ElderMapLocation.class);
        startActivity(intent);
    }

    private void updateUI2(GoogleSignInAccount user) {
        //if(user != null)
        //Signout_btn.setVisibility(View.VISIBLE);
        //finish();
        //Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
        //startActivity(intent);
    }
    //Signs out of google and FireBase Auth

}