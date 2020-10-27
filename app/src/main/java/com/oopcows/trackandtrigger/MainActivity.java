package com.oopcows.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.oopcows.trackandtrigger.regandlogin.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent registerIntent = new Intent(getBaseContext(), RegisterActivity.class);
        this.startActivity(registerIntent);
        finish();
    }
}