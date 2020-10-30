package com.oopcows.trackandtrigger.regandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityEmailVerifyBinding;
import com.oopcows.trackandtrigger.helpers.CowConstants;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.*;

public class EmailVerifyActivitiy extends AppCompatActivity {

    //@vraj make this work

    private UserAccount userAccount;
    private ActivityEmailVerifyBinding binding;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailVerifyBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailHasBeenVerified()) {
                    userAccount = new UserAccount(userAccount.getUsername(), String.valueOf(binding.gmailIdField.getText()), userAccount.getPhno(), userAccount.getProfession());
                    Intent registerActivity = new Intent(getBaseContext(), DashboardActivity.class);
                    registerActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                    startActivity(registerActivity);
                    finish();
                }
                else {
                    Toast.makeText(EmailVerifyActivitiy.this, "Email not verified yet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // @vraj fill this pls
    private boolean emailHasBeenVerified() {
        return true;
    }
}