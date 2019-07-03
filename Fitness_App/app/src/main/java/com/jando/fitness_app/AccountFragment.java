package com.jando.fitness_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private Context mContext;
    private TextView alertTextView;
    private Button EmergencyBTN;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        mContext = getActivity();

        EmergencyBTN = v.findViewById(R.id.EmergencyBTN);
        EmergencyBTN.setOnClickListener(this);

        Button userAccountSettings = v.findViewById(R.id.button_user_account_settings);
        Button userInformationSettings = v.findViewById(R.id.button_user_settings);
        userAccountSettings.setOnClickListener(this);
        userInformationSettings.setOnClickListener(this);
        return v;
    }

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

                        String number = "8133250497";   // Alfredo number
                        Intent callIntent = new Intent(Intent.ACTION_CALL); // or ACTION_DIAL
                        callIntent.setData(Uri.parse("tel:"+number));

                        startActivity(callIntent);


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
                        Intent callIntent = new Intent(Intent.ACTION_CALL); // or ACTION_DIAL
                        callIntent.setData(Uri.parse("tel:"+number));

                        startActivity(callIntent);


                    }
                });


                builder.show();
                break;
                /*
            case R.id.buttonNutrition:
                Toast.makeText(getContext(),
                        "Nutrition Clicked",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), NutritionActivity.class);
                startActivity(intent);
                break;*/
            default:
                Toast.makeText(getContext(),
                        "Default Button Response",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
/*
    private void GoToAccountSettingsPage(View view){
        Intent intent = new Intent(HomeScreen.this, AccountSettingsActivity.class);
        startActivity(intent);
    }
*/

}
