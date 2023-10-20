package com.example.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.views.MainActivity;

public class Player {
    private static Player player;
    private int score;
    private int health;
    private String difficulty = MainActivity.getDifficulty();
    private float x,y;

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
    }

    public static synchronized Player getInstance() {
        if (player == null) {
            player = new Player();
        }
        return player;
    }

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
}