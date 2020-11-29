package com.oopcows.trackandtrigger.regandlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.database.Converters;
import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.HashMap;
import java.util.Map;

import static com.oopcows.trackandtrigger.helpers.CowConstants.GMAILID_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.IS_NEW_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PHNO_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PROF_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USERNAME_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_NAMES;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserAccount userAccount;
    private FirebaseDatabase db;
    private DatabaseReference dr;
    private boolean check = false;
    private String username, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.regAndLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputsAreValid()) {
                    SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);

                    userAccount = new UserAccount(String.valueOf(binding.usernameField.getText()), binding.passwordField.getText().toString(), preferences.getString(PHNO_COLUMN_NAME, ""), Profession.nullProfession);
                    uploadAccountToFirebase();
                    Intent dashboardActivity = new Intent(getBaseContext(), DashboardActivity.class);
                    dashboardActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                    dashboardActivity.putExtra(IS_NEW_ACCOUNT_INTENT_KEY, true);
                    startActivity(dashboardActivity);
                    finish();
                } else {
                    // inputs are invalid, tell the user in a seski way @subs
                }
                check = false;
            }
        });
    }

    private boolean inputsAreValid() {
        return String.valueOf(binding.usernameField.getText()).length() > 0 &&
                String.valueOf(binding.passwordField.getText()).length() > 0;
    }

    // @vraj fill this up, procure password from the passwordField
    // userAccount is an instance variable anyway
    private void uploadAccountToFirebase() {
        Map<String, Object> uploader = new HashMap<>();
        uploader.put(USERNAME_COLUMN_NAME, binding.usernameField.getText().toString());
        db = FirebaseDatabase.getInstance();
        dr = db.getReference().child(USER_NAMES).child(FirebaseAuth.getInstance().getUid());

        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        //saving username and password on database for reference when logging in
        db.collection(PAST_USERS)
                .document(USER_NAMES)
                .set(uploader)
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    System.out.println("Success");
                })
                .addOnFailureListener((e) -> {
                    System.out.println("Fail");
                });
         */
        //saving all new user data on database
        uploader.put(GMAILID_COLUMN_NAME, userAccount.getGmailId());
        uploader.put(PHNO_COLUMN_NAME, userAccount.getPhno());
        uploader.put(PROF_COLUMN_NAME, Converters.fromProfession(Profession.nullProfession));
        SharedPreferences sharedPref = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(USERNAME_COLUMN_NAME, binding.usernameField.getText().toString());
        editor.putString(GMAILID_COLUMN_NAME, userAccount.getGmailId());
        editor.putString(PHNO_COLUMN_NAME, userAccount.getPhno());
        editor.putString(PROF_COLUMN_NAME, Converters.fromProfession(Profession.nullProfession));
        editor.apply();
        dr.setValue(uploader);

       /*db.collection(USERS_TABLE_NAME)
                .document(userAccount.getUsername())
                .set(uploader)
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    System.out.println("Success");
                })
                .addOnFailureListener((e) -> {
                    System.out.println("Fail");
                });*/

    }
}