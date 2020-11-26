package com.oopcows.trackandtrigger.regandlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.databinding.ActivityEmailVerifyBinding;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.HashMap;
import java.util.Map;

import static com.oopcows.trackandtrigger.helpers.CowConstants.MAIL;
import static com.oopcows.trackandtrigger.helpers.CowConstants.OTP;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

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
        sentOtp = String.valueOf((int) (Math.random() * 9)) + String.valueOf((int) (Math.random() * 9)) + String.valueOf((int) (Math.random() * 9)) + String.valueOf((int) (Math.random() * 9)) + String.valueOf((int) (Math.random() * 9)) + String.valueOf((int) (Math.random() * 9));
        ;
        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        binding.gmailIdField.setText(userAccount.getGmailId());

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = String.valueOf(binding.otpField.getText());
                if (otp.equals(sentOtp)) {
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
        System.out.println(sentOtp);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, String> otpField = new HashMap<>();
        otpField.put(binding.gmailIdField.getText().toString(), sentOtp);
        db.collection(OTP)
                .document(MAIL)
                .set(otpField);
    }

}