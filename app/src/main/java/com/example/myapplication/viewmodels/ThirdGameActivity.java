package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.model.BigDemonFactory;
import com.example.myapplication.model.ChortFactory;
import com.example.myapplication.model.Enemy;
import com.example.myapplication.model.EnemyFactory;
import com.example.myapplication.model.Leaderboard;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.PlayerDecorator;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.model.StarPowerUpDecorator;
import com.example.myapplication.model.TimeCountdown;
import com.example.myapplication.views.EndActivity;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.model.MoveDownStrategy;
import com.example.myapplication.model.MoveLeftStrategy;
import com.example.myapplication.model.MoveRightStrategy;
import com.example.myapplication.model.MoveUpStrategy;
import com.example.myapplication.model.MovementStrategy;
import com.example.myapplication.model.Observer;
import com.example.myapplication.model.Weapon;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.example.myapplication.model.Wall;

public class ThirdGameActivity extends AppCompatActivity implements Observer {
    private TextView countdownTextView;
    private TextView timeTextView;
    private TextView textViewHealth;
    private TextView characterNameTextView;

    //weapon
    private Weapon weapon = Weapon.getInstance();
    private Boolean performWeaponAttack = false;
    private WeaponViewModel swordView;


    // player movement variables
    private final Player player = Player.getInstance();
    private PlayerViewModel playerView;
    private EnemyViewModel chortView;
    private EnemyViewModel bigDemonView;
    private static boolean chortAlive = true;
    private static boolean bigDemonAlive = true;
    ConstraintLayout gameLayout;
    private final int minX = 0;
    private final int minY = -50;
    private int maxX;
    private int maxY;

    // player animation variables
    private static Boolean stop;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static int animationCount = 0;
    private static int weaponAnimationCount = 0;

    private List<Wall> walls = new ArrayList<>();

    // enemy handler variables
    private Handler gameLoopHandler = new Handler();
    private static final int CHORT_LOOP_DELAY = 100;
    private static final int BIG_LOOP_DELAY = 200;
    private List<Enemy> enemies = new ArrayList<>();
    private boolean pause;
    private TimeCountdown timeCountDownTimer;
    private ScoreCountdown scoreCountDownTimer;
    // powerup variables
    private ImageView starPowerUp;
    private int starPowerUpX = 1100;
    private int starPowerUpY = 250;

    public static void setRestart() {
        bigDemonAlive = true;
        chortAlive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_game_screen);
        gameLayout = findViewById(R.id.gameLayout3);

        player.registerObserver(this);
        player.registerObserver(weapon);

        final Button pauseButton = findViewById(R.id.pauseButton3);
        pauseButton.setOnClickListener(v -> {
            pauseGame();
        });

        // start the time countdown
        timeTextView = findViewById(R.id.textViewTime);
        timeTextView.setText("Time Spent: " + player.getTime() + " s");
        timeCountDownTimer = TimeCountdown.getInstance(200000, 1000);
        timeCountDownTimer.setOnTimeChangeListener(newTime -> timeTextView.setText("Time Spent: " + newTime + " s"));

        // starting countdown
        countdownTextView = findViewById(R.id.viewScore);
        countdownTextView.setText("Score: " + player.getScore());
        scoreCountDownTimer = ScoreCountdown.getInstance(50000, 2000);
        scoreCountDownTimer.setOnScoreChangeListener(newScore -> countdownTextView.setText("Score: " + newScore));
        stop = false;

        //chort
        EnemyFactory ChortFactory = new ChortFactory();
        Enemy chort = ChortFactory.createEnemy(835, 200);
        chortView = new EnemyViewModel(this, chort);
        enemies.add(chort);
        player.registerObserver(chort);
        if (chortAlive) {
            gameLayout.addView(chortView);
            chortView.setVisibility(chortView.VISIBLE);
            // start the game loops
            startChortLoop();
        }else {
            chort.setAlive(false);
        }


        //bigDemon
        EnemyFactory BigDemonFactory = new BigDemonFactory();
        Enemy bigDemon = BigDemonFactory.createEnemy(250, 2000);
        bigDemonView = new EnemyViewModel(this, bigDemon);
        enemies.add(bigDemon);
        player.registerObserver(bigDemon);
        if (bigDemonAlive) {
            gameLayout.addView(bigDemonView);
            bigDemonView.setVisibility(bigDemonView.VISIBLE);
            // start the game loops
            startBigDemonLoop();
        } else {
            bigDemon.setAlive(false);
        }


        // initializing location of player and player name
        characterNameTextView = findViewById(R.id.textViewName);
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() - 25);
        playerView = new PlayerViewModel(this, player);
        gameLayout.addView(playerView);
        playerView.setVisibility(playerView.VISIBLE);
        characterNameTextView.setVisibility(View.VISIBLE);
        swordView = new WeaponViewModel(this, weapon);
        gameLayout.addView(swordView);
        swordView.setVisibility(swordView.INVISIBLE);
        updateWeaponAttack();
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

        // loading the star powerup
        if (!player.isStarPowerUpClaimed()) {
            starPowerUp = new ImageView(this);
            starPowerUp.setImageResource(R.drawable.starsprite);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(80, 80);
            layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.leftMargin = starPowerUpX;
            layoutParams.topMargin = starPowerUpY;
            gameLayout.addView(starPowerUp, layoutParams);
        }
    }

    private void startChortLoop() {
        gameLoopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pause) {
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
                    if (chort.contactWithWeapon(weapon) && swordView.getVisibility() == swordView.VISIBLE) {
                        gameLayout.removeView(chortView);
                        if (chortAlive) {
                            player.setScore(player.getScore() + 75);
                        }
                        chort.setAlive(false);
                        chortAlive = false;
                    }
                    // Repeat this runnable code again every GAME_LOOP_DELAY milliseconds
                    gameLoopHandler.postDelayed(this, CHORT_LOOP_DELAY);
                }
            }
        }, CHORT_LOOP_DELAY);
    }

    private void startBigDemonLoop() {
        gameLoopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pause) {
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
                    if (bigDemon.contactWithWeapon(weapon) && swordView.getVisibility() == swordView.VISIBLE) {
                        gameLayout.removeView(bigDemonView);
                        if (bigDemonAlive) {
                            player.setScore(player.getScore() + 25);
                        }
                        bigDemon.setAlive(false);
                        bigDemonAlive = false;
                    }
                    // Repeat this runnable code again every GAME_LOOP_DELAY milliseconds
                    gameLoopHandler.postDelayed(this, BIG_LOOP_DELAY);
                }
            }
        }, BIG_LOOP_DELAY);
    }

    @Override
    public void update(float x, float y, String direction) {
        characterNameTextView.setX(x - 125);
        characterNameTextView.setY(y - characterNameTextView.getHeight() + 45);
//        playerView.updatePosition(x, y);
        playerView.invalidate();
    }

    // handles the animation of the character
    private void animationCountdown() {
        handler.postDelayed(() -> {
            if (!stop && !pause) {
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
            if (!stop && !pause) {
                for (int i = 0; i < enemies.size(); i++) {
                    if (enemies.get(i).contactWithPlayer() && !player.getInvincibility() && enemies.get(i).isAlive()) {
                        player.setInvincibility(true);
                        player.setHealth(player.getHealth() - enemies.get(i).getPower());
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
                            TimeCountdown timeCountDownTimer = TimeCountdown.getInstance(1000000, 1000);
                            timeCountDownTimer.cancel();
                            Intent end = new Intent(ThirdGameActivity.this, EndActivity.class);
                            startActivity(end);
                            finish();
                        }
                        handler.postDelayed(() -> {
                            player.setInvincibility(false);
                        }, 1000);
                    }
                }
                textViewHealth.setText(String.valueOf(player.getHealth()));
                updateHealth();
            }
        }, 100);
    }

    //performs weapon attack in the given direction the player is facing.
    private void updateWeaponAttack() {
        handler.postDelayed(() -> {
            if (!stop && !pause) {
                if (performWeaponAttack) {
                    weapon.setAttackCooldown(true);
                    player.notifyObservers();
                    swordView.setVisibility(swordView.VISIBLE);
                    swordView.updateAnimation(weaponAnimationCount);
                    weaponAnimationCount++;
                    handler.postDelayed(() -> {
                        swordView.updateAnimation(weaponAnimationCount);
                        weaponAnimationCount++;
                    }, 100);
                    handler.postDelayed(() -> {
                        swordView.updateAnimation(weaponAnimationCount);
                        weaponAnimationCount++;
                    }, 150);
                    handler.postDelayed(() -> {
                        swordView.updateAnimation(weaponAnimationCount);
                        weaponAnimationCount++;
                    }, 200);
                    handler.postDelayed(() -> {
                        swordView.updateAnimation(weaponAnimationCount);
                        weaponAnimationCount++;
                    }, 250);
                    handler.postDelayed(() -> {
                        swordView.updateAnimation(weaponAnimationCount);
                        weaponAnimationCount = 0;
                    }, 300);
                    handler.postDelayed(() -> {
                        swordView.updateAnimation(weaponAnimationCount);
                        weaponAnimationCount = 0;
                        swordView.setVisibility(swordView.INVISIBLE);
                    }, 400);

                    //animation stuff goes here for attack
                    //enemy stuff goes here
                    performWeaponAttack = false;
                    handler.postDelayed(() -> {
                        weapon.setAttackCooldown(false);
                    }, weapon.getWeaponAttackDelay());
                }
            }
            updateWeaponAttack();
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
                if (!pause) {
                    strategy = new MoveLeftStrategy();
                    player.setFacingRight(false);
                    weapon.setWeaponSwingDirection("left");
                    player.setPlayerDirection(weapon.getWeaponSwingDirection());
                    player.setProposedX(player.getProposedX() - (float)(50 * player.getSpeedMultiplier()));
                    break;
                }
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (!pause) {
                    strategy = new MoveRightStrategy();
                    player.setFacingRight(true);
                    weapon.setWeaponSwingDirection("right");
                    player.setPlayerDirection(weapon.getWeaponSwingDirection());
                    player.setProposedX(player.getProposedX() + (float)(50 * player.getSpeedMultiplier()));
                    break;
                }
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (!pause) {
                    strategy = new MoveDownStrategy();
                    weapon.setWeaponSwingDirection("down");
                    player.setPlayerDirection(weapon.getWeaponSwingDirection());
                    player.setProposedY(player.getProposedY() + (float)(50 * player.getSpeedMultiplier()));
                    break;
                }
            case KeyEvent.KEYCODE_DPAD_UP:
                if (!pause) {
                    strategy = new MoveUpStrategy();
                    weapon.setWeaponSwingDirection("up");
                    player.setPlayerDirection(weapon.getWeaponSwingDirection());
                    player.setProposedY(player.getProposedY() - (float)(50 * player.getSpeedMultiplier()));
                    break;
                }
            case KeyEvent.KEYCODE_X:
                if (weapon.isAttackCooldown() == false && !pause) {
                    performWeaponAttack = true;
                }
                break;
            case KeyEvent.KEYCODE_P:
                pauseGame();
                break;
        }

        // if no wall collision, update player's position
        player.setMovementStrategy(strategy);
        if (!collidesWithAnyWall((int) player.getProposedX(), (int) player.getProposedY()) && strategy != null) {
            player.performMovement();
            player.notifyObservers();
        }

        // checking for collision with star powerup
        if (Math.abs(player.getX() - starPowerUpX) < 80 && Math.abs(player.getY() - starPowerUpY) < 80 && !player.isStarPowerUpClaimed()) {
            PlayerDecorator playerWithStar = new StarPowerUpDecorator();
            playerWithStar.activateInvincibility(10000);
            player.setStarPowerUpClaimed(true);
            gameLayout.removeView(starPowerUp);
        }


        // checking to see if player is leaving the screen
        if (player.getX() < minX) {
            player.removeObserver(this);
            for (int i = 0; i < enemies.size(); i++) {
                player.removeObserver(enemies.get(i));
            }
            player.removeObserver(weapon);
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(maxX - 10);
            weapon.setX(player.getX());
            Intent end = new Intent(ThirdGameActivity.this, SecondGameActivity.class);
            startActivity(end);
            finish();
        } else if (player.getX() > maxX) {
            player.removeObserver(this);
            for (int i = 0; i < enemies.size(); i++) {
                player.removeObserver(enemies.get(i));
            }
            player.removeObserver(weapon);
            player.setWon(true);
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(player.getOriginalX());
            player.setY(player.getOriginalY());
            ScoreCountdown scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
            scoreCountDownTimer.cancel();
            TimeCountdown timeCountDownTimer = TimeCountdown.getInstance(1000000, 1000);
            timeCountDownTimer.cancel();
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
        if (swordView != null) {
            swordView.invalidate();
        }
        return true;
    }

    public void showPopup() {
        // Anchor popoup with layout to "center" menu
        View pauseScreenView = getLayoutInflater().inflate(R.layout.pause_screen, null);
        PopupWindow pausePopup = new PopupWindow(
                pauseScreenView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
        );
        pausePopup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pausePopup.showAtLocation(gameLayout, Gravity.CENTER, 0, 0);
        Button resumeButton = pauseScreenView.findViewById(R.id.restartButton);
        resumeButton.setOnClickListener(v -> {
            pausePopup.dismiss();
            pauseGame();
        });
        leaderboardInitialization(pauseScreenView);
    }

    private void leaderboardInitialization(View pauseScreenView) {
        TextView scoreRecentView = pauseScreenView.findViewById(R.id.scoreRecentView);
        scoreRecentView.setText(String.valueOf(player.getScore()));
        TextView nameRecentView = pauseScreenView.findViewById(R.id.nameRecentView);
        nameRecentView.setText(MainActivity.getName());
        TextView timeView = pauseScreenView.findViewById(R.id.textViewTime);
        timeView.setText("Time Spent: " + player.getTime() + " seconds");

        Date currentTime = Calendar.getInstance().getTime();
        int month = currentTime.getMonth() + 1;
        int day = currentTime.getDate();
        String date = month + "/" + day;

        TextView dateRecentView = pauseScreenView.findViewById(R.id.dateRecentView);
        dateRecentView.setText(date);


        Leaderboard lb = Leaderboard.getInstance();
        ArrayList<String> nameList = lb.getNameList();
        ArrayList<String> dateList = lb.getDateList();
        ArrayList<Integer> scoreList = lb.getScoreList();

        // modifying leaderboard visual to take in 5 different inputs
        if (scoreList.size() >= 1) {
            TextView leaderboardNameView1 = pauseScreenView.findViewById(R.id.leaderboardNameView1);
            leaderboardNameView1.setText(nameList.get(0));
            TextView leaderboardDateView1 = pauseScreenView.findViewById(R.id.leaderboardDateView1);
            leaderboardDateView1.setText(dateList.get(0));
            TextView leaderboardScoreView1 = pauseScreenView.findViewById(R.id.leaderboardScoreView1);
            leaderboardScoreView1.setText(scoreList.get(0).toString());
        }

        if (scoreList.size() >= 2) {
            TextView leaderboardNameView2 = pauseScreenView.findViewById(R.id.leaderboardNameView2);
            leaderboardNameView2.setText(nameList.get(1));
            TextView leaderboardDateView2 = pauseScreenView.findViewById(R.id.leaderboardDateView2);
            leaderboardDateView2.setText(dateList.get(1));
            TextView leaderboardScoreView2 = pauseScreenView.findViewById(R.id.leaderboardScoreView2);
            leaderboardScoreView2.setText(scoreList.get(1).toString());
        }

        if (scoreList.size() >= 3) {
            TextView leaderboardNameView3 = pauseScreenView.findViewById(R.id.leaderboardNameView3);
            leaderboardNameView3.setText(nameList.get(2));
            TextView leaderboardDateView3 = pauseScreenView.findViewById(R.id.leaderboardDateView3);
            leaderboardDateView3.setText(dateList.get(2));
            TextView leaderboardScoreView3 = pauseScreenView.findViewById(R.id.leaderboardScoreView3);
            leaderboardScoreView3.setText(scoreList.get(2).toString());
        }

        if (scoreList.size() >= 4) {
            TextView leaderboardNameView4 = pauseScreenView.findViewById(R.id.leaderboardNameView4);
            leaderboardNameView4.setText(nameList.get(3));
            TextView leaderboardDateView4 = pauseScreenView.findViewById(R.id.leaderboardDateView4);
            leaderboardDateView4.setText(dateList.get(3));
            TextView leaderboardScoreView4 = pauseScreenView.findViewById(R.id.leaderboardScoreView4);
            leaderboardScoreView4.setText(scoreList.get(3).toString());
        }

        if (scoreList.size() >= 5) {
            TextView leaderboardNameView5 = pauseScreenView.findViewById(R.id.leaderboardNameView5);
            leaderboardNameView5.setText(nameList.get(4));
            TextView leaderboardDateView5 = pauseScreenView.findViewById(R.id.leaderboardDateView5);
            leaderboardDateView5.setText(dateList.get(4));
            TextView leaderboardScoreView5 = pauseScreenView.findViewById(R.id.leaderboardScoreView5);
            leaderboardScoreView5.setText(scoreList.get(4).toString());
        }
    }

    public void pauseGame () {
        if (!pause) {
            pause = true;
            timeCountDownTimer.setPause(true);
            scoreCountDownTimer.setPause(true);
            showPopup();

        } else {
            pause = false;
            updateWeaponAttack();
            animationCountdown();
            updateHealth();
            startChortLoop();
            startBigDemonLoop();
            timeCountDownTimer.setPause(false);
            scoreCountDownTimer.setPause(false);

        }
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
