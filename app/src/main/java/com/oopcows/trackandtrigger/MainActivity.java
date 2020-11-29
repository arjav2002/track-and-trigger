package com.oopcows.trackandtrigger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.database.Converters;
import com.oopcows.trackandtrigger.helpers.UserAccount;
import com.oopcows.trackandtrigger.regandlogin.PhNoOtpActivity;

import static com.oopcows.trackandtrigger.helpers.CowConstants.GMAILID_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.IS_NEW_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PHNO_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PROF_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USERNAME_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(getBaseContext(), DashboardActivity.class);
            SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
            UserAccount userAccount = new UserAccount(sharedPreferences.getString(USERNAME_COLUMN_NAME, ""),
                    sharedPreferences.getString(GMAILID_COLUMN_NAME, ""),
                    sharedPreferences.getString(PHNO_COLUMN_NAME, ""),
                    Converters.fromString(sharedPreferences.getString(PROF_COLUMN_NAME, "")));
            intent.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
            intent.putExtra(IS_NEW_ACCOUNT_INTENT_KEY, false);
            startActivity(intent);
            finish();
        } else {
            Intent loginIntent = new Intent(getBaseContext(), PhNoOtpActivity.class);
            this.startActivity(loginIntent);
            finish();
        }
    }
}