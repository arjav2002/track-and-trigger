package com.oopcows.trackandtrigger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.oopcows.trackandtrigger.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}