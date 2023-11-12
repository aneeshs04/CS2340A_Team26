package com.example.myapplication.model;

public class ChortDemon implements Enemy {
    private int health;
    private int movementSpeed;
    private int power;
    private int size;
    private Player player = Player.getInstance();
    private float x,y;
    private static String name = "chort demon";
    private String direction = "up";
    private float playerX,playerY;

    public ChortDemon(int x, int y) {
        switch (player.getDifficulty()) {
            case "easy":
                this.health = 10;
                this.power = 20;
                break;
            case "medium":
                this.health = 50;
                this.power = 40;
                break;
            default:
                this.health = 100;
                this.power = 90;
                break;
        }
        this.x = x;
        this.y = y;
        this.size = 5;
        this.movementSpeed = 75;
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
        if (direction.equals("up")) {
            this.y += 50;
        } else {
            this.y -= 50;
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

    @Override
    public void update(float x, float y) {
        playerX = x;
        playerY = y;

    }

    @Override
    public boolean contactWithPlayer() {
        return playerX > (x - 100) && playerX < (x + 120)
                && playerY > (y - 80) && playerY < (y + 120);
    }
}

