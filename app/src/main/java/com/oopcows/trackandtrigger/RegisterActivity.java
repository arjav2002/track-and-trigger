package com.oopcows.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private static final int G_SIGN_IN = 1;
    private ActivityRegisterBinding binding;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.googleSignInButton.setOnClickListener(googleSignInListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
    }

    private View.OnClickListener googleSignInListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, G_SIGN_IN);
            }
        };
    }
}