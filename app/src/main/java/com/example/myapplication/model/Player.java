package com.example.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.views.MainActivity;

public class Player extends AppCompatActivity {
    private static Player player;
    private int score;
    private int health;
    final private String difficulty = MainActivity.getDifficulty();

    private Player () {
        this.score = 100;

        if (difficulty.equals("easy")) {
            this.health = 150;
        } else if (difficulty.equals("medium")) {
            this.health = 100;
        } else {
            this.health = 50;
        }
    }

    public static synchronized Player getInstance() {
        if (player == null) {
            player = new Player();
        }
        return player;
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
