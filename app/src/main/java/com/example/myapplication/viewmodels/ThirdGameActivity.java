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
import com.example.myapplication.model.BigDemonFactory;
import com.example.myapplication.model.ChortFactory;
import com.example.myapplication.model.Enemy;
import com.example.myapplication.model.EnemyFactory;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.views.EndActivity;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.model.MoveDownStrategy;
import com.example.myapplication.model.MoveLeftStrategy;
import com.example.myapplication.model.MoveRightStrategy;
import com.example.myapplication.model.MoveUpStrategy;
import com.example.myapplication.model.MovementStrategy;
import com.example.myapplication.model.Observer;

import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.model.Wall;

public class ThirdGameActivity extends AppCompatActivity implements Observer {
    private TextView countdownTextView;
    private TextView textViewHealth;
    private TextView characterNameTextView;

    // player movement variables
    private final Player player = Player.getInstance();
    private PlayerViewModel playerView;
    private EnemyViewModel chortView;
    private EnemyViewModel bigDemonView;
    ConstraintLayout gameLayout;
    private final int minX = 0;
    private final int minY = -50;
    private int maxX;
    private int maxY;

    // player animation variables
    private static Boolean stop;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static int animationCount = 0;
    private List<Wall> walls = new ArrayList<>();
    private Handler gameLoopHandler = new Handler();
    private static final int CHORT_LOOP_DELAY = 100;
    private static final int BIG_LOOP_DELAY = 200;
    private List<Enemy> enemies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_game_screen);
        gameLayout = findViewById(R.id.gameLayout3);

        player.registerObserver(this);

        // starting countdown
        countdownTextView = findViewById(R.id.viewScore);
        countdownTextView.setText("Score: " + player.getScore());
        ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(50000, 2000);
        scoreCountDownTimer.setOnScoreChangeListener(newScore -> countdownTextView.setText("Score: " + newScore));
        stop = false;

        //chort
        EnemyFactory ChortFactory = new ChortFactory();
        Enemy chort = ChortFactory.createEnemy(835, 200);
        chortView = new EnemyViewModel(this, chort);
        gameLayout.addView(chortView);
        chortView.setVisibility(chortView.VISIBLE);
        enemies.add(chort);
        player.registerObserver(chort);


        //chort
        EnemyFactory BigDemonFactory = new BigDemonFactory();
        Enemy bigDemon = BigDemonFactory.createEnemy(250, 2000);
        bigDemonView = new EnemyViewModel(this, bigDemon);
        gameLayout.addView(bigDemonView);
        bigDemonView.setVisibility(bigDemonView.VISIBLE);
        enemies.add(bigDemon);
        player.registerObserver(bigDemon);


        // start the game loops
        startChortLoop();
        startBigDemonLoop();

        // initializing location of player and player name
        characterNameTextView = findViewById(R.id.textViewName);
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() - 25);
        playerView = new PlayerViewModel(this, player);
        gameLayout.addView(playerView);
        playerView.setVisibility(playerView.VISIBLE);
        characterNameTextView.setVisibility(View.VISIBLE);
        animationCountdown();
        updateHealth();


        // initializing boundaries of screen
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        maxX = screenWidth - characterNameTextView.getWidth() - 100;
        maxY = screenHeight - characterNameTextView.getHeight() - 450;

        // populating name, difficulty, and health
        TextView textViewName = findViewById(R.id.textViewName);
        TextView textViewDiff = findViewById(R.id.textViewDifficulty);
        textViewHealth = findViewById(R.id.textViewHealth);

        textViewName.setText(MainActivity.getName());
        textViewDiff.setText("Difficulty: " + MainActivity.getDifficulty());
        textViewHealth.setText(String.valueOf(player.getHealth()));

        //walls for screen 3
        walls.add(new Wall(50, 80, 1300, 150));
        walls.add(new Wall(1130, 80, 1440, 900));
        walls.add(new Wall(950, 782, 1300, 1730));
        walls.add(new Wall(50, 782, 750, 1730));
        walls.add(new Wall(0, 0, 410, 400));
        walls.add(new Wall(0, 600, 400, 1730));
        walls.add(new Wall(150, 2340, 1400, 3000));
        walls.add(new Wall(0, 1730, 220, 3000));
        walls.add(new Wall(1250, 1730, 1440, 2100));
        walls.add(new Wall(150, 2420, 1600, 3000));
        walls.add(new Wall(1250, 2320, 1440, 3000));
        walls.add(new Wall(1250, 2270, 1440, 4000));
    }

    private void startChortLoop() {
        gameLoopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Enemy chort = enemies.get(0);
                // Move each enemy
                if (chort.getDirection().equals("up")) {
                    if (!collidesWithAnyWall((int) chort.getX(), (int) chort.getY() + 50)) {
                        chort.move();
                        if (chort.getY() >= maxY) {
                            chort.changeDirection("down");
                        }
                        chortView.invalidate();
                    } else {
                        chort.changeDirection("down");
                    }
                } else {
                    if (!collidesWithAnyWall((int) chort.getX(), (int) chort.getY() - 50)) {
                        chort.move();
                        if (chort.getX() <= minX) {
                            chort.changeDirection("up");
                        }
                        chortView.invalidate();
                    } else {
                        chort.changeDirection("up");
                    }
                }
                // Repeat this runnable code again every GAME_LOOP_DELAY milliseconds
                gameLoopHandler.postDelayed(this, CHORT_LOOP_DELAY);
            }
        }, CHORT_LOOP_DELAY);
    }

    private void startBigDemonLoop() {
        gameLoopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Enemy bigDemon = enemies.get(1);
                if (bigDemon.getDirection().equals("up")) {
                    if (!collidesWithAnyWall((int) bigDemon.getX(), (int) bigDemon.getY() - 50)) {
                        bigDemon.move();
                        if (bigDemon.getY() <= minY) {
                            bigDemon.changeDirection("right");
                        }
                        bigDemonView.invalidate();
                    } else {
                        bigDemon.changeDirection("right");
                    }
                } else if (bigDemon.getDirection().equals("right")) {
                    if (!collidesWithAnyWall((int) bigDemon.getX() + 125, (int) bigDemon.getY())) {
                        bigDemon.move();
                        if (bigDemon.getX() >= maxX) {
                            bigDemon.changeDirection("down");
                        }
                        bigDemonView.invalidate();
                    } else {
                        bigDemon.changeDirection("down");
                    }
                } else if (bigDemon.getDirection().equals("down")) {
                    if (!collidesWithAnyWall((int) bigDemon.getX(), (int) bigDemon.getY() + 100)) {
                        bigDemon.move();
                        if (bigDemon.getY() >= maxY) {
                            bigDemon.changeDirection("left");
                        }
                        bigDemonView.invalidate();
                    } else {
                        bigDemon.changeDirection("left");
                    }
                } else if (bigDemon.getDirection().equals("left")) {
                    if (!collidesWithAnyWall((int) bigDemon.getX() - 50, (int) bigDemon.getY())) {
                        bigDemon.move();
                        if (bigDemon.getX() <= minX) {
                            bigDemon.changeDirection("up");
                        }
                        bigDemonView.invalidate();
                    } else {
                        bigDemon.changeDirection("up");
                    }
                }
                // Repeat this runnable code again every GAME_LOOP_DELAY milliseconds
                gameLoopHandler.postDelayed(this, BIG_LOOP_DELAY);
            }
        }, BIG_LOOP_DELAY);
    }

    @Override
    public void update(float x, float y) {
        characterNameTextView.setX(x - 125);
        characterNameTextView.setY(y - characterNameTextView.getHeight() + 45);
//        playerView.updatePosition(x, y);
        playerView.invalidate();
    }

    // handles the animation of the character
    private void animationCountdown() {
        handler.postDelayed(() -> {
            if (!stop) {
                playerView.updateAnimation(animationCount % 4, player.getMovement());
                bigDemonView.updateAnimation(animationCount % 4);
                chortView.updateAnimation(animationCount % 4);
                animationCount++;
                animationCountdown();
            }
        }, 200);
    }

    // updates the health of the player
    private void updateHealth() {
        handler.postDelayed(() -> {
            if (!stop) {
                if (enemies.get(0).contactWithPlayer() && !player.getInvincibility()) {
                    player.setInvincibility(true);
                    player.setHealth(player.getHealth() - enemies.get(0).getPower());
                    if (player.getHealth() <= 0) {
                        player.removeObserver(this);
                        player.setWon(false);
                        playerView.setVisibility(playerView.INVISIBLE);
                        characterNameTextView.setVisibility(View.INVISIBLE);
                        stop = true;
                        player.setX(player.getOriginalX());
                        player.setY(player.getOriginalY());
                        ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
                        scoreCountDownTimer.cancel();
                        Intent end = new Intent(ThirdGameActivity.this, EndActivity.class);
                        startActivity(end);
                        finish();
                    }
                    handler.postDelayed(() -> {
                        player.setInvincibility(false);
                    }, 1000);
                } else if (enemies.get(1).contactWithPlayer() && !player.getInvincibility()) {
                    player.setInvincibility(true);
                    player.setHealth(player.getHealth() - enemies.get(1).getPower());
                    if (player.getHealth() <= 0) {
                        player.removeObserver(this);
                        player.setWon(false);
                        playerView.setVisibility(playerView.INVISIBLE);
                        characterNameTextView.setVisibility(View.INVISIBLE);
                        stop = true;
                        player.setX(player.getOriginalX());
                        player.setY(player.getOriginalY());
                        ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
                        scoreCountDownTimer.cancel();
                        Intent end = new Intent(ThirdGameActivity.this, EndActivity.class);
                        startActivity(end);
                        finish();
                    }
                    handler.postDelayed(() -> {
                        player.setInvincibility(false);
                    }, 1000);
                }
                textViewHealth.setText(String.valueOf(player.getHealth()));
                updateHealth();
            }
        }, 100);
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
                player.setFacingRight(false);;
                player.setProposedX(player.getProposedX() - 50);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                strategy = new MoveRightStrategy();
                player.setFacingRight(true);;
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
            player.notifyObservers();
        }

        // checking to see if player is leaving the screen
        if (player.getX() < minX) {
            player.removeObserver(this);
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(maxX - 10);
            Intent end = new Intent(ThirdGameActivity.this, SecondGameActivity.class);
            startActivity(end);
            finish();
        } else if (player.getX() > maxX) {
            player.removeObserver(this);
            player.setWon(true);
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(player.getOriginalX());
            player.setY(player.getOriginalY());
            ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
            scoreCountDownTimer.cancel();
            Intent end = new Intent(ThirdGameActivity.this, EndActivity.class);
            startActivity(end);
            finish();
        }

        if (player.getY() < minY) {
            player.setY(minY);
        } else if (player.getY() > maxY) {
            player.setY(maxY);
        }

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
