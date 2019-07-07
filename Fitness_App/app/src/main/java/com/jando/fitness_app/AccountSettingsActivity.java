package com.jando.fitness_app;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jando.fitness_app.Model.User;

public class AccountSettingsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail1;
    private TextView textViewUserEmail2;
    private TextView textViewUserPassword1;
    private TextView textViewUserPassword2;
    private DatabaseReference usersRef;
    final User userInfo = new User("", "", "", "", "", "");
    FirebaseUser user = null;

    /** What happens when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        //FireBase info to log out
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"getCurrentUser == null",Toast.LENGTH_SHORT).show();
        }

        user = firebaseAuth.getCurrentUser();

        textViewUserEmail1 = findViewById(R.id.editText4);
        textViewUserEmail1.setText(user.getEmail());
        textViewUserEmail2 = findViewById(R.id.editText44);
        textViewUserEmail2.setText(user.getEmail());
        textViewUserPassword1 = findViewById(R.id.editText6);
        textViewUserPassword2 = findViewById(R.id.editText66);
        Button buttonEmail = findViewById(R.id.button1);
        Button buttonPassword = findViewById(R.id.button2);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        displayUserInfo();

        // Update email address
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email1 = textViewUserEmail1.getText().toString().trim();
                final String email2 = textViewUserEmail2.getText().toString().trim();
                if(!email1.equals(email2)){
                    textViewUserEmail1.setError("Emails does not match");
                    textViewUserEmail1.requestFocus();
                    textViewUserEmail2.setError("Emails does not match");
                    textViewUserEmail2.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email1)) {
                    textViewUserEmail1.setError("Please type the email to change");
                    textViewUserEmail1.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email2)) {
                    textViewUserEmail2.setError("Please type the email to confirm new address");
                    textViewUserEmail2.requestFocus();
                    return;
                }
                if(userInfo.getEmail().equals(email1)){
                    textViewUserEmail1.setError("This is your current email address");
                    textViewUserEmail1.requestFocus();
                    return;
                }
                if(userInfo.getEmail().equals(email2)){
                    textViewUserEmail2.setError("This is your current email address");
                    textViewUserEmail2.requestFocus();
                    return;
                }
                final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                f_user.updateEmail(email1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AccountSettingsActivity.this,
                                            "User email is updated",Toast.LENGTH_SHORT).show();
                                    usersRef.child(f_user.getUid()).child("email").setValue(email1);
                                }
                            }
                        });
            }
        });

        // Update password
        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pass1 = textViewUserPassword1.getText().toString().trim();
                final String pass2 = textViewUserPassword2.getText().toString().trim();
                if(!pass1.equals(pass2)){
                    textViewUserPassword1.setError("Password does not match");
                    textViewUserPassword1.requestFocus();
                    textViewUserPassword2.setError("Password does not match");
                    textViewUserPassword2.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(pass1)) {
                    textViewUserPassword1.setError("Please type the password to change");
                    textViewUserPassword1.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(pass2)) {
                    textViewUserPassword2.setError("Please type the password to confirm");
                    textViewUserPassword2.requestFocus();
                    return;
                }
                final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                f_user.updatePassword(pass1)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AccountSettingsActivity.this,
                                            "User password is updated",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //hide keyboard after typing email2
        textViewUserEmail2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textViewUserEmail2.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        //hide keyboard after typing password2
        textViewUserPassword2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(textViewUserPassword2.getApplicationWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

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

    /** Display user info function */
    public void displayUserInfo(){
        // Display user information
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(AccountSettingsActivity.this,"Trying to find the user",Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataSnapshot email = ds.child("email");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
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
