package com.example.myapplication.model;

public class NecromancerDemon implements Enemy {
    private int health;
    private int movementSpeed;
    private int power;
    private int size;
    private float x,y;
    private Player player = Player.getInstance();
    private static String name = "necromancer demon";

    public NecromancerDemon() {
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
        this.x = 500;
        this.y = 2000;
        this.size = 20;
        this.movementSpeed = 20;
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
        this.x += 100;
//        Random random = new Random();
//        int movement = random.nextInt(11) - 5; // Random movement between -5 and 5
//        x += movement * movementSpeed;
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
}
