package com.oopcows.trackandtrigger.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class DashboardActivity extends AppCompatActivity implements ChooseProfessionFragment.ChooseProfession {

    private UserAccount userAccount;
    private DatabaseHelper dh;
    private ChooseProfessionFragment chooseProfessionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);

        try {
            dh = new DatabaseHelper(getApplicationContext());
            dh.start();
        } catch (DatabaseHelper.DisposedHelperStartedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(userAccount.getProfession() == Profession.nullProfession) {
            displayDialogue();
        }
    }

    private void displayDialogue() {
        FragmentManager fm = getSupportFragmentManager();
        chooseProfessionFragment = ChooseProfessionFragment.newInstance();
        chooseProfessionFragment.show(fm, null);
    }

    @Override
    public void setChosenProfession(Profession profession) {
        userAccount = new UserAccount(userAccount.getUsername(), userAccount.getGmailId(), userAccount.getPhno(), profession);
        updateDatabase();
    }

    private void updateDatabase() {
        try {
            dh.updateUser(userAccount);
        } catch (DatabaseHelper.HelperNotRunningException e) {
            e.printStackTrace();
        }
    }
}