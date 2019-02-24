package com.example.afiat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeScreen extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;

    private Button btnSensor;
    private Button btnProfile;
    private Button btnSignOut;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnSensor = findViewById(R.id.sensor);
        btnProfile = findViewById(R.id.profile);
        btnSignOut = findViewById(R.id.signout);

        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(HomeScreen.this, LoginActivity.class));
                }
            }
        };

        setClickListener();
    }

    private void setClickListener() {
        btnSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeScreen.this, SampleActivity.class));
                finish();
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, ProfileActivity.class));
                finish();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }
}
