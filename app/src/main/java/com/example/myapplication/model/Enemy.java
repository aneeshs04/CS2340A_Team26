package com.example.myapplication.model;
public interface Enemy extends Observer{
    void move();
    int getHealth();
    void setHealth(int health);
    int getMovementSpeed();
    void setMovementSpeed(int speed);
    public int getPower();
    public void setPower(int power);
    public int getSize();
    public void setSize(int size);
    public float getX();
    public float getY();
    public void setX(float x);
    public void setY(float y);
    public String getName();
    public String getDirection();
    public void changeDirection(String direction);

    public void update(float x, float y, String playerDirection);
    public boolean contactWithPlayer();
    public boolean contactWithWeapon(Weapon weapon);
    public boolean isAlive();
    public void setAlive(boolean alive);



    }
