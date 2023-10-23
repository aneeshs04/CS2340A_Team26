package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.views.EndActivity;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.views.MoveDownStrategy;
import com.example.myapplication.views.MoveLeftStrategy;
import com.example.myapplication.views.MoveRightStrategy;
import com.example.myapplication.views.MoveUpStrategy;
import com.example.myapplication.views.MovementStrategy;
import com.example.myapplication.views.Observer;
import com.example.myapplication.views.PlayerView;
import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.model.Wall;


public class SecondGameActivity extends AppCompatActivity implements Observer {
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
    private List<Wall> walls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_game_screen);

        player.registerObserver(this);

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

        // create walls for second screen
        walls.add(new Wall(150, 80, 1350, 150));
        walls.add(new Wall(0, 0, 150, 400));
        walls.add(new Wall(1100, 0, 1440, 400));
        walls.add(new Wall(1100, 582, 1440, 3500));
        walls.add(new Wall(0, 582, 150, 1862));
        walls.add(new Wall(150, 2280, 1050, 3000));
        walls.add(new Wall(0, 2062, 150, 2500));
        walls.add(new Wall(150, 990, 480, 1550));
        walls.add(new Wall(800, 990, 1480, 1550));




    }

    @Override
    public void update(float x, float y) {
        characterNameTextView.setX(x - 125);
        characterNameTextView.setY(y - characterNameTextView.getHeight() + 45);
        playerView.updatePosition(x, y);
        playerView.invalidate();
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
        MovementStrategy strategy = null;
        player.setProposedX(player.getX());
        player.setProposedY(player.getY());

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                strategy = new MoveLeftStrategy();
                playerView.setCharacterDirection(false);
                player.setProposedX(player.getProposedX() - 50);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                strategy = new MoveRightStrategy();
                playerView.setCharacterDirection(true);
                player.setProposedX(player.getProposedX() + 50);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                strategy = new MoveDownStrategy();
                player.setProposedY(player.getProposedY() + 50);
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                strategy = new MoveUpStrategy();
                player.setProposedY(player.getProposedY() - 50);
                break;
        }

        // if no wall collision, update player's position
        player.setMovementStrategy(strategy);
        if (!collidesWithAnyWall((int) player.getProposedX(), (int) player.getProposedY()) && strategy != null) {
            player.performMovement();
        }

        // checking to see if player is leaving the screen
        if (player.getX() < minX) {
            player.removeObserver(this);
            playerView.setVisibility(View.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(maxX - 10);
            Intent end = new Intent(SecondGameActivity.this, MainGameActivity.class);
            startActivity(end);
            finish();
        } else if (player.getX() > maxX) {
            player.removeObserver(this);
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

    boolean collidesWithAnyWall(int x, int y) {
        for (Wall wall : walls) {
            if (wall.collidesWith(x, y)) {
                return true;
            }
        }
        return false;
    }
}
