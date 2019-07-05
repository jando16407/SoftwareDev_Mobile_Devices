package com.jando.fitness_app;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class HealthFragment extends Fragment implements View.OnClickListener {

    /** What happens when fragment is created */
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

    /** Button click handler */
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.buttonExercise:
                intent = new Intent(getActivity(), ExerciseActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonNutrition:
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
