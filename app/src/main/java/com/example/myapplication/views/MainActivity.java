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

    private static String name;
    private static String difficulty;
    private static String character;
//    private boolean necro1Alive = true;
//    private boolean chort1Alive = true;
//    private boolean necro2Alive = true;
//    private boolean imp2Alive = true;
//    private boolean chort3Alive = true;
//    private boolean big3Alive = true;


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
                Player player = Player.getInstance();
                player.setScore(0);
                player.setDifficulty(difficulty);
                player.setName(name);
                player.setCharacter(character);
                ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 200);
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

//    public boolean isNecro1Alive() {
//        return necro1Alive;
//    }
//
//    public void setNecro1Alive(boolean necro1Alive) {
//        this.necro1Alive = necro1Alive;
//    }
//
//    public boolean isChort1Alive() {
//        return chort1Alive;
//    }
//
//    public void setChort1Alive(boolean chort1Alive) {
//        this.chort1Alive = chort1Alive;
//    }
//
//    public boolean isNecro2Alive() {
//        return necro2Alive;
//    }
//
//    public void setNecro2Alive(boolean necro2Alive) {
//        this.necro2Alive = necro2Alive;
//    }
//
//    public boolean isImp2Alive() {
//        return imp2Alive;
//    }
//
//    public void setImp2Alive(boolean imp2Alive) {
//        this.imp2Alive = imp2Alive;
//    }
//
//    public boolean isChort3Alive() {
//        return chort3Alive;
//    }
//
//    public void setChort3Alive(boolean chort3Alive) {
//        this.chort3Alive = chort3Alive;
//    }
//
//    public boolean isBig3Alive() {
//        return big3Alive;
//    }
//
//    public void setBig3Alive(boolean big3Alive) {
//        this.big3Alive = big3Alive;
//    }
}

