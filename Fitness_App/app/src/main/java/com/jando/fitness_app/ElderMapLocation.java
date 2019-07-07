package com.jando.fitness_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ElderMapLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private double ElderLatitude;
    private double ElderLongitude;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_map_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = database.getReference("Location");

        //Gets Google info so it can log out
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("434381172772-nab3ojvfhn78s3s6en73mdbmg9pk30ak.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(ElderMapLocation.this, gso);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    ElderLatitude = (double) dataSnapshot.child(user.getUid()).child("Latitude").getValue();
                    ElderLongitude = (double) dataSnapshot.child(user.getUid()).child("Longitude").getValue();

                    onMapReady(mMap);
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.clear();
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(ElderLatitude, ElderLongitude);
        mMap.addMarker(new MarkerOptions().position(sydney).title("This is Elder Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 19.0f));
    }

    protected void onDestroy() {
        super.onDestroy();
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();
        finish();
    }

    protected void onPause(){
        super.onPause();
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();
        finish();
    }

    protected void onStop() {
        super.onStop();
        mGoogleSignInClient.signOut();
        firebaseAuth.signOut();
        finish();
    }
}
