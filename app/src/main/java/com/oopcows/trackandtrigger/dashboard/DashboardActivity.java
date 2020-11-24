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

public class DashboardActivity extends AppCompatActivity implements ProfessionChooseFragment.PersonalDetailsFillable {

    private UserAccount userAccount;
    private DatabaseHelper dh;
    private ActivityDashboardBinding binding;
    private View homeMaintenanceButton, kitchenApplianceButton;
    // @subs dashboardActivity should be in a sort of shadow while the dialogue is open
    // so that it is not visible until the profession has been chosen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        dh = DatabaseHelper.getInstance(this);

        View groceryButton = binding.groceryListButton;
        homeMaintenanceButton = binding.mainatenanceListButton;
        kitchenApplianceButton = binding.kitchenAppliancesButton;
        binding.specialButtons.removeAllViews();
        binding.specialButtons.addView(groceryButton);
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(userAccount.getProfession() == Profession.nullProfession) {
            displayDialogue();
        }
        else addAppropriateButtons();
    }

    private void displayDialogue() {
        FragmentManager fm = getSupportFragmentManager();
        ProfessionChooseFragment.newInstance().show(fm, null);
    }

    @Override
    public void fillDetails(Profession profession) {
        userAccount = new UserAccount(userAccount.getUsername(), userAccount.getGmailId(), userAccount.getPhno(), profession);
        dh.updateUser(userAccount);
        addAppropriateButtons();
    }

    private void addAppropriateButtons() {
        if(!userAccount.getProfession().equals(Profession.jobSeeker)) {
            binding.specialButtons.addView(homeMaintenanceButton);
        }
        if(userAccount.getProfession().equals(Profession.homeMaker)) {
            binding.specialButtons.addView(kitchenApplianceButton);
        }
    }
}