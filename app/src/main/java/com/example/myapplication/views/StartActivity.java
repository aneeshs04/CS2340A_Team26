package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;


public class StartActivity extends AppCompatActivity {
    // opens welcome screen with start button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        Button startBtn = findViewById(R.id.startButton);

        // loads animation of background
        ImageView dungeonImageView = findViewById(R.id.backgroundImageView);
        Glide.with(this).asGif().load(R.drawable.dungeon_background_final).into(dungeonImageView);

        // switches to MainActivity when start button is clicked
        startBtn.setOnClickListener(view -> {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // exits app when exit button is clicked
        Button exitBtn = findViewById(R.id.exitButton);
        exitBtn.setOnClickListener(v -> {
            finish();
            System.exit(0);
        });
    }
}