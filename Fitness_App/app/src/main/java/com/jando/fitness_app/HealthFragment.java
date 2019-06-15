package com.jando.fitness_app;

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

public class HealthFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_health, container, false);

        Button exercises = v.findViewById(R.id.buttonExercise);
        exercises.setOnClickListener(this);

        Button nutrition = v.findViewById(R.id.buttonNutrition);
        nutrition.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.buttonExercise:
                Toast.makeText(getContext(),
                        "Exercise Clicked",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), ExerciseActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonNutrition:
                Toast.makeText(getContext(),
                        "Nutrition Clicked",
                        Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), NutritionActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(getContext(),
                        "Default Button Response",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
