package com.oopcows.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;
import com.oopcows.trackandtrigger.helpers.UserAccount;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.googleSignInButton.setOnClickListener(googleSignInListener());
        binding.registerButton.setOnClickListener(registerButtonListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(gAccount != null) {
            updateUI(gAccount);
        }
    }

    private View.OnClickListener registerButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(binding.usernameField.getText());
                String password = String.valueOf(binding.passwordField.getText());
                String gmail = String.valueOf(binding.gmailField.getText());
                String phno = String.valueOf(binding.phnoField.getText());
                // validate inputs
                goToNext(new UserAccount(username, password, gmail, phno), OtpActivity.class);
            }
        };
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == G_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        GoogleSignInAccount gAccount = null;
        try {
            gAccount = completedTask.getResult(ApiException.class);
        } catch (ApiException e) {
            Log.w("sign-in error", "signInResult:failed code=" + e.getStatusCode());
        } finally {
            updateUI(gAccount);
        }
    }

    private void updateUI(GoogleSignInAccount gAccount) {
        if(gAccount == null) {
            // show in some label that google sign in didn't work
        }
        else {
            // check with firebase if account is a new one
            boolean accountIsNew = true;
            UserAccount userAccount = null;
            Intent nextActivityIntent = null;
            if(accountIsNew) {
                goToNext(new UserAccount("", "", gAccount.getEmail(), ""), OtpActivity.class);
            }
            else {
                // fill up username, password, phno, email using firebase
                String username, password, gmailId, phno;
                username = password = gmailId = phno = null;
                goToNext(new UserAccount(username, password, gmailId, phno), DashboardActivity.class);
            }
        }
    }

    private void goToNext(UserAccount userAccount, Class goToActivity) {
        Intent nextActivityIntent = new Intent(getBaseContext(), goToActivity);
        nextActivityIntent.putExtra("com.oopcows.trackandtrigger.helpers.UserAccount", userAccount);
        startActivity(nextActivityIntent);
        finish();
    }
}