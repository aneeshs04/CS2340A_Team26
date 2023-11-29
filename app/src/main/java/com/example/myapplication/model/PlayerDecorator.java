package com.example.myapplication.model;

public abstract class PlayerDecorator {
    protected Player player;

    public PlayerDecorator() {
        this.player = Player.getInstance();
    }

    public void setHealth(int health) {
        player.setHealth(health);
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        player.setSpeedMultiplier(speedMultiplier);
    }

    public void activateInvincibility(int duration) {
        player.activateInvincibility(duration);
    }

    public int getHealth() {
        return player.getHealth();
    }
}
