package com.oopcows.trackandtrigger.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.oopcows.trackandtrigger.database.DatabaseHelper;
import com.oopcows.trackandtrigger.databinding.ActivityDashboardBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class DashboardActivity extends AppCompatActivity implements PersonalDetailsFragment.PersonalDetailsFillable {

    private UserAccount userAccount;
    private DatabaseHelper dh;
    private PersonalDetailsFragment personalDetailsFragment;
    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);

        dh = DatabaseHelper.getInstance(this);
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
        personalDetailsFragment = PersonalDetailsFragment.newInstance();
        personalDetailsFragment.show(fm, null);
    }

    @Override
    public void fillDetails(String username, Profession profession) {
        userAccount = new UserAccount(username, userAccount.getGmailId(), userAccount.getPhno(), profession);
        dh.updateUser(userAccount);
    }
}