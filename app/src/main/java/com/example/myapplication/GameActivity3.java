package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity3 extends AppCompatActivity {
    private int score;
    private TextView countdownTextView;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_game_screen);
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // starting countdown
        countdownTextView = findViewById(R.id.viewScore);
        score = preferences.getInt("score", 0);
        countdownTextView.setText("Score: " + String.valueOf(score));
        startCountdown();

        // populating name and difficulty
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewDiff = findViewById(R.id.textViewDifficulty);
        TextView textViewHealth = findViewById(R.id.textViewHealth);
        ImageView imageViewChar = findViewById(R.id.imageViewCharacter);

        String savedName = preferences.getString("name", "");
        textViewName.setText(savedName);

        String savedDiff = preferences.getString("diff", "");
        textViewDiff.setText("Difficulty: " + savedDiff);

        // populating HP
        if (savedDiff.equals("easy")) {
            textViewHealth.setText("150");
        } else if (savedDiff.equals("medium")) {
            textViewHealth.setText("100");
        } else {
            textViewHealth.setText("50");
        }

        // populating the character icon
        String savedChar = preferences.getString("char", "");
        int imageResource;
        if (savedChar.equals("knight")) {
            imageResource = R.drawable.knight;
        } else if (savedChar.equals("elf")) {
            imageResource = R.drawable.elf;
        } else {
            imageResource = R.drawable.lizard;
        }

        imageViewChar.setImageResource(imageResource);

        // ending the game
        Button endBtn = findViewById(R.id.endButton);
        endBtn.setOnClickListener(v -> {
            editor.putInt("score", score);
            editor.apply();
            Intent end = new Intent(GameActivity3.this, EndActivity.class);
            startActivity(end);
            finish();
        });
    }

    // handles the countdown of the score
    private void startCountdown() {
        handler.postDelayed(() -> {
            // decrease count
            score -= 5;

            // update text
            countdownTextView.setText("Score: " + String.valueOf(score));

            if (score > 0) {
                // repeat countdown
                startCountdown();
            }
        }, 2000); // 2 second delay
    }
}
