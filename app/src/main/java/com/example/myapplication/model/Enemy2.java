package com.example.myapplication.model;

import com.example.myapplication.views.MainActivity;

public class Enemy2 implements Enemy{
    private int health;
    private int movementSpeed;
    private int power;
    private int size;
    private String difficulty;
    private float x,y;

    public Enemy2() {
        if (MainActivity.getDifficulty() != null) {
            difficulty = MainActivity.getDifficulty();
        }
        switch (difficulty) {
            case "easy":
                this.health = 10;
                this.power = 10;
                break;
            case "medium":
                this.health = 50;
                this.power = 30;
                break;
            default:
                this.health = 100;
                this.power = 50;
                break;
        }
        this.x = 500;
        this.y = 2000;
        this.size = 5;
        this.movementSpeed = 40;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {this.x = x;}

    public void setY(float y) {this.y = y;}
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {this.health = health;}
    public int getMovementSpeed() {return movementSpeed;}
    public void setMovementSpeed(int movementSpeed) {this.movementSpeed = movementSpeed;}
    public int getPower() {return power;}
    public void setPower(int power) {this.power = power;}
    public int getSize() {return size;}
    public void setSize(int size) {this.size = size;}
}
