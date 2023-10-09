package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        TextView scoreView = findViewById(R.id.scoreView);
        int score = preferences.getInt("score", 0);
        scoreView.setText("Score: " + score);
    }
}
