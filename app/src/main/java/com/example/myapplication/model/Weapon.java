package com.example.myapplication.model;
import android.graphics.Rect;
public class Weapon implements Observer{

    private float x;
    private float y;
    private long weaponAttackDelay;
    private float playerX, playerY;
    //private float deltax, deltay;
    private boolean attackCooldown;
    private static Weapon weapon;
    private String weaponSwingDirection;
    private int recLeft, recRight, recUp, recDown;

    private Weapon () {
        playerX = 550;
        playerY = 2000;
        attackCooldown = false;
        weaponSwingDirection = "right";
        weaponAttackDelay = 0; //one second delay between swings
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
        playerX = x + 50;
        playerY = y + 125;
        this.x = playerX;
        this.y = playerY;
    }

    public Rect getDestRect() {
        Rect destRect;
        switch (weaponSwingDirection) {
            case "right":
                destRect = new Rect((int) x, (int) (y - 300 / 2)
                        , (int) (x + 150), (int) (y + 300 / 2));
                recLeft = destRect.left;
                recRight = destRect.right;
                recUp = destRect.top;
                recDown = destRect.bottom;
                return destRect;
            case "left":
                destRect = new Rect((int) (x - 150), (int) (y - 300 / 2)
                        , (int) x, (int) (y + 300 / 2));
                recLeft = destRect.left;
                recRight = destRect.right;
                recUp = destRect.top;
                recDown = destRect.bottom;
                return destRect;
            case "up":
                destRect = new Rect((int) (x - 300 / 2), (int) (y - 150)
                        , (int) (x + 300 / 2), (int) (y));;
                recLeft = destRect.left;
                recRight = destRect.right;
                recUp = destRect.top;
                recDown = destRect.bottom;
                return destRect;
            case "down":
                destRect = new Rect((int) (x - 300 / 2), (int) (y)
                        , (int) (x + 300 / 2), (int) (y + 150));
                recLeft = destRect.left;
                recRight = destRect.right;
                recUp = destRect.top;
                recDown = destRect.bottom;
                return destRect;
        }
        return null;
    }

    public int getRecRight() {
        return recRight;
    }
    public int getRecLeft() {
        return recLeft;
    }
    public int getRecUp() {
        return recUp;
    }
    public int getRecDown() {
        return recDown;
    }

}
