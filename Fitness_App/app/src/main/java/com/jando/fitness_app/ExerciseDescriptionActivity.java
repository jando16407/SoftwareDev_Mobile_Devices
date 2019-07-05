package com.jando.fitness_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ExerciseDescriptionActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    TextView textView;
    ImageView imageView;
    String exerciseName, exerciseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_description);

        //Gets exercise type from bundle inside intent that started the activity
        Bundle bundle = getIntent().getExtras();
        exerciseName = bundle.getString("EXERCISE_NAME");
        exerciseType = bundle.getString("EXERCISE_TYPE");

        //Gets Exercise information from database
        databaseReference = FirebaseDatabase.getInstance().getReference("Exercises")
                .child(exerciseType).child(exerciseName);

        setTitle(exerciseName);
        textView = findViewById(R.id.textViewExerciseDesc);

        imageView = findViewById(R.id.imageViewExercises);

        //Gets info about exercise and displays it inside the textView
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String desc = dataSnapshot.child("Description").getValue().toString();
                String diff = "Difficulty: "
                        + dataSnapshot.child("Difficulty").getValue().toString();
                String img = dataSnapshot.child("ImageLink").getValue().toString();

                //sets image to proper diagram
                setImageView(img);

                textView.setText(diff + "\n\n" + desc);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Adds back button
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /** Back button functionality */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    private void setImageView(String img) {
        switch (img){
            case "crunches":
                imageView.setImageResource(R.drawable.crunches_400x400);
                break;
            default:
                break;
        }
    }
}
