package com.example.myapplication.model;

public class Weapon implements Observer{
    private float x;
    private float y;
    private boolean attackCooldown;
    private static Weapon weapon;
    private String weaponSwingDirection;
    private long weaponAttackDelay;
    private float playerX, playerY;
    private float deltax, deltay;
    //private boolean playerDirectionRight;


    private Weapon () {
        playerX = 550;
        playerY = 2000;
        attackCooldown = false;
        weaponSwingDirection = "right";
        //playerDirectionRight = true;
        weaponAttackDelay = 1000; //one second delay between swings
    }
    public static Weapon getInstance() {
        if (weapon == null) {
            weapon = new Weapon();
        }
        return weapon;
    }
    public String getWeaponSwingDirection() {
        return weaponSwingDirection;
    }
    public void setWeaponSwingDirection(String weaponSwingDirection) {
        this.weaponSwingDirection = weaponSwingDirection;
    }
    public void setAttackCooldown(boolean attackCooldown) {
        this.attackCooldown = attackCooldown;
    }
    public boolean isAttackCooldown() {
        return attackCooldown;
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
    public long getWeaponAttackDelay() {
        return weaponAttackDelay;
    }
    public void setWeaponAttackDelay(long delay) {
        weaponAttackDelay = delay;
    }

    @Override
    public void update(float x, float y, String playerDirection) {
        playerX = x;
        playerY = y;
        this.x = playerX + 50;
        this.y = playerY + 100;
    }

//    public boolean isPlayerDirectionRight() {
//        return playerDirectionRight;
//    }
//    public void setPlayerDirectionRight(boolean right) {
//        playerDirectionRight = right;
//    }

}
