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
import android.widget.PopupWindow;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.R;
import com.example.myapplication.model.Enemy;
import com.example.myapplication.model.EnemyFactory;
import com.example.myapplication.model.ImpFactory;
import com.example.myapplication.model.Leaderboard;
import com.example.myapplication.model.NecromancerFactory;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.PlayerDecorator;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.model.SpeedPowerUpDecorator;
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


public class SecondGameActivity extends AppCompatActivity implements Observer {
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
    private EnemyViewModel necroView;
    private EnemyViewModel impView;
    private static boolean necroAlive = true;
    private static boolean impAlive = true;
    ConstraintLayout gameLayout;
    private final int minX = 0; // Left boundary
    private final int minY = -50; // Top boundary
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
    private static int NECRO_LOOP_DELAY;
    private static int IMP_LOOP_DELAY;
    private List<Enemy> enemies = new ArrayList<>();
    private boolean pause;
    private TimeCountdown timeCountDownTimer;
    private ScoreCountdown scoreCountDownTimer;
    // powerup variables
    private ImageView speedPowerUp;
    private int speedPowerUpX = 650;
    private int speedPowerUpY = 2035;

    public static void setRestart() {
        necroAlive = true;
        impAlive = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_game_screen);

        player.registerObserver(this);
        player.registerObserver(weapon);

        final Button pauseButton = findViewById(R.id.pauseButton2);
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
        scoreCountDownTimer = ScoreCountdown.getInstance(100000, 2000);
        scoreCountDownTimer.setOnScoreChangeListener(newScore -> countdownTextView.setText("Score: " + newScore));
        stop = false;

        // necromancer
        gameLayout = findViewById(R.id.gameLayout2);
        EnemyFactory NecroFactory = new NecromancerFactory();
        Enemy necromancer = NecroFactory.createEnemy(500, 850);
        enemies.add(necromancer);
        necroView = new EnemyViewModel(this, necromancer);
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

        // imp
        EnemyFactory ImpFactory = new ImpFactory();
        Enemy imp = ImpFactory.createEnemy(100, 2000);
        enemies.add(imp);
        impView = new EnemyViewModel(this, imp);
        IMP_LOOP_DELAY = imp.getMovementSpeed();
        player.registerObserver(imp);
        if (impAlive) {
            gameLayout.addView(impView);
            impView.setVisibility(impView.VISIBLE);
            // Start the game loops
            startImpLoop();
        } else {
            imp.setAlive(false);
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

        // create walls for second screen
        walls.add(new Wall(150, 80, 1350, 150));
        walls.add(new Wall(0, 0, 150, 400));
        walls.add(new Wall(1100, 0, 1440, 400));
        walls.add(new Wall(1100, 582, 1440, 3500));
        walls.add(new Wall(0, 582, 150, 1862));
        walls.add(new Wall(150, 2280, 1500, 3000));
        walls.add(new Wall(0, 2062, 150, 2500));
        walls.add(new Wall(150, 990, 480, 1550));
        walls.add(new Wall(800, 990, 1480, 1550));

        // loading the Speed powerup
        if (!player.isSpeedPowerUpClaimed()) {
            speedPowerUp = new ImageView(this);
            speedPowerUp.setImageResource(R.drawable.speedsprite);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(80, 80);
            layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.leftMargin = speedPowerUpX;
            layoutParams.topMargin = speedPowerUpY;
            gameLayout.addView(speedPowerUp, layoutParams);
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

    private void startImpLoop() {
        gameLoopHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pause) {
                    Enemy imp = enemies.get(1);
                    // Move each enemy
                    if (imp.getDirection() == "up") {
                        if (!collidesWithAnyWall((int) imp.getX() + 100, (int) imp.getY() - 100)) {
                            imp.move();
                            if (imp.getX() >= maxX) {
                                imp.changeDirection("down");
                            }
                            impView.invalidate();
                        } else {
                            imp.changeDirection("down");
                        }
                    } else {
                        if (!collidesWithAnyWall((int) imp.getX() - 25, (int) imp.getY() + 25)) {
                            imp.move();
                            if (imp.getX() <= minX) {
                                imp.changeDirection("up");
                            }
                            impView.invalidate();
                        } else {
                            imp.changeDirection("up");
                        }
                    }
                    if (imp.contactWithWeapon(weapon) && swordView.getVisibility() == swordView.VISIBLE) {
                        gameLayout.removeView(impView);
                        if (impAlive) {
                            player.setScore(player.getScore() + 100);
                        }
                        imp.setAlive(false);
                        impAlive = false;
                    }
                    // Repeat this runnable code again every GAME_LOOP_DELAY milliseconds
                    gameLoopHandler.postDelayed(this, IMP_LOOP_DELAY);
                }
            }
        }, IMP_LOOP_DELAY);
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
                impView.updateAnimation(animationCount % 4);
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
                            Intent end = new Intent(SecondGameActivity.this, EndActivity.class);
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

        // checking for collision with speed powerup
        if (Math.abs(player.getX() - speedPowerUpX) < 80 && Math.abs(player.getY() - speedPowerUpY) < 80 && !player.isSpeedPowerUpClaimed()) {
            PlayerDecorator playerWithSpeed = new SpeedPowerUpDecorator();
            playerWithSpeed.setSpeedMultiplier(1.5);
            player.setSpeedPowerUpClaimed(true);
            gameLayout.removeView(speedPowerUp);
        }

        // checking to see if player is leaving the screen
        if (player.getX() < minX) {
            player.removeObserver(this);
            for (int i = 0; i < enemies.size(); i++) {
                player.removeObserver(enemies.get(i));
            }
            player.removeObserver(weapon);
            playerView.setVisibility(View.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(maxX - 10);
            weapon.setX(player.getX());
            Intent end = new Intent(SecondGameActivity.this, MainGameActivity.class);
            startActivity(end);
            finish();
        } else if (player.getX() > maxX) {
            player.removeObserver(this);
            for (int i = 0; i < enemies.size(); i++) {
                player.removeObserver(enemies.get(i));
            }
            player.removeObserver(weapon);
            playerView.setVisibility(View.INVISIBLE);
            characterNameTextView.setVisibility(View.INVISIBLE);
            stop = true;
            player.setX(minX + 10);
            weapon.setX(player.getX() + 50);
            Intent end = new Intent(SecondGameActivity.this, ThirdGameActivity.class);
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
            startImpLoop();
            startNecromancerLoop();
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
