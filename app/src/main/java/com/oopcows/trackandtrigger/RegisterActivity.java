
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
import com.oopcows.trackandtrigger.helpers.CowConstants;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.*;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signInButton.setOnClickListener(signInListener());
        binding.registerButton.setOnClickListener(registerButtonListener());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, G_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            processGoogleAccount(account, R.string.g_siginin_fail);
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            binding.errorLabel.setText(R.string.g_siginin_fail);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        processGoogleAccount(account, R.string.g_signin_suggestion);
    }

    private View.OnClickListener registerButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(binding.usernameField.getText());
                String password = String.valueOf(binding.passwordField.getText());
                // validate inputs
                goToActivity(new UserAccount(username, password, "", "", Profession.nullProfession), PhNoOtpActivity.class);
            }
        };
    }

    private View.OnClickListener signInListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do firebase/server bullshit and fill up userAccount @vraj/@arjav
                String username = String.valueOf(binding.usernameField.getText());
                String password = String.valueOf(binding.passwordField.getText());
                UserAccount userAccount = null; // fill this with that
                goToActivity(userAccount, DashboardActivity.class);
            }
        };
    }

    private void goToActivity(UserAccount userAccount, Class activity) {
        Intent nextActivity = new Intent(getBaseContext(), activity);
        nextActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
        startActivity(nextActivity);
        finish();
    }

    private void processGoogleAccount(GoogleSignInAccount account, int errorMsg) {
        // @vraj check if account with this gmail account exists
        boolean accountExists = false;
        UserAccount userAccount = null; // fill it up from firebase @vraj
        if(accountExists) {
            goToActivity(userAccount, DashboardActivity.class);
        }
        else {
            binding.errorLabel.setText(errorMsg);
        }
    }
}