package com.oopcows.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.databinding.ActivityOtpBinding;
import com.oopcows.trackandtrigger.databinding.ActivityPhNoOtpBinding;
import com.oopcows.trackandtrigger.helpers.UserAccount;

public class PhNoOtpActivity extends AppCompatActivity {

    private UserAccount userAccount;
    private ActivityPhNoOtpBinding binding;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhNoOtpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable("com.oopcows.trackandtrigger.helpers.UserAccount");
        binding.getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // @vraj send otp to email
                otp = "101"; // initialise
            }
        });
        binding.regAndLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp != null && otp.length() == binding.otpField.getText().length() && otp.contains(binding.otpField.getText())) {
                    userAccount = new UserAccount(userAccount.getUsername(), userAccount.getPassword(), userAccount.getGmailId(), String.valueOf(binding.phnoField.getText()));
                    Intent dashboardActivity = new Intent(getBaseContext(), DashboardActivity.class);
                    dashboardActivity.putExtra("com.oopcows.trackandtrigger.helpers.UserAccount", userAccount);
                    startActivity(dashboardActivity);
                }
                else {
                    System.out.println("wrong phone number otp");
                }
            }
        });
    }
}