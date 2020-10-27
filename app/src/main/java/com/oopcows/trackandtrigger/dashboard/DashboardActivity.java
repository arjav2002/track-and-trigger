package com.oopcows.trackandtrigger.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class DashboardActivity extends AppCompatActivity {

    private UserAccount userAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);

        // @subs make UI to select Profession from a drop down (if profession is nullProfession)
        // check Profession enum
    }
}