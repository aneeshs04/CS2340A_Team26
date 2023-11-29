package com.example.myapplication.model;

public class SpeedPowerUpDecorator extends PlayerDecorator {
    public SpeedPowerUpDecorator() {
        super();
    }

    public void setSpeedMultiplier(double speedMultiplier) {
        player.setSpeedMultiplier(speedMultiplier + 0.5); // Increase speed
    }

}
