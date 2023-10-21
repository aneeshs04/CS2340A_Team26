package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.views.PlayerView;

public class SecondGameActivity extends AppCompatActivity {
    private TextView countdownTextView;
    private TextView characterNameTextView;

    // player movement variables
    private final Player player = Player.getInstance();
    private PlayerView playerView;
    ConstraintLayout gameLayout;
    private final int minX = 0; // Left boundary
    private final int minY = -50; // Top boundary
    private int maxX;
    private int maxY;

    // player animation variables
    private static Boolean stop;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static int animationCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_game_screen);

        // starting countdown
        countdownTextView = findViewById(R.id.viewScore);
        countdownTextView.setText("Score: " + player.getScore());
        ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
        scoreCountDownTimer.setOnScoreChangeListener(newScore -> countdownTextView.setText("Score: " + newScore));
        stop = false;

        // initializing location of player and player name
        characterNameTextView = findViewById(R.id.textViewName);
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() - 25);
        playerView = new PlayerView(this, player.getX(), player.getY());
        gameLayout = findViewById(R.id.gameLayout2);
        gameLayout.addView(playerView);
        playerView.setVisibility(playerView.VISIBLE);
        characterNameTextView.setVisibility(View.VISIBLE);
        animationCountdown();

        // initializing boundaries of screen
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        maxX = screenWidth - characterNameTextView.getWidth() - 100;
        maxY = screenHeight - characterNameTextView.getHeight() - 450;

        // populating name, difficulty, and health
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewDiff = findViewById(R.id.textViewDifficulty);
        TextView textViewHealth = findViewById(R.id.textViewHealth);

        textViewName.setText(MainActivity.getName());
        textViewDiff.setText("Difficulty: " + MainActivity.getDifficulty());
        textViewHealth.setText(String.valueOf(player.getHealth()));

    }

    // handles the animation of the player
    private void animationCountdown() {
        handler.postDelayed(() -> {
            if (!stop) {
                playerView.updateAnimation(animationCount % 4);
                animationCount++;
                animationCountdown();
            }
        }, 200);
    }

    // handle key events to move the player and name
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                player.setX(player.getX() - 50);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                player.setX(player.getX() + 50);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                player.setY(player.getY() + 50);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                player.setY(player.getY() - 50);
                break;
        }

        // checking to see if player is leaving the screen
        if (player.getX() < minX) {
            playerView.setVisibility(View.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(maxX - 10);
            Intent end = new Intent(SecondGameActivity.this, MainGameActivity.class);
            startActivity(end);
            finish();
        } else if (player.getX() > maxX) {
            playerView.setVisibility(View.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(minX + 10);
            Intent end = new Intent(SecondGameActivity.this, ThirdGameActivity.class);
            startActivity(end);
            finish();
        }

        if (player.getY() < minY) {
            player.setY(minY);
        } else if (player.getY() > maxY) {
            player.setY(maxY);
        }

        playerView.updatePosition(player.getX(), player.getY());
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() + 45);
        playerView.invalidate();
        return true;
    }
}
