package com.example.myapplication.model;

public class ImpDemon implements Enemy {
    private int health;
    private int movementSpeed;
    private int power;
    private Player player = Player.getInstance();
    private int size;
    private float x,y;
    private static String name = "imp demon";
    private String direction = "up";
    private float playerX,playerY;
    private String playerDirection;

    public ImpDemon(int x, int y) {
        switch (player.getDifficulty()) {
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
        this.x = x;
        this.y = y;
        this.size = 5;
        this.movementSpeed = 50;
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
            this.x += 50;
            this.y -= 50;
        } else {
            this.x -= 50;
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

    @Override
    public void update(float x, float y, String playerDirection) {
        playerX = x;
        playerY = y;
        this.playerDirection = playerDirection;
    }

    @Override
    public boolean contactWithPlayer() {
        return (playerX + 90) > (x + 25) && (playerX + 20) < (x + 80)
                && (playerY + 115) > y && playerY < (y + 90);
    }

    @Override
    public boolean contactWithWeapon(Weapon weapon) {
        return weapon.getRecRight() > (x + 25) && weapon.getRecLeft() < (x + 80)
                && weapon.getRecDown() > y && weapon.getRecUp() < (y + 90);
    }
}


