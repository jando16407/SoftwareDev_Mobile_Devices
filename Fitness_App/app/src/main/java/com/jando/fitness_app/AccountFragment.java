package com.jando.fitness_app;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AccountFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        Button userAccountSettings = v.findViewById(R.id.button_user_account_settings);
        userAccountSettings.setOnClickListener(this);
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
                intent = new Intent(getActivity(), AccountSettingsActivity.class);
                startActivity(intent);
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
