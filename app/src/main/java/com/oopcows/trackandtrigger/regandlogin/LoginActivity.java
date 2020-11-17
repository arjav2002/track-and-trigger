
package com.oopcows.trackandtrigger.regandlogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityLoginBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.oopcows.trackandtrigger.helpers.CowConstants.COUNTRY_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.G_SIGN_IN;
import static com.oopcows.trackandtrigger.helpers.CowConstants.OTP_LENGTH;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TIME_TAKEN_TO_RESEND_OTP;

public class LoginActivity extends AppCompatActivity {

    private String verificationCodeBySystem;
    private FirebaseAuth firebaseAuth;
    private ActivityLoginBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    private UserAccount userAccount;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.getOtp.setOnClickListener((v) -> {
            if (binding.phnoField.getText().toString().isEmpty() || binding.phnoField.getText().toString().length() != 10) {
                binding.phnoField.setError("Enter valid phone number");
            } else {
                sendVerificationCodeToUser(COUNTRY_CODE + binding.phnoField.getText().toString());
                phoneNumber = binding.phnoField.getText().toString();
            }
        });
        binding.registerOrSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.otpField.getText().toString();
                if (code.isEmpty() || code.length() < OTP_LENGTH) {
                    binding.otpField.setError("Wrong OTP");
                    binding.otpField.requestFocus();
                } else {
                    verifyCode(code);
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(String.valueOf(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.gSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, G_SIGN_IN);
            }
        });
    }

    private void sendVerificationCodeToUser(String phNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phNo,
                TIME_TAKEN_TO_RESEND_OTP,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Success");
                            FirebaseFirestore fb = FirebaseFirestore.getInstance();
                            boolean isNewUser = false;
                            fb.collection("PastUsers")
                                    .document("PhoneNumbers")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.getResult().contains(phoneNumber)) {
                                                Intent emailVerifyActivity = new Intent(getBaseContext(), EmailVerifyActivitiy.class);
                                                //emailVerifyActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                                                startActivity(emailVerifyActivity);
                                                finish();
                                            }
                                        }
                                    });
                            if (isNewUser) {
                                //userAccount = new UserAccount(userAccount.getUsername(), userAccount.getGmailId(), userAccount.getPhno(), Profession.nullProfession);
                                Intent emailVerifyActivity = new Intent(getBaseContext(), EmailVerifyActivitiy.class);
                                //emailVerifyActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                                startActivity(emailVerifyActivity);
                                finish();
                            } else {
                                Intent dashBoardActivity = new Intent(getBaseContext(), DashboardActivity.class);
                                startActivity(dashBoardActivity);
                                finish();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == G_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                        } else {
                            Toast.makeText(LoginActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            processGoogleAccount(account, R.string.g_siginin_fail);
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.getStatusCode());
            binding.errorLabel.setText(R.string.g_siginin_fail);
        }
    }

    //@Override
    //protected void onStart(){
    //    super.onStart();
    //    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    //    if(account != null) {
    //        processGoogleAccount(account, R.string.g_signin_suggestion);
    //    }
    //}

    private void goToActivity(UserAccount userAccount, Class activity) {
        Intent nextActivity = new Intent(getBaseContext(), activity);
        //nextActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
        startActivity(nextActivity);
        finish();
    }

    private void processGoogleAccount(GoogleSignInAccount account, int errorMsg) {
        // @vraj check if account with this gmail account exists
        boolean accountExists = false;
        UserAccount userAccount = null; // fill it up from firebase @vraj
        if(accountExists) {
            goToActivity(userAccount, DashboardActivity.class);
        }
        else {
            goToActivity(new UserAccount("", account.getEmail(), "", Profession.nullProfession), PhNoOtpActivity.class);
        }
    }


}