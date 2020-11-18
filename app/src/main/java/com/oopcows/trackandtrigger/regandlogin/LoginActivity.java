
package com.oopcows.trackandtrigger.regandlogin;

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
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.databinding.ActivityLoginBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.*;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.registerOrSignInButton.setOnClickListener(registerOrSignInButtonListener());

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
            processGoogleAccount(account);
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            binding.errorLabel.setText(R.string.g_siginin_fail);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            processGoogleAccount(account);
        }
        binding.errorLabel.setText(R.string.g_signin_suggestion);
    }

    private View.OnClickListener registerOrSignInButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(binding.usernameField.getText());
                String password = String.valueOf(binding.passwordField.getText());
                // validate inputs
                boolean accountExists = false;
                UserAccount uc = new UserAccount(username, "", "", Profession.nullProfession); // if account exists then get it @vraj
                processAccount(uc, accountExists);
            }
        };
    }

    private void goToActivity(UserAccount userAccount, Class activity) {
        Intent nextActivity = new Intent(getBaseContext(), activity);
        nextActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
        startActivity(nextActivity);
        finish();
    }

    private void processGoogleAccount(GoogleSignInAccount account) {
        // @vraj check if account with this gmail account exists
        boolean accountExists = false;
        UserAccount userAccount = new UserAccount("", account.getEmail(), "", Profession.nullProfession); // fill it up from firebase @vraj
        processAccount(userAccount, accountExists);
    }

    private void processAccount(UserAccount userAccount, boolean exists) {
        if(exists) {
            goToActivity(userAccount, DashboardActivity.class);
        }
        else {
            goToActivity(userAccount, PhNoOtpActivity.class);
        }
    }
}