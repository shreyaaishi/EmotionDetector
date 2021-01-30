package com.example.emotiondetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void returnLaunch(View view) {
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
        // Do something in response to button
    }

}