package com.example.myapplication.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.myapplication.viewmodels.ThirdGameActivity;
import com.example.myapplication.views.EndActivity;
import com.example.myapplication.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class Player implements Subject {
    private List<Observer> enemies = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();


    private MovementStrategy movementStrategy;
    private static Player player;
    private static String name;
    private static String character;
    private int score;
    private int time;
    private int health;
    private String difficulty = "easy";
    private float x;
    private float y;
    private int movementSpeed;
    private boolean isFacingRight;
    private float proposedX, proposedY;
    private final float originalX, originalY;
    private boolean won = false;
    private boolean invincibility = false;
    private boolean movement = false;
    private String playerDirection = "right";
    private static boolean healthPowerUpClaimed = false;
    private static boolean speedPowerUpClaimed = false;
    private static boolean starPowerUpClaimed = false;
    private static double speedMultiplier = 1;
    private Player () {
        this.movementSpeed = 50;
        this.movementStrategy = new MoveRightStrategy();
        this.score = 105;

        this.x = 550;
        this.y = 2000;
        originalX = x;
        originalY = y;
        isFacingRight = true;
    }

    public static synchronized Player getInstance() {
        if (player == null) {
            player = new Player();
        }
        return player;
    }

    // getters/setters to allow other classes to get/modify private variables
    public float getProposedX() {
        return proposedX;
    }

    public float getProposedY() {
        return proposedY;
    }

    public void setProposedX(float proposedX) {
        this.proposedX = proposedX;
    }

    public void setProposedY(float proposedY) {
        this.proposedY = proposedY;
    }

    public void setX(float x) {
        this.x = x;
        notifyObservers();

    }

    public void setY(float y) {
        this.y = y;
        notifyObservers();
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }

    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(x, y, playerDirection);
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Boolean getWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setMovementStrategy(MovementStrategy strategy) {
        movementStrategy = strategy;
    }

    public void performMovement() {
        movementStrategy.move(this);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Player.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        if (health > 0){
            this.health = health;
        } else {
            this.health = 0;
        }
    }

    public static String getCharacter() {
        return character;
    }

    public static void setCharacter(String character) {
        Player.character = character;
    }

    public float getOriginalX() {
        return originalX;
    }

    public float getOriginalY() {
        return originalY;
    }

    public boolean getInvincibility() {
        return invincibility;
    }

    public void setInvincibility(boolean invincibility) {
        this.invincibility = invincibility;
    }

    public boolean getMovement() {
        return movement;
    }

    public void setMovement(boolean movement) {
        this.movement = movement;
    }

    public void setPlayerDirection(String playerDirection) {
        this.playerDirection = playerDirection;
    }

    public String getPlayerDirection() {
        return playerDirection;
    }
    public boolean isHealthPowerUpClaimed() {
        return healthPowerUpClaimed;
    }

    public void setHealthPowerUpClaimed(boolean healthPowerUpClaimed) {
        this.healthPowerUpClaimed = healthPowerUpClaimed;
    }
    public boolean isStarPowerUpClaimed() {
        return starPowerUpClaimed;
    }

    public void setStarPowerUpClaimed(boolean starPowerUpClaimed) {
        this.starPowerUpClaimed = starPowerUpClaimed;
    }

    public boolean isSpeedPowerUpClaimed() {
        return speedPowerUpClaimed;
    }

    public void setSpeedPowerUpClaimed(boolean speedPowerUpClaimed) {
        this.speedPowerUpClaimed = speedPowerUpClaimed;
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
    public static void restartPowerups() {
        speedMultiplier = 1;
        speedPowerUpClaimed = false;
        healthPowerUpClaimed = false;
        starPowerUpClaimed = false;
    }
    public void activateInvincibility(int duration) {
        this.invincibility = true;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            this.invincibility = false;
        }, duration);
    }
}