package com.example.myapplication.model;

import com.example.myapplication.views.MainActivity;
import com.example.myapplication.views.MoveRightStrategy;
import com.example.myapplication.views.MovementStrategy;
import com.example.myapplication.views.Observer;
import com.example.myapplication.views.Subject;

import java.util.ArrayList;
import java.util.List;

public class Player implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private MovementStrategy movementStrategy;
    private static Player player;
    private int score;
    private int health;
    private String difficulty = MainActivity.getDifficulty();
    private float x,y;
    private float proposedX, proposedY;
    private final float originalX, originalY;
    private Player () {
        movementStrategy = new MoveRightStrategy();
        this.score = 105;

        switch (difficulty) {
            case "easy":
                this.health = 150;
                break;
            case "medium":
                this.health = 100;
                break;
            default:
                this.health = 50;
                break;
        }

        this.x = 550;
        this.y = 2000;
        originalX = x;
        originalY = y;
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

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        notifyObservers();
    }

    public void setY(float y) {
        this.y = y;
        notifyObservers();
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
            observer.update(x, y);
        }
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public float getOriginalX() {
        return originalX;
    }

    public float getOriginalY() {
        return originalY;
    }

//    public String getCharacter() {return character;}
//    public void setCharacter(String character) {
//        this.character = character;
//    }
}