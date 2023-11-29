package com.example.myapplication.viewmodels;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.model.ChortFactory;
import com.example.myapplication.model.Enemy;
import com.example.myapplication.model.EnemyFactory;
import com.example.myapplication.model.Leaderboard;
import com.example.myapplication.model.MoveDownStrategy;
import com.example.myapplication.model.MoveLeftStrategy;
import com.example.myapplication.model.MoveRightStrategy;
import com.example.myapplication.model.MoveUpStrategy;
import com.example.myapplication.model.MovementStrategy;
import com.example.myapplication.model.NecromancerFactory;
import com.example.myapplication.model.Observer;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.model.TimeCountdown;
import com.example.myapplication.model.Wall;
import com.example.myapplication.model.Weapon;
import com.example.myapplication.views.EndActivity;
import com.example.myapplication.views.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainGameActivity extends AppCompatActivity implements Observer {
    private TextView textViewHealth;
    private TextView timeTextView;
    private TextView countdownTextView;
    private TextView characterNameTextView;

    //weapon
    private Weapon weapon = Weapon.getInstance();
    private Boolean performWeaponAttack = false;
    private WeaponViewModel swordView;

    // player movement variables
    private final Player player = Player.getInstance();
    private PlayerViewModel playerView;
    private EnemyViewModel necroView;
    private EnemyViewModel chortView;
    private static boolean necroAlive = true;
    private static boolean chortAlive = true;

    ConstraintLayout gameLayout;
    private final int minX = 0;
    private final int minY = -50;
    private int maxX;
    private int maxY;

    // player animation variables
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static Boolean stop;
    private static int animationCount = 0;
    private static int weaponAnimationCount = 0;
    private List<Wall> walls = new ArrayList<>();

    // enemy handler variables
    private Handler gameLoopHandler = new Handler();
    private static int NECRO_LOOP_DELAY;
    private static int CHORT_LOOP_DELAY;
    private List<Enemy> enemies = new ArrayList<>();
    private boolean pause;
    private TimeCountdown timeCountDownTimer;
    private ScoreCountdown scoreCountDownTimer;
    // powerup variables
    private ImageView healthPowerUp;
    private int healthPowerUpX = 500;
    private int healthPowerUpY = 500;

    public static void setRestart() {
        necroAlive = true;
        chortAlive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_game_screen);
        gameLayout = findViewById(R.id.gameLayout);

        final Button pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(v -> {
            pauseGame();
        });

        player.registerObserver(this);
        player.registerObserver(weapon);

        // start the time countdown
        timeTextView = findViewById(R.id.textViewTime);
        timeTextView.setText("Time Spent: " + player.getTime() + " s");
        timeCountDownTimer = TimeCountdown.getInstance(200000, 1000);
        timeCountDownTimer.setOnTimeChangeListener(newTime -> timeTextView.setText("Time Spent: " + newTime + " s"));

        // start the score countdown
        countdownTextView = findViewById(R.id.viewScore);
        countdownTextView.setText("Score: " + player.getScore());
        scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
        scoreCountDownTimer.setOnScoreChangeListener(newScore -> countdownTextView.setText("Score: " + newScore));
        stop = false;

        //necromancer
        EnemyFactory NecroFactory = new NecromancerFactory();
        Enemy necromancer = NecroFactory.createEnemy(500, 500);
        necroView = new EnemyViewModel(this, necromancer);
        enemies.add(necromancer);
        NECRO_LOOP_DELAY = necromancer.getMovementSpeed();
        player.registerObserver(necromancer);
        if (necroAlive) {
            gameLayout.addView(necroView);
            necroView.setVisibility(necroView.VISIBLE);
            // start the game loops
            startNecromancerLoop();
        } else {
            necromancer.setAlive(false);
        }

        //chort
        EnemyFactory ChortFactory = new ChortFactory();
        Enemy chort = ChortFactory.createEnemy(500, 400);
        chortView = new EnemyViewModel(this, chort);
        enemies.add(chort);
        CHORT_LOOP_DELAY = chort.getMovementSpeed();
        player.registerObserver(chort);
        if (chortAlive) {
            gameLayout.addView(chortView);
            chortView.setVisibility(chortView.VISIBLE);
            // start the game loops
            startChortLoop();
        }else {
            chort.setAlive(false);
        }

        // initializing location of player and player name + starting animation
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

        textViewName.setText(player.getName());
        textViewDiff.setText("Difficulty: " + player.getDifficulty());
        textViewHealth.setText(String.valueOf(player.getHealth()));

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

        // loading the health powerup
        if (!player.isHealthPowerUpClaimed()) {
            healthPowerUp = new ImageView(this);
            healthPowerUp.setImageResource(R.drawable.heartsprite);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(80, 80);
            layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.leftMargin = healthPowerUpX;
            layoutParams.topMargin = healthPowerUpY;
            gameLayout.addView(healthPowerUp, layoutParams);
        }
    }

    private void startNecromancerLoop() {
        gameLoopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pause) {
                    Enemy necromancer = enemies.get(0);
                    // Move each enemy
                    if (necromancer.getDirection().equals("right")) {
                        if (!collidesWithAnyWall((int) necromancer.getX() + 50, (int) necromancer.getY())) {
                            necromancer.move();
                            if (necromancer.getX() >= maxX) {
                                necromancer.changeDirection("left");
                            }
                            necroView.invalidate();
                        } else {
                            necromancer.changeDirection("left");
                        }
                    } else {
                        if (!collidesWithAnyWall((int) necromancer.getX() - 50, (int) necromancer.getY())) {
                            necromancer.move();
                            if (necromancer.getX() <= minX) {
                                necromancer.changeDirection("right");
                            }
                            necroView.invalidate();
                        } else {
                            necromancer.changeDirection("right");
                        }
                    }
                    boolean scoreIncrease = false;
                    if (necromancer.contactWithWeapon(weapon) && swordView.getVisibility() == swordView.VISIBLE) {
                        gameLayout.removeView(necroView);
                        if (necroAlive) {
                            player.setScore(player.getScore() + 50);
                        }
                        necromancer.setAlive(false);
                        necroAlive = false;
                    }
                    // Repeat this runnable code again every GAME_LOOP_DELAY milliseconds
                    gameLoopHandler.postDelayed(this, NECRO_LOOP_DELAY);
                }
            }
        }, NECRO_LOOP_DELAY);
    }

    private void startChortLoop() {
        gameLoopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pause) {
                    Enemy chort = enemies.get(1);
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
                        chortAlive = false;
                        chort.setAlive(false);

                    }
                    // Repeat this runnable code again every GAME_LOOP_DELAY milliseconds
                    gameLoopHandler.postDelayed(this, CHORT_LOOP_DELAY);
                }
            }
        }, CHORT_LOOP_DELAY);
    }

    @Override
    public void update(float x, float y, String direction) {
        characterNameTextView.setX(x - 125);
        characterNameTextView.setY(y - characterNameTextView.getHeight() + 45);
    }

    // handles the animation of the player
    private void animationCountdown() {
        handler.postDelayed(() -> {
            if (!stop && !pause) {
                playerView.updateAnimation(animationCount % 4, player.getMovement());
                necroView.updateAnimation(animationCount % 4);
                chortView.updateAnimation(animationCount % 4);
                animationCount++;
                animationCountdown();
            }
        }, 200);
    }

    // updates the health of the player
    private void updateHealth() {
        handler.postDelayed(() -> {
            if (!stop && !pause) { // && !pause
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
                            Intent end = new Intent(MainGameActivity.this, EndActivity.class);
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
                    player.notifyObservers();
                    weapon.setAttackCooldown(true);
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
                    player.setProposedX(player.getProposedX() - 50);
                    break;
                }
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (!pause) {
                        strategy = new MoveRightStrategy();
                        player.setFacingRight(true);
                        weapon.setWeaponSwingDirection("right");
                        player.setPlayerDirection(weapon.getWeaponSwingDirection());
                        player.setProposedX(player.getProposedX() + 50);
                        break;
                    }
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (!pause) {
                        strategy = new MoveDownStrategy();
                        weapon.setWeaponSwingDirection("down");
                        player.setPlayerDirection(weapon.getWeaponSwingDirection());
                        player.setProposedY(player.getProposedY() + 50);
                        break;
                    }
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (!pause) {
                        strategy = new MoveUpStrategy();
                        weapon.setWeaponSwingDirection("up");
                        player.setPlayerDirection(weapon.getWeaponSwingDirection());
                        player.setProposedY(player.getProposedY() - 50);
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
        // if no wall collision, update player's position and updates Observers
        player.setMovementStrategy(strategy);
        if (!collidesWithAnyWall((int) player.getProposedX(), (int) player.getProposedY()) && strategy != null) {
            player.performMovement();
            player.notifyObservers();
        }

        // checking for collision with healthpowerup
        if (Math.abs(player.getX() - healthPowerUpX) < 80 && Math.abs(player.getY() - healthPowerUpY) < 80 && !player.isHealthPowerUpClaimed()) {
            player.setHealth(player.getHealth() + 25);
            player.setHealthPowerUpClaimed(true);
            gameLayout.removeView(healthPowerUp);
        }

        // checking to see if player is leaving the screen
        if (player.getX() < minX) {
            player.setX(minX);
        } else if (player.getX() > maxX) {
            player.removeObserver(this);
            for (int i = 0; i < enemies.size(); i++) {
                player.removeObserver(enemies.get(i));
            }
            player.removeObserver(weapon);
            playerView.setVisibility(playerView.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(minX + 10);
            weapon.setX(player.getX() + 50);
            Intent end = new Intent(MainGameActivity.this, SecondGameActivity.class);
            startActivity(end);
            finish();
        }

        if (player.getY() < minY) {
            player.setY(minY);
        } else if (player.getY() > maxY) {
            player.setY(maxY);
        }

        // playerView.updatePosition(player.getX(), player.getY());
        characterNameTextView.setX(player.getX() - 125);
        characterNameTextView.setY(player.getY() - characterNameTextView.getHeight() + 45);
        playerView.invalidate();
        if (swordView != null) {
            swordView.invalidate();
        }
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
            startNecromancerLoop();
            timeCountDownTimer.setPause(false);
            scoreCountDownTimer.setPause(false);

        }
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

