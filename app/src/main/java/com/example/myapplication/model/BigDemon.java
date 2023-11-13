package com.example.myapplication.model;

import com.example.myapplication.views.MainActivity;

public class BigDemon implements Enemy {
    private int health;
    private int movementSpeed;
    private int power;
    private int size;
    private String difficulty;
    private float x,y;
    private Player player = Player.getInstance();
    private static String name = "big demon";
    private String direction = "up";
    private float playerX,playerY;

    public BigDemon(int x, int y) {
        switch (player.getDifficulty()) {
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
        this.x = x;
        this.y = y;
        this.size = 5;
        this.movementSpeed = 200;
    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {this.x = x;}

    public void setY(float y) {this.y = y;}

    public String getName() {
        return name;
    }

    @Override
    public void move() {
        if (direction.equals("left")) {
            this.x -= 50;
        } else if (direction.equals("right")) {
            this.x += 50;
        } else if (direction.equals("up")) {
            this.y -= 50;
        } else if (direction.equals("down")) {
            this.y += 50;
        }
    }

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
    public void changeDirection(String direction) {
        this.direction = direction;
    }
    public String getDirection() {return direction;}
    public float getPlayerX() {return playerX;}
    public float getPlayerY() {return playerY;}

    public void updatePlayerHealth(boolean contact, int power, int health) {
        if (contact == true) {
            player.setHealth(health - power);
        }
    }
    @Override
    public void update(float x, float y) {
        playerX = x;
        playerY = y;

    }

    @Override
    public boolean contactWithPlayer() {
        return playerX > (x - 150) && playerX < (x + 150)
                && playerY > (y - 160) && playerY < (y + 160);
    }
}
