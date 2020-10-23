package com.oopcows.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.oopcows.trackandtrigger.helpers.UserAccount;

public class OtpActivity extends AppCompatActivity {

    // allows the last minute changing of details and sending otp to verify
    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        userAccount = getIntent().getExtras().getParcelable("com.oopcows.trackandtrigger.helpers.UserAccount");
    }
}