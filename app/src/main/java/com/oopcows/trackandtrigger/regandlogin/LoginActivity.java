
package com.oopcows.trackandtrigger.regandlogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityLoginBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.oopcows.trackandtrigger.helpers.CowConstants.GMAILID_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.G_SIGN_IN;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PAST_USERS;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PHNO_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PROF_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USERS_TABLE_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_NAMES;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private String username, password, gmail;
    private FirebaseDatabase db;
    private DatabaseReference dr;
    private boolean check = false;
    private Map<String, Object> stringObjectMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.registerOrSignInButton.setOnClickListener(registerOrSignInButtonListener());

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, G_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == G_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            processGoogleAccount(account);
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            binding.errorLabel.setText(R.string.g_siginin_fail);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            processGoogleAccount(account);
        }
        binding.errorLabel.setText(R.string.g_signin_suggestion);
    }

    private void importFromFirebase(Map<String, Object> snapshot) {
        for (Map.Entry<String, Object> entry : snapshot.entrySet()) {
            stringObjectMap.put(entry.getKey(), entry.getValue().toString());
        }
    }

    private View.OnClickListener registerOrSignInButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = String.valueOf(binding.usernameField.getText());
                password = String.valueOf(binding.passwordField.getText());


                // validate inputs
                checkIfUserExists();
                System.out.println("Account new");
                UserAccount uc = new UserAccount(username, "", "", Profession.nullProfession); // if account exists then get it @vraj
                if (check) {
                    //FirebaseFirestore db = FirebaseFirestore.getInstance();
                    /*Task<DocumentSnapshot> task = db.collection(USERS_TABLE_NAME)
                            .document(username)
                            .get();
                    Map<String, Object> stringObjectMap = task.getResult().getData();*/
                    db = FirebaseDatabase.getInstance();
                    dr = db.getReference().child(USERS_TABLE_NAME);
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            importFromFirebase((Map<String, Object>) snapshot.getValue());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            System.out.println("ERROR in retrieving data:" + error.getMessage());
                        }
                    });

                    String prof = stringObjectMap.get(PROF_COLUMN_NAME).toString();
                    Profession p = Profession.nullProfession;
                    if (prof == "workingProfessional") p = Profession.workingProfessional;
                    if (prof == "jobSeeker") p = Profession.jobSeeker;
                    if (prof == "bachelor") p = Profession.bachelor;
                    if (prof == "homeMaker") p = Profession.homeMaker;
                    if (prof == "others") p = Profession.others;
                    uc = new UserAccount(username,
                            stringObjectMap.get(GMAILID_COLUMN_NAME).toString(),
                            stringObjectMap.get(PHNO_COLUMN_NAME).toString(),
                            p);

                    processAccount(uc, check);
                }
            }
        };
    }

    private void valueSaver(Map<String, Object> snapshot) {
        check = false;
        for (Map.Entry<String, Object> entry : snapshot.entrySet()) {
            if (entry.getKey().equals(username) && entry.getValue().equals(password)) {
                check = true;
                break;
            }
        }
    }

    private void checkIfUserExists() {
        db = FirebaseDatabase.getInstance();
        dr = db.getReference().child(PAST_USERS).child(USER_NAMES);
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

        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*Map<String, Object> snapshot = new HashMap<>();
        db.collection(PAST_USERS)
                .document(USER_NAMES)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            snapshot = documentSnapshot.getData();
                        } else {
                            System.out.println("Error:List Empty");
                            return;
                        }
                    }
                })*/

        /*for (Map.Entry<String, Object> entry : snapshot.entrySet()) {
            if (entry.getKey().equals(username) && entry.getValue().equals(password))
                return true;
        }*/


    }

    private void goToActivity(UserAccount userAccount, Class activity) {
        Intent nextActivity = new Intent(getBaseContext(), activity);
        nextActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
        startActivity(nextActivity);
        finish();
    }

    private void checkIfGmailAccountExists() {
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*Map<String, Object> checker = db.collection(PAST_USERS)
                .document(GMAILID_COLUMN_NAME)
                .get()
                .getResult()
                .getData();
        return checker.containsKey(gmail);*/
        db = FirebaseDatabase.getInstance();
        dr = db.getReference().child(USERS_TABLE_NAME);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gmailChecker((Map<String, Object>) snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("ERROR in retrieving data:" + error.getMessage());
            }
        });


    }

    private void gmailChecker(Map<String, Object> snapshot) {
        check = false;
        ArrayList<String> mails = new ArrayList<>();
        for (Map.Entry<String, Object> entry : snapshot.entrySet()) {
            Map s = (Map) entry.getValue();
            mails.add(s.get(GMAILID_COLUMN_NAME).toString());
        }
        for (int i = 0; i < gmail.length(); i++) {
            if (gmail.charAt(i) == '@') {
                gmail.substring(0, i);
                break;
            }
        }
        for (String listIterator : mails) {
            if (listIterator.equals(gmail)) {
                check = true;
                return;
            }
        }

    }

    private void processGoogleAccount(GoogleSignInAccount account) {
        // @vraj check if account with this gmail account exists
        gmail = account.getEmail();
        checkIfGmailAccountExists();
        UserAccount userAccount = new UserAccount("", gmail, "", Profession.nullProfession); // fill it up from firebase @vraj
        processAccount(userAccount, check);
    }

    private void processAccount(UserAccount userAccount, boolean exists) {
        if (exists) {
            goToActivity(userAccount, DashboardActivity.class);
        }
        else {
            goToActivity(userAccount, PhNoOtpActivity.class);
        }
    }
}