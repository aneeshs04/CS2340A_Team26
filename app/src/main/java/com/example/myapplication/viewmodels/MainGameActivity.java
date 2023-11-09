package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.model.Enemy;
import com.example.myapplication.model.EnemyFactory;
import com.example.myapplication.model.NecromancerDemon;
import com.example.myapplication.model.NecromancerFactory;
import com.example.myapplication.model.ScoreCountdown;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.myapplication.R;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.Wall;
import com.example.myapplication.model.MoveDownStrategy;
import com.example.myapplication.model.MoveLeftStrategy;
import com.example.myapplication.model.MoveRightStrategy;
import com.example.myapplication.model.MoveUpStrategy;
import com.example.myapplication.model.MovementStrategy;
import com.example.myapplication.model.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainGameActivity extends AppCompatActivity implements Observer {
    private TextView countdownTextView;
    private TextView characterNameTextView;

    // player movement variables
    private final Player player = Player.getInstance();
    private PlayerViewModel playerView;
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
    private Handler gameLoopHandler = new Handler();
    private static final int GAME_LOOP_DELAY = 100;
    private List<Enemy> enemies = new ArrayList<>();
    private Timer enemyTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_screen);

        player.registerObserver(this);

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
        playerView = new PlayerViewModel(this, player);
        gameLayout = findViewById(R.id.gameLayout);
        gameLayout.addView(playerView);
        playerView.setVisibility(playerView.VISIBLE);
        characterNameTextView.setVisibility(View.VISIBLE);
        animationCountdown();

        //initializing enemies

        // initializing boundaries of screen
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        maxX = screenWidth - characterNameTextView.getWidth() - 100;
        maxY = screenHeight - characterNameTextView.getHeight() - 450;

        // populating name, difficulty, and health
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewDiff = findViewById(R.id.textViewDifficulty);
        TextView textViewHealth = findViewById(R.id.textViewHealth);

        textViewName.setText(player.getName());
        textViewDiff.setText("Difficulty: " + player.getDifficulty());
        switch (player.getDifficulty()) {
            case "easy":
                player.setHealth(150);
                break;
            case "medium":
                player.setHealth(100);
                break;
            default:
                player.setHealth(50);
                break;
        }
        textViewHealth.setText(String.valueOf(player.getHealth()));

        //enemy movement


        //wall creation for screen 1
        walls.add(new Wall(150, 80, 1050, 150));
        walls.add(new Wall(50, 80, 210, 2800));
        walls.add(new Wall(200, 782, 445, 1750));
        walls.add(new Wall(150, 2280, 1050, 3000));
        walls.add(new Wall(600, 782, 1000, 1750));
        walls.add(new Wall(970, 50, 1500, 450));
        walls.add(new Wall(970, 580, 1500, 1900));
        walls.add(new Wall(828, 2050, 1500, 2800));
        walls.add(new Wall(828, 1600, 1500, 1900));
    }
    @Override
    public void update(float x, float y) {
        characterNameTextView.setX(x - 125);
        characterNameTextView.setY(y - characterNameTextView.getHeight() + 45);
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
                player.setFacingRight(false);
                player.setProposedX(player.getProposedX() - 50);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                strategy = new MoveRightStrategy();
                player.setFacingRight(true);
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
            player.setX(minX);
        } else if (player.getX() > maxX) {
            player.removeObserver(this);
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

//        playerView.updatePosition(player.getX(), player.getY());
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

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

}

