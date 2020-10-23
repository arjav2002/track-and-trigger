package com.oopcows.trackandtrigger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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