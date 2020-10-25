package com.oopcows.trackandtrigger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.oopcows.trackandtrigger.helpers.UserAccount;

public class DashboardActivity extends AppCompatActivity {

    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userAccount = getIntent().getExtras().getParcelable("com.oopcows.trackandtrigger.helpers.UserAccount");

        // @subs make UI to select Profession from a drop down (if profession is null)
        // check Profession enum
    }
}