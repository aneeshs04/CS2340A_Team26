package com.example.myapplication.model;

import com.example.myapplication.views.MainActivity;

public class Player {
    private static Player player;
    private int score;
    private int health;
    private String difficulty = MainActivity.getDifficulty();
    private float x,y;
    private final float originalX, originalY;
    private Player () {
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
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
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