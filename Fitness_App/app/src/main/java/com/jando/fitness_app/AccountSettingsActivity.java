package com.jando.fitness_app;

import android.accounts.Account;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jando.fitness_app.Model.User;

public class AccountSettingsActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private TextView textViewUserPassword1;
    private TextView textViewUserPassword2;
    private TextView textViewUserAge;
    private TextView textViewUserWeight;
    private TextView textViewUserFirstName;
    private TextView textViewUserLastName;
    //private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private Button buttonEmail;
    private Button buttonAge;
    private Button buttonWeight;
    private Button buttonFirstName;
    private Button buttonLastName;
    private Button buttonPassword;
    final User userInfo = new User("", "", "", "", "");
    FirebaseUser user = null;

    /** What happens when activity is started */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        /*
        //Gets Google info so it can log out
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("434381172772-nab3ojvfhn78s3s6en73mdbmg9pk30ak.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
*/

        //firebase info to log out
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"getCurrentUser == null",Toast.LENGTH_SHORT).show();
            //finish();
            //startActivity(new Intent(this, LoginActivity.class));
        }


        user = firebaseAuth.getCurrentUser();

        //textViewUserName = findViewById(R.id.editText2);
        //textViewUserName.setText(user.getu);
        //firebaseAuth = FirebaseAuth.getInstance();
        textViewUserEmail = findViewById(R.id.editText4);
        textViewUserEmail.setText(user.getEmail());
        textViewUserPassword1 = findViewById(R.id.editText6);
        //textViewUserPassword1.setText("Type password to update");
        textViewUserPassword2 = findViewById(R.id.editText66);
        //textViewUserPassword2.setText("Type password to update");
        textViewUserAge = findViewById(R.id.editText8);
        textViewUserWeight = findViewById(R.id.editText10);
        textViewUserFirstName = findViewById(R.id.editText12);
        textViewUserLastName = findViewById(R.id.editText16);
        buttonEmail = findViewById(R.id.button1);
        buttonPassword = findViewById(R.id.button2);
        buttonAge = findViewById(R.id.button3);
        buttonWeight = findViewById(R.id.button4);
        buttonFirstName = findViewById(R.id.button5);
        buttonLastName = findViewById(R.id.button6);


        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        displayUserInfo();

        String firstname = textViewUserFirstName.getText().toString().trim();
        String lastname = textViewUserLastName.getText().toString().trim();
        String age = textViewUserAge.getText().toString().trim();
        String weight = textViewUserWeight.getText().toString().trim();
        //String email;
        String password1 = textViewUserPassword1.getText().toString().trim();
        String password2 = textViewUserPassword2.getText().toString().trim();



        /** Update email address */
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 final String email = textViewUserEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    textViewUserEmail.setError("Please type the email to change");
                    textViewUserEmail.requestFocus();
                    return;
                }
                else if(userInfo.getEmail().equals(email)){
                    textViewUserEmail.setError("This is your current email address");
                    textViewUserEmail.requestFocus();
                }
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(userEmailExists(dataSnapshot, email)){
                            textViewUserEmail.setError("This email address is already registered");
                            textViewUserEmail.requestFocus();
                        }
                        else{
                            userInfo.setEmail(email);
                            final FirebaseUser f_user = firebaseAuth.getInstance().getCurrentUser();
                           // AuthCredential credential = EmailAuthProvider
                             //       .getCredential(f_user.getEmail(), f_user.getPasswor);
                            f_user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AccountSettingsActivity.this, "User email address updated",Toast.LENGTH_SHORT).show();
                                        usersRef.child(f_user.getUid()).setValue(userInfo);
                                    }
                                    else{
                                        Toast.makeText(AccountSettingsActivity.this, "User email update Failed",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


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
        /** Display user information */
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(AccountSettingsActivity.this,"Trying to find the user",Toast.LENGTH_SHORT).show();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataSnapshot email = ds.child("email");
                    //Toast.makeText(AccountSettingsActivity.this,ds.getValue().toString()+" and "+user.getUid(),Toast.LENGTH_SHORT).show();
                    //if(ds.getValue().toString().equals(user.getUid())){
                    if (email.getValue().toString().equals(user.getEmail())) {
                        Toast.makeText(AccountSettingsActivity.this,"Found the user, "+ds.child("age").getValue().toString()+", "
                                +ds.child("weight").getValue().toString()+", "+ds.child("firstname").getValue().toString()+", "
                                +ds.child("lastname").getValue().toString(),Toast.LENGTH_SHORT).show();
                        textViewUserAge.setText(ds.child("age").getValue().toString());
                        textViewUserWeight.setText(ds.child("weight").getValue().toString());
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

    /** Cheeck if the given email is already registered on firebase */
    private boolean userEmailExists(DataSnapshot dataSnapshot, String email_address) {
        //Iterate thorough children to find email matching
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            DataSnapshot email = ds.child("email");
            if (email.getValue().toString().equals(email_address)) {
                //Toast.makeText(AccountSettingsActivity.this, "Email is "+email.getValue().toString(), Toast.LENGTH_LONG).show();
                return true;
            }

        }
        return false;
    }
}
