package com.oopcows.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.oopcows.trackandtrigger.databinding.ActivityOtpBinding;
import com.oopcows.trackandtrigger.databinding.ActivityPhNoOtpBinding;
import com.oopcows.trackandtrigger.helpers.UserAccount;

import java.util.concurrent.TimeUnit;

public class PhNoOtpActivity extends AppCompatActivity {

    private static final int TIME_TAKEN_TO_RESEND_OTP=60, OTP_LENGTH=6;
    private static final String COUNTRY_CODE="+91";
    private String verificationCodeBySystem;
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


        userAccount = getIntent().getExtras().getParcelable("com.oopcows.trackandtrigger.helpers.UserAccount");
        binding.getOtpButton.setOnClickListener( (v) -> {
            if(binding.phnoField.getText().toString().isEmpty() || binding.phnoField.getText().toString().length()!=10){
                binding.phnoField.setError("Enter valid phone number");
            } else {
                sendVerificationCodeToUser(COUNTRY_CODE+binding.phnoField.getText().toString());
            }
        });
        binding.regAndLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.otpField.getText().toString();
                if(code.isEmpty() || code.length()<OTP_LENGTH){
                    binding.otpField.setError("Wrong OTP");
                    binding.otpField.requestFocus();
                }
                else {
                    verifyCode(code);
                }
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
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhNoOtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(PhNoOtpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        System.out.println("Success");
                        userAccount = new UserAccount(userAccount.getUsername(), userAccount.getPassword(), userAccount.getGmailId(), String.valueOf(binding.phnoField.getText()), null);
                        Intent dashboardActivity = new Intent(getBaseContext(), DashboardActivity.class);
                        dashboardActivity.putExtra("com.oopcows.trackandtrigger.helpers.UserAccount", userAccount);
                        startActivity(dashboardActivity);
                    } else {
                        Toast.makeText(PhNoOtpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

}
