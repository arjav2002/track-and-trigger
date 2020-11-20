package com.oopcows.trackandtrigger;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.oopcows.trackandtrigger.regandlogin.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
        this.startActivity(loginIntent);
        finish();
    }
}