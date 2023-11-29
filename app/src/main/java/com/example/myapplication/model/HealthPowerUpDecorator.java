package com.example.myapplication.model;

public class HealthPowerUpDecorator extends PlayerDecorator {
    public HealthPowerUpDecorator() {
        super();
    }

    @Override
    public void setHealth(int health) {
        super.setHealth(health + 25);
    }

}
