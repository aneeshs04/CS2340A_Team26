package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.viewmodels.MainGameActivity;

public class MainActivity extends AppCompatActivity {
    private static String difficulty;
    private static String name;
    private static String character;

    // opens start screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);

        // loading animations of background and character sprites
        ImageView dungeonImageView = findViewById(R.id.dungeonBackground);
        Glide.with(this).asGif().load(R.drawable.dungeon_background_final).into(dungeonImageView);

        ImageView imageViewChar1 = findViewById(R.id.characterSprite1);
        Glide.with(this).asGif().load(R.drawable.knight).into(imageViewChar1);

        ImageView imageViewChar2 = findViewById(R.id.characterSprite2);
        Glide.with(this).asGif().load(R.drawable.elf).into(imageViewChar2);

        ImageView imageViewChar3 = findViewById(R.id.characterSprite3);
        Glide.with(this).asGif().load(R.drawable.lizard).into(imageViewChar3);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.flash_animation);

        // declaring difficulty buttons
        final Button buttonEasy = findViewById(R.id.easyButton);
        final Button buttonMedium = findViewById(R.id.mediumButton);
        final Button buttonHard = findViewById(R.id.hardButton);

        // difficulty buttons listening for input
        buttonEasy.setOnClickListener(v -> {
            buttonEasy.startAnimation(animation);
            buttonMedium.clearAnimation();
            buttonHard.clearAnimation();

            difficulty = "easy";
        });

        buttonMedium.setOnClickListener(v -> {
            buttonEasy.clearAnimation();
            buttonMedium.startAnimation(animation);
            buttonHard.clearAnimation();

            difficulty = "medium";
        });

        buttonHard.setOnClickListener(v -> {
            buttonEasy.clearAnimation();
            buttonMedium.clearAnimation();
            buttonHard.startAnimation(animation);

            difficulty = "hard";
        });

        // character sprites listening for input
        imageViewChar1.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Knight Selected!", Toast.LENGTH_SHORT).show();
            v.startAnimation(animation);
            imageViewChar2.clearAnimation();
            imageViewChar3.clearAnimation();
            character = "knight";
        });

        imageViewChar2.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Elf Selected!", Toast.LENGTH_SHORT).show();
            v.startAnimation(animation);
            imageViewChar1.clearAnimation();
            imageViewChar3.clearAnimation();
            character = "elf";
        });

        imageViewChar3.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Lizard Selected!", Toast.LENGTH_SHORT).show();
            v.startAnimation(animation);
            imageViewChar1.clearAnimation();
            imageViewChar2.clearAnimation();
            character = "lizard";
        });

        // starting game and countdown timer
        Button enterBtn = findViewById(R.id.enterButton);
        enterBtn.setOnClickListener(v -> {
            EditText editTextName = findViewById(R.id.playerNameEditText);
            name = editTextName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a valid name.",
                        Toast.LENGTH_SHORT).show();
            } else if (difficulty == null) {
                Toast.makeText(MainActivity.this, "Please select a difficulty.",
                        Toast.LENGTH_SHORT).show();
            } else if (character == null) {
                Toast.makeText(MainActivity.this, "Please select a character.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Player.getInstance().setScore(105);
                ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
                scoreCountDownTimer.start();
                Intent game = new Intent(MainActivity.this, MainGameActivity.class);
                startActivity(game);
                finish();
            }
        });
    }

    // getters for other classes to access private variables
    public static String getDifficulty() {
        return difficulty;
    }

    public static String getName() {
        return name;
    }

    public static String getCharacter() {
        return character;
    }
}

