package com.example.myapplication.model;

import com.example.myapplication.views.MainActivity;

public class Enemy4 implements Enemy{
    private int health;
    private int movementSpeed;
    private int power;
    private int size;
    private String difficulty;
    private float x,y;

    public Enemy4() {
        if (MainActivity.getDifficulty() != null) {
            difficulty = MainActivity.getDifficulty();
        }
        switch (difficulty) {
            case "easy":
                this.health = 50;
                this.power = 40;
                break;
            case "medium":
                this.health = 100;
                this.power = 100;
                break;
            default:
                this.health = 400;
                this.power = 250;
                break;
        }
        this.x = 500;
        this.y = 2000;
        this.size = 5;
        this.movementSpeed = 10;
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


