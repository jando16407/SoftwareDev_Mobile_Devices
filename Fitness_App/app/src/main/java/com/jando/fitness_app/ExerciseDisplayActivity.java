package com.jando.fitness_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExerciseDisplayActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ListView listView;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    String exerciseType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_display);

        //Gets exercise type from bundle inside intent that started the activity
        Bundle bundle = getIntent().getExtras();
        exerciseType = bundle.getString("EXERCISE_TYPE");

        //Gets Exercise information from database
        databaseReference = FirebaseDatabase.getInstance().getReference("Exercises")
                .child(exerciseType);
        listView = findViewById(R.id.listViewDisplayExercises);

        populateExercises();

        //list view click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //gets name of the item clicked
                String name = (String) listView.getItemAtPosition(position);

                //adds name of item to bundle, opens activity to display activity
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getApplicationContext(), ExerciseDescriptionActivity.class);
                bundle.putString("EXERCISE_NAME", name);
                bundle.putString("EXERCISE_TYPE", exerciseType);
                intent.putExtras(bundle);
                startActivity(intent);
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

    /** Function to display information from the database **/
    private void populateExercises() {
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                arrayList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);
                TextView tv = view.findViewById(android.R.id.text1);
                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
                // Return the view
                return view;
            }
        };

        listView.setAdapter(arrayAdapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //Gets name of exercise
                String info = dataSnapshot.getKey();
                arrayList.add(info);
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
