package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import com.example.myapplication.model.ScoreCountdown;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.views.PlayerView;

public class MainGameActivity extends AppCompatActivity implements ScoreCountdown.OnTickListener {
    private ScoreCountdown countdown;
    private TextView countdownTextView;
    private TextView characterNameTextView;
    private final Player player = Player.getInstance();
    private PlayerView playerView;
    ConstraintLayout gameLayout;
    private static Boolean stop;
    // Define boundaries
    private final int minX = 0;
    private final int minY = -50;
    private int maxX;
    private int maxY;
    private static int count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_screen);

        // start the score countdown
        countdownTextView = findViewById(R.id.viewScore);
        countdown = new ScoreCountdown(60000, 2000, player.getScore(), this);
        countdown.start();

        // initializing location of player and player name
        characterNameTextView = findViewById(R.id.textViewName);
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() - 25);
        playerView = new PlayerView(this, player.getX(), player.getY());
        gameLayout = findViewById(R.id.gameLayout);
        gameLayout.addView(playerView);
        playerView.setVisibility(playerView.VISIBLE);

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
//        Button nextBtn = findViewById(R.id.mainNextButton);
//        nextBtn.setOnClickListener(v -> {
//            Intent end = new Intent(MainGameActivity.this, SecondGameActivity.class);
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
        if (player.getX() < minX) {
            player.setX(minX);
        } else if (player.getX() > maxX) {
            playerView.setVisibility(playerView.INVISIBLE);
            player.setX(minX + 10);
            Intent end = new Intent(MainGameActivity.this, SecondGameActivity.class);
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

    // allows for other classes to modify the status of the countdown
    public static void setStop(Boolean newStop) {
        stop = newStop;
    }

    public static void setCount(int newCount) {
        count = newCount;
    }

    public static int getCount() {
        return count;
    }
}

