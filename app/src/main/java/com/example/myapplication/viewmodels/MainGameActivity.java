package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.example.myapplication.model.ScoreCountdown;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.Wall;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.views.PlayerView;
import java.util.ArrayList;
import java.util.List;


public class
MainGameActivity extends AppCompatActivity {
    private TextView countdownTextView;
    private TextView characterNameTextView;

    // player movement variables
    private final Player player = Player.getInstance();
    private PlayerView playerView;
    ConstraintLayout gameLayout;
    private final int minX = 0;
    private final int minY = -50;
    private int maxX;
    private int maxY;

    // player animation variables
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static Boolean stop;
    private static int animationCount = 0;
    private List<Wall> walls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_screen);

        // start the score countdown
        countdownTextView = findViewById(R.id.viewScore);
        countdownTextView.setText("Score: " + player.getScore());
        ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
        scoreCountDownTimer.setOnScoreChangeListener(newScore -> countdownTextView.setText("Score: " + newScore));
        stop = false;

        // initializing location of player and player name + starting animation
        characterNameTextView = findViewById(R.id.textViewName);
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() - 25);
        playerView = new PlayerView(this, player.getX(), player.getY());
        gameLayout = findViewById(R.id.gameLayout);
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

        //wall creation for screen 1
        walls.add(new Wall(150, 80, 1050, 150));
        walls.add(new Wall(50, 80, 210, 2800));
        walls.add(new Wall(200, 782, 445, 1750));
        walls.add(new Wall(150, 2280, 1050, 3000));
        walls.add(new Wall(600, 782, 1000, 1750));
        walls.add(new Wall(970, 50, 1500, 450));
        walls.add(new Wall(970, 580, 1500, 1900));
        walls.add(new Wall(850, 2100, 1500, 2800));
        walls.add(new Wall(850, 1600, 1500, 1900));

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
        float proposedX = player.getX();
        float proposedY = player.getY();

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                proposedX -= 50;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                proposedX += 50;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                proposedY += 50;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                proposedY -= 50;
                break;
        }

        // If no wall collision, update player's position
        if (!collidesWithAnyWall((int) proposedX, (int) proposedY)) {
            player.setX(proposedX);
            player.setY(proposedY);
        }

        // Now, check for boundary conditions
        if (player.getX() < minX) {
            player.setX(minX);
        } else if (player.getX() > maxX) {
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
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

    boolean collidesWithAnyWall(int x, int y) {
        for (Wall wall : walls) {
            if (wall.collidesWith(x, y)) {
                return true;
            }
        }
        return false;
    }
}

