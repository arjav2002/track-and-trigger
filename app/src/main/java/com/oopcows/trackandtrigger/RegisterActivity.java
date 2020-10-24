package com.oopcows.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;
import com.oopcows.trackandtrigger.helpers.UserAccount;

public class RegisterActivity extends AppCompatActivity {

    private static final int G_SIGN_IN = 1;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.signInButton.setOnClickListener(signInListener());
        binding.registerButton.setOnClickListener(registerButtonListener());
    }

    private View.OnClickListener registerButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = String.valueOf(binding.usernameField.getText());
                String password = String.valueOf(binding.passwordField.getText());
                // validate inputs
                Intent otpActivity = new Intent(getBaseContext(), EmailOtpActivity.class);
                otpActivity.putExtra("com.oopcows.trackandtrigger.helpers.UserAccount", new UserAccount(username, password, "", ""));
                startActivity(otpActivity);
                finish();
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
                Intent dashboardIntent = new Intent(getBaseContext(), DashboardActivity.class);
                dashboardIntent.putExtra("com.oopcows.trackandtrigger.helpers.UserAccount", userAccount);
                startActivity(dashboardIntent);
                finish();
            }
        };
    }
}