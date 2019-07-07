package com.jando.fitness_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AccountFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private FirebaseAuth firebaseAuth;

    /** What happens when fragment is created */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        mContext = getActivity();

        Button EmergencyBTN = v.findViewById(R.id.EmergencyBTN);
        EmergencyBTN.setOnClickListener(this);

        Button userAccountSettings = v.findViewById(R.id.button_user_account_settings);
        Button userInformationSettings = v.findViewById(R.id.button_user_settings);
        userAccountSettings.setOnClickListener(this);
        userInformationSettings.setOnClickListener(this);
        return v;
    }

    /** Button functionality */
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.button_user_account_settings:
                Toast.makeText(getContext(),
                        "Account Settings Clicked",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), LoginForAccountSettings.class);
                startActivity(intent);
                break;

            case R.id.button_user_settings:
                Toast.makeText(getContext(),
                        "User Information Settings Clicked",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), UserSettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.EmergencyBTN:
                Toast.makeText(getContext(),
                        "Emergency Clicked",
                        Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle("Emergency!");
                builder.setMessage("Who do you want to call?");

                builder.setNeutralButton("Care Taker", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference mRef = database.getReference("Users");

                        firebaseAuth = FirebaseAuth.getInstance();
                        final FirebaseUser user = firebaseAuth.getCurrentUser();

                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                               String caretakerPhonenumber = (String) dataSnapshot.child(user.getUid()).child("caretakerPhone").getValue();

                                Intent callIntent = new Intent(Intent.ACTION_DIAL); // or ACTION_DIAL
                                callIntent.setData(Uri.parse("tel:"+caretakerPhonenumber));

                                startActivity(callIntent);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("911", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String number = "18882378289";  //Best Buy Customer Service
                        Intent callIntent = new Intent(Intent.ACTION_DIAL); // or ACTION_DIAL
                        callIntent.setData(Uri.parse("tel:"+number));

                        startActivity(callIntent);
                    }
                });

                builder.show();
                break;

            default:
                Toast.makeText(getContext(),
                        "Default Button Response",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
