package com.example.myapplication.model;

public class NecromancerDemon implements Enemy {
    private int health;
    private int movementSpeed;
    private int power;
    private int size;
    private float x,y;
    private Player player = Player.getInstance();
    private static String name = "necromancer demon";
    private String direction = "right";
    private float playerX,playerY;

    public NecromancerDemon(int x, int y) {
        switch (player.getDifficulty()) {
            case "easy":
                this.health = 30;
                this.power = 20;
                break;
            case "medium":
                this.health = 100;
                this.power = 50;
                break;
            default:
                this.health = 300;
                this.power = 100;
                break;
        }
        this.x = x;
        this.y = y;
        this.size = 20;
        this.movementSpeed = 100;
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
        if (direction.equals("right")) {
            this.x += 50;
        } else {
            this.x -= 50;
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
    public String getDirection() {return direction;}
    public void changeDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public void update(float x, float y) {
        playerX = x;
        playerY = y;

    }

    @Override
    public boolean contactWithPlayer() {
        return playerX > (x - 100) && playerX < (x + 100)
                && playerY > (y - 100) && playerY < (y + 100);
    }
}
