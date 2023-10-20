package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.graphics.RenderNode;
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
import com.example.myapplication.views.EndActivity;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.views.PlayerView;

public class ThirdGameActivity extends AppCompatActivity implements ScoreCountdown.OnTickListener {
    private TextView countdownTextView;
    private TextView characterNameTextView;
    private final Player player = Player.getInstance();
    private PlayerView playerView;
    ConstraintLayout gameLayout;
    private final int minX = 0;
    private final int minY = -50;
    private int maxX;
    private int maxY;
    private ScoreCountdown countdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_game_screen);

        // starting countdown
        countdownTextView = findViewById(R.id.viewScore);
        player.setScore(player.getScore() + 5);
        countdown = new ScoreCountdown(60000, 2000, player.getScore(), this);
        countdown.start();

        // initializing location of player and player name
        characterNameTextView = findViewById(R.id.textViewName);
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() - 25);
        playerView = new PlayerView(this, player.getX(), player.getY());
        gameLayout = findViewById(R.id.gameLayout3);
        gameLayout.addView(playerView);
        playerView.setVisibility(playerView.VISIBLE);
        characterNameTextView.setVisibility(View.VISIBLE);

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


        // moving to next screen (temp)
//        Button endBtn = findViewById(R.id.endButton);
//        endBtn.setOnClickListener(v -> {
//            MainGameActivity.setStop(true);
//            Intent end = new Intent(ThirdGameActivity.this, EndActivity.class);
//            startActivity(end);
//            finish();
//        });
    }

    public void onScoreUpdate(int updatedScore) {
        if (updatedScore >= 0) {
            player.setScore(updatedScore);
            countdownTextView.setText("Score: " + player.getScore());
        }
    }

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
        if (player.getX() < minX) {
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            player.setX(maxX - 10);
            Intent end = new Intent(ThirdGameActivity.this, SecondGameActivity.class);
            startActivity(end);
            finish();
        } else if (player.getX() > maxX) {
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            player.setX(player.getOrginalX());
            player.setY(player.getOrginalY());
            Intent end = new Intent(ThirdGameActivity.this, EndActivity.class);
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
