package com.oopcows.trackandtrigger.regandlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.HashMap;
import java.util.Map;

import static com.oopcows.trackandtrigger.helpers.CowConstants.USERS_TABLE_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private UserAccount userAccount;
    private String username;
    private String password;
    private String confirmedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        userAccount = getIntent().getExtras().getParcelable(USER_ACCOUNT_INTENT_KEY);
        username = userAccount.getUsername();
        binding.regAndLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputsAreValid()) {
                    userAccount = new UserAccount(username, userAccount.getGmailId(), userAccount.getPhno(), Profession.nullProfession);
                    uploadAccountToFirebase(userAccount);
                    Intent dashboardActivitiy = new Intent(getBaseContext(), DashboardActivity.class);
                    dashboardActivitiy.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                    startActivity(dashboardActivitiy);
                    finish();
                }
            }
        });
    }

    private boolean inputsAreValid() {
        return String.valueOf(binding.usernameField.getText()).length() > 0 &&
               String.valueOf(binding.passwordField.getText()).length() > 0 &&
               String.valueOf(binding.passwordField.getText()).equals(String.valueOf(binding.confirmPasswordField.getText()));
    }

    // @vraj fill this up
    private void uploadAccountToFirebase(UserAccount userAccount) {
        Map<String, Object> v = new HashMap<>();
        v.put("name", userAccount.getUsername());
        v.put("gmailId", userAccount.getGmailId());
        v.put("phoneNumber", userAccount.getPhno());
        v.put("profession", userAccount.getProfession());
        v.put("password", password);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        db.collection(USERS_TABLE_NAME)
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(v)
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    System.out.println("Success");
                })
                .addOnFailureListener((e) -> {
                    System.out.println("Fail");
                });
    }
}