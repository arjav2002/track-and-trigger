package com.oopcows.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.databinding.ActivityOtpBinding;
import com.oopcows.trackandtrigger.helpers.UserAccount;

public class EmailOtpActivity extends AppCompatActivity {

    //@vraj make this work

    private UserAccount userAccount;
    private ActivityOtpBinding binding;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable("com.oopcows.trackandtrigger.helpers.UserAccount");
        binding.getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate email
                // send otp @vraj
                otp = "101"; // initialise it
            }
        });
        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp != null && otp.length() == binding.otpField.getText().length() && otp.contains(binding.otpField.getText())) {
                    userAccount = new UserAccount(userAccount.getUsername(), userAccount.getPassword(), String.valueOf(binding.gmailIdField.getText()), "", null);
                    Intent phnoActivity = new Intent(getBaseContext(), PhNoOtpActivity.class);
                    phnoActivity.putExtra("com.oopcows.trackandtrigger.helpers.UserAccount", userAccount);
                    startActivity(phnoActivity);
                }
                else {
                    System.out.println("wrong otp supplied");
                }
            }
        });
    }
}