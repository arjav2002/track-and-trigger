package com.oopcows.trackandtrigger.regandlogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.oopcows.trackandtrigger.dashboard.DashboardActivity;
import com.oopcows.trackandtrigger.databinding.ActivityPhNoOtpBinding;
import com.oopcows.trackandtrigger.helpers.Profession;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.concurrent.TimeUnit;

import static com.oopcows.trackandtrigger.helpers.CowConstants.COUNTRY_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.IS_NEW_ACCOUNT_INTENT_KEY;
import static com.oopcows.trackandtrigger.helpers.CowConstants.PHNO_COLUMN_NAME;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TIME_TAKEN_TO_RESEND_OTP;
import static com.oopcows.trackandtrigger.helpers.CowConstants.USER_ACCOUNT_INTENT_KEY;

public class PhNoOtpActivity extends AppCompatActivity {

    private String verificationCodeBySystem = null;
    private FirebaseAuth firebaseAuth;
    private UserAccount userAccount;
    private ActivityPhNoOtpBinding binding;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhNoOtpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        String phno;
        firebaseAuth = FirebaseAuth.getInstance();

        userAccount = new UserAccount("", "", "", Profession.nullProfession);
        binding.getOtpButton.setOnClickListener((v) -> {
            if (binding.phnoField.getText().toString().isEmpty() || binding.phnoField.getText().toString().length() != 10) {
                binding.phnoField.setError("Enter valid phone number");
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PHNO_COLUMN_NAME, binding.phnoField.getText().toString());
                editor.apply();
                System.out.println(binding.phnoField.getText().toString());
                sendVerificationCodeToUser(COUNTRY_CODE + binding.phnoField.getText().toString());
            }
        });
        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.otpField.getText().toString();
                if (!code.isEmpty()) {
                    verifyCode(code);
                }

                /*
                String code = binding.otpField.getText().toString();
                if(code.isEmpty() || code.length()<OTP_LENGTH){
                    binding.otpField.setError("Wrong OTP");
                    binding.otpField.requestFocus();
                }
                else {
                    verifyCode(code);
                }*/
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

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        binding.otpField.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationCodeBySystem = s;
                }


                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(PhNoOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInUsingPhoneAuthCredential(credential);
    }

    private void signInUsingPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information


                            FirebaseUser user = task.getResult().getUser();
                            checker(task.getResult().getAdditionalUserInfo().isNewUser());

                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    private void checker(boolean c) {
        if (c) {
            Intent registerActivity = new Intent(getBaseContext(), RegisterActivity.class);
            registerActivity.putExtra(USER_ACCOUNT_INTENT_KEY, new UserAccount("", "", "", Profession.nullProfession));
            startActivity(registerActivity);
            finish();
        } else {
            Intent dashboardActivity = new Intent(getBaseContext(), DashboardActivity.class);
            dashboardActivity.putExtra(USER_ACCOUNT_INTENT_KEY, new UserAccount("", "", binding.phnoField.getText().toString(), Profession.nullProfession));
            dashboardActivity.putExtra(IS_NEW_ACCOUNT_INTENT_KEY, false);
            startActivity(dashboardActivity);
            finish();
        }
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(PhNoOtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Success");

                            String p = binding.phnoField.getText().toString();
                            userAccount = new UserAccount(userAccount.getUsername(), "", p, Profession.nullProfession);
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                Intent registerActivity = new Intent(getBaseContext(), RegisterActivity.class);
                                registerActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                                startActivity(registerActivity);
                                finish();
                            } else {
                                Intent dashBoardActivity = new Intent(getBaseContext(), DashboardActivity.class);
                                dashBoardActivity.putExtra(USER_ACCOUNT_INTENT_KEY, userAccount);
                                startActivity(dashBoardActivity);
                                finish();

                            }
                        } else {
                            Toast.makeText(PhNoOtpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

}
