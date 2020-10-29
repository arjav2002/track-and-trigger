package com.oopcows.trackandtrigger.regandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserAccount userAccount;
    private String username;
    private String password;
    private String confirmedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        username = userAccount.getUsername();
        binding.regAndLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputsAreValid()) {
                    userAccount = new UserAccount(username, userAccount.getGmailId(), userAccount.getPhno(), Profession.nullProfession);
                    Intent dashboardActivitiy = new Intent(getBaseContext(), DashboardActivity.class);
                    dashboardActivitiy.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                    startActivity(dashboardActivitiy);
                    finish();
                }
            }
        });
    }

    private boolean inputsAreValid() {
        return String.valueOf(binding.usernameField.getText()).length() > 0 &&
               String.valueOf(binding.passwordField.getText()).length() > 0 &&
               String.valueOf(binding.passwordField.getText()).equals(String.valueOf(binding.confirmPasswordField.getText()));
    }

    private void uploadAccountToFirebase() {

    }
}