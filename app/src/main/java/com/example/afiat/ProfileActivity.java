package com.example.afiat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        for (UserInfo profile : user.getProviderData()) {
            // Email
            String email_address = profile.getEmail();
            TextView email = findViewById(R.id.email);
            email.setText(email_address);
        }

        // Back button pressed
        Button back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, HomeScreen.class));
                finish();
            }
        });

        // Save Change button pressed
        Button saveChange = findViewById(R.id.submitBtn);
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

        // Delete account button pressed
        Button deleteAcc = findViewById(R.id.deleteBtn);
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    public void updatePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /* Authentication Section */
        TextView email = findViewById(R.id.email);
        final String email_address = email.getText().toString();

        TextView oldPassword = findViewById(R.id.oldPassword);
        final String oldPass = oldPassword.getText().toString();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email_address, oldPass);

        // Prompt the user to re-provide their sign-in credentials
        Task<Void> voidTask = user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // [START update_password]
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            TextView newPassword = findViewById(R.id.newPassword);
                            final String newPass = newPassword.getText().toString();

                            user.updatePassword(newPass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Snackbar.make(findViewById(R.id.oldPassword),"Password changed", Snackbar.LENGTH_LONG).show();
                                                Log.d(TAG, "Password changed successfully");
                                            } else {
                                                Snackbar.make(findViewById(R.id.oldPassword),"Failed to change password", Snackbar.LENGTH_LONG).show();
                                                Log.d(TAG, "Password failed to change");
                                            }
                                        }
                                    });
                            // [END update_password]
                        } else {
                            Snackbar.make(findViewById(R.id.oldPassword),"Your current password is incorrect!", Snackbar.LENGTH_LONG).show();
                            Log.d(TAG, "Authentication failed!");
                        }
                    }
                });

        // [END reauthenticate]
    }

    public void deleteUser() {
        // [START delete_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });
        // [END delete_user]
    }
}
