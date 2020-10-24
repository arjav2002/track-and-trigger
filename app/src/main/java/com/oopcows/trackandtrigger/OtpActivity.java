package com.oopcows.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.databinding.ActivityOtpBinding;
import com.oopcows.trackandtrigger.helpers.UserAccount;

public class OtpActivity extends AppCompatActivity {

    //@vraj make this work

    // allows the last minute changing of details and sending otp to verify
    private UserAccount userAccount;
    private ActivityOtpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable("com.oopcows.trackandtrigger.helpers.UserAccount");
        binding.usernameField.setText(userAccount.getUsername());
        binding.passwordField.setText(userAccount.getPassword());
        binding.gmailField.setText(userAccount.getGmailId());
        binding.phnoField.setText(userAccount.getPhno());

        // send otp @vraj
    }
}