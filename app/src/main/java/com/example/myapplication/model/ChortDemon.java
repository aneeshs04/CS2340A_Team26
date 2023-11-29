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
    private boolean alive = true;
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
    public float getPlayerX() {return playerX;}
    public float getPlayerY() {return playerY;}

    @Override
    public void update(float x, float y, String playerDirection) {
        playerX = x;
        playerY = y;
    }

    @Override
    public boolean contactWithPlayer() {
        return (playerX + 90) > (x + 25) && (playerX + 20) < (x + 100)
                && (playerY + 115) > y && playerY < (y + 140);
    }

    @Override
    public boolean contactWithWeapon(Weapon weapon) {
        return weapon.getRecRight() > (x + 25) && weapon.getRecLeft() < (x + 100)
                && weapon.getRecDown() > y && weapon.getRecUp() < (y + 140);
    }

    @Override
    public boolean contactWithWeapon(int[] weaponRect) {
        return false;
    }

    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}

