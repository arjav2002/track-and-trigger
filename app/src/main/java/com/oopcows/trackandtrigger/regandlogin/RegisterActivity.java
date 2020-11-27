package com.oopcows.trackandtrigger.regandlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.HashMap;
import java.util.Map;

import static com.oopcows.trackandtrigger.helpers.CowConstants.GMAILID_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PAST_USERS;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PHNO_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PROF_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USERS_TABLE_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_NAMES;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserAccount userAccount;
    private FirebaseDatabase db;
    private DatabaseReference dr;
    private boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        binding.usernameField.setText(userAccount.getUsername());
        binding.regAndLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputsAreValid()) {
                    checkIfUserAlreadyExists();
                    if (!check) {
                        userAccount = new UserAccount(String.valueOf(binding.usernameField.getText()), userAccount.getGmailId(), userAccount.getPhno(), Profession.nullProfession);
                        uploadAccountToFirebase();
                        Intent dashboardActivity = new Intent(getBaseContext(), DashboardActivity.class);
                        dashboardActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                        startActivity(dashboardActivity);
                        finish();
                    } else {
                        //@subs please tell user username already exists and to try another username
                    }
                } else {
                    // inputs are invalid, tell the user in a seski way @subs
                }
                check = false;
            }
        });
    }

    private void valueSaver(Map<String, Object> users) {
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            if (entry.getKey().equals(binding.usernameField.getText().toString()) && (entry.getValue() == null)) {
                check = true;
                break;
            }
        }
    }

    private void checkIfUserAlreadyExists() {
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> query = db.collection(PAST_USERS)
                .document(USER_NAMES)
                .get()
                .getResult()
                .getData(); */
        //return (query.containsKey(binding.usernameField.getText().toString()));
        db = FirebaseDatabase.getInstance();

        dr = db.getReference().child(PAST_USERS).child(USER_NAMES); //.child(binding.usernameField.getText().toString());
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                valueSaver((Map<String, Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("ERROR in retrieving data:" + error.getMessage());
            }
        });

    }

    private boolean inputsAreValid() {
        return String.valueOf(binding.usernameField.getText()).length() > 0 &&
                String.valueOf(binding.passwordField.getText()).length() > 0 &&
                String.valueOf(binding.passwordField.getText()).equals(String.valueOf(binding.confirmPasswordField.getText()));
    }

    // @vraj fill this up, procure password from the passwordField
    // userAccount is an instance variable anyway
    private void uploadAccountToFirebase() {
        Map<String, Object> uploader = new HashMap<>();
        uploader.put(binding.usernameField.getText().toString(), binding.passwordField.getText().toString());
        db = FirebaseDatabase.getInstance();
        dr = db.getReference().child(PAST_USERS).child(USER_NAMES);
        dr.setValue(binding.usernameField.getText().toString(), binding.passwordField.getText().toString());
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
        Profession p = userAccount.getProfession();
        String s = null;
        if (p == Profession.workingProfessional) s = "workingProfessional";
        else if (p == Profession.jobSeeker) s = "jobSeeker";
        else if (p == Profession.bachelor) s = "bachelor";
        else if (p == Profession.homeMaker) s = "homeMaker";
        else if (p == Profession.others) s = "others";
        uploader.put(PROF_COLUMN_NAME, s);
        dr = db.getReference().child(USERS_TABLE_NAME).child(userAccount.getUsername());
        dr.setValue(PROF_COLUMN_NAME, s);
        dr.setValue(GMAILID_COLUMN_NAME, userAccount.getGmailId());
        dr.setValue(PHNO_COLUMN_NAME, userAccount.getPhno());

        /*db.collection(USERS_TABLE_NAME)
                .document(userAccount.getUsername())
                .set(uploader)
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    System.out.println("Success");
                })
                .addOnFailureListener((e) -> {
                    System.out.println("Fail");
                });

         */

        //saving gmail id for reference when logging in
        Map<String, String> u = new HashMap<>();
        dr = db.getReference().child(PAST_USERS).child(GMAILID_COLUMN_NAME);
        dr.setValue(userAccount.getGmailId(), "true");
        /*db.collection(PAST_USERS)
                .document(GMAILID_COLUMN_NAME)
                .set(u);
    } */
    }
}