package com.oopcows.trackandtrigger.regandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.IS_NEW_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        binding.usernameField.setText(userAccount.getUsername());
        binding.regAndLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(true/*inputsAreValid()*/) {
                    userAccount = new UserAccount(String.valueOf(binding.usernameField.getText()), userAccount.getGmailId(), userAccount.getPhno(), Profession.nullProfession);
                    uploadAccountToFirebase();
                    Intent dashboardActivity = new Intent(getBaseContext(), DashboardActivity.class);
                    dashboardActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                    dashboardActivity.putExtra(IS_NEW_ACCOUNT_INTENT_KEY, true);
                    startActivity(dashboardActivity);
                    finish();
                }
                else {
                    // inputs are invalid, tell the user in a seski way @subs
                }
            }
        });
    }

    private boolean inputsAreValid() {
        return String.valueOf(binding.usernameField.getText()).length() > 0 &&
               String.valueOf(binding.passwordField.getText()).length() > 0 &&
               String.valueOf(binding.passwordField.getText()).equals(String.valueOf(binding.confirmPasswordField.getText()));
    }

    // @vraj fill this up, procure password from the passwordField
    // userAccount is an instance variable anyway
    private void uploadAccountToFirebase() {

    }
}