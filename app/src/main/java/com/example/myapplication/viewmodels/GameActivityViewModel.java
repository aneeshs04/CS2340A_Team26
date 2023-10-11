package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.views.MainActivity;

public class GameActivityViewModel extends AppCompatActivity {
    private TextView countdownTextView;
    private Player player = Player.getInstance();
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_screen);

        // start the score countdown
        countdownTextView = findViewById(R.id.viewScore);
        startCountdown();

        // populating name and difficulty
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewDiff = findViewById(R.id.textViewDifficulty);
        TextView textViewHealth = findViewById(R.id.textViewHealth);
        ImageView imageViewChar = findViewById(R.id.imageViewCharacter);

        textViewName.setText(MainActivity.getName());
        textViewDiff.setText("Difficulty: " + MainActivity.getDifficulty());

        // populating HP
        if (MainActivity.getDifficulty().equals("easy")) {
            textViewHealth.setText("150");
        } else if (MainActivity.getDifficulty().equals("medium")) {
            textViewHealth.setText("100");
        } else {
            textViewHealth.setText("50");
        }

        // populating the character icon
        int imageResource;
        if (MainActivity.getCharacter().equals("knight")) {
            imageResource = R.drawable.knight;
        } else if (MainActivity.getCharacter().equals("elf")) {
            imageResource = R.drawable.elf;
        } else {
            imageResource = R.drawable.lizard;
        }

        imageViewChar.setImageResource(imageResource);

        // ending the game
        Button nextBtn = findViewById(R.id.mainNextButton);
        nextBtn.setOnClickListener(v -> {
            Intent end = new Intent(GameActivityViewModel.this, GameActivity2.class);
            startActivity(end);
            finish();
        });

    }

    // handles the countdown of the score
    private void startCountdown() {
        handler.postDelayed(() -> {
            if (player.getScore() > 0) {
                player.setScore(player.getScore() - 5);
                countdownTextView.setText("Score: " + String.valueOf(player.getScore()));
                startCountdown();
            }
        }, 2000); // 2 second delay
    }
}
