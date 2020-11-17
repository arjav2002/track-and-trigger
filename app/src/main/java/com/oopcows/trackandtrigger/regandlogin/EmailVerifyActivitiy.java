package com.oopcows.trackandtrigger.regandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityEmailVerifyBinding;
import com.oopcows.trackandtrigger.helpers.CowConstants;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.*;

public class EmailVerifyActivitiy extends AppCompatActivity {

    private UserAccount userAccount;
    private ActivityEmailVerifyBinding binding;
    private String sentOtp; // @vraj fill this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailVerifyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        sentOtp = "";
        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        binding.gmailIdField.setText(userAccount.getGmailId());


        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = String.valueOf(binding.otpField.getText());
                if(otp.equals(sentOtp)) {
                    userAccount = new UserAccount(userAccount.getUsername(), String.valueOf(binding.gmailIdField.getText()), userAccount.getPhno(), userAccount.getProfession());
                    Intent registerActivity = new Intent(getBaseContext(), RegisterActivity.class);
                    registerActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                    startActivity(registerActivity);
                    finish();
                }
                else binding.statusLabel.setText(R.string.wrong_otp);
            }
        });
        binding.getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp();
                binding.statusLabel.setText(R.string.otp_sent);
            }
        });
    }

    // @vraj, fill the variable sentOtp here itself
    private void sendOtp() {

    }

}