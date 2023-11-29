package com.example.myapplication.model;

public class StarPowerUpDecorator extends PlayerDecorator {
    public StarPowerUpDecorator() {
        super();
    }

    public void activateInvincibility(int duration) {
        player.activateInvincibility(duration + 10000); // Extend invincibility duration
    }
}
