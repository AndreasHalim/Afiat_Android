package com.example.afiat;

import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afiat.datastore.StoreManager;
import com.example.afiat.datastore.StoreManagerListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 123;

    @BindView(R.id.email) EditText inEmail;
    @BindView(R.id.password) EditText inPassword;
    @BindView(R.id.btn_login) Button btnLogin;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.sign_in_button) TextView btnSignIn;
    @BindView(R.id.sign_in_google) SignInButton btnSignInGoogle;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setupFirebaseAuth();
        setupClickListener();
    }

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            StoreManagerListener listener = new StoreManagerListener() {
                @Override
                public void onCreatedNewInstance(StoreManager manager) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }
            };
            initStoreManager(mAuth.getCurrentUser(), listener);
        }

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("269015646526-8d4fioshot1s5i7vpth1k6v8vlp958n7.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void initStoreManager(FirebaseUser user, StoreManagerListener listener) {
        StoreManager manager = StoreManager.getInstance(getApplicationContext());
        if (manager == null) {
            StoreManager.newInstance(getApplicationContext(), user.getEmail(), listener);
        } else {
            listener.onCreatedNewInstance(manager);
        }
    }

    private void setupClickListener() {
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inEmail.getText().toString();
                final String password = inPassword.getText().toString();

                if (isLoginValid(email, password)) {
                    progressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private boolean isLoginValid(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_email_empty, Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.error_password_empty, Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }
        return true;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                    Intent intent = new Intent(getApplicationContext(), SampleActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Opps sorry, something is wrong!", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "Error has been occured!", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, getString(R.string.error_aut), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}