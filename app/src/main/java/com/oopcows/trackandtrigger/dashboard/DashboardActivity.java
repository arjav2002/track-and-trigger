package com.oopcows.trackandtrigger.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import android.os.Bundle;
import android.widget.TextView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.database.AppDatabase;
import com.oopcows.trackandtrigger.database.DatabaseHelper;
import com.oopcows.trackandtrigger.database.UserDao;
import com.oopcows.trackandtrigger.helpers.Profession;
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

        DatabaseHelper dh = new DatabaseHelper();
        dh.start();
        dh.add(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                db.getUserDao().insertUser(userAccount);
                UserAccount uc = db.getUserDao().getUser(userAccount.getUsername());
                assert uc.getUsername().equals(userAccount.getUsername());
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(uc.getUsername() + ", " + userAccount.getUsername());
            }
        });
        dh.add(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                userAccount = new UserAccount("LOLOLOOL", "", "" , Profession.homeMaker);
                db.getUserDao().insertUser(userAccount);
                UserAccount uc = db.getUserDao().getUser(userAccount.getUsername());
                assert uc.getUsername().equals(userAccount.getUsername());
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(uc.getUsername() + ", " + userAccount.getUsername());
            }
        });
    }
}