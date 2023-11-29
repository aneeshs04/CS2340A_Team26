package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.myapplication.model.HealthPowerUpDecorator;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.PlayerDecorator;
import com.example.myapplication.model.SpeedPowerUpDecorator;
import com.example.myapplication.model.StarPowerUpDecorator;


public class Sprint5UnitTests {
    @Test
    public void testHealthPowerUpIncreasesHealth() {
        Player player = Player.getInstance();
        int initialHealth = player.getHealth();
        PlayerDecorator playerWithHealthPowerUp = new HealthPowerUpDecorator();
        playerWithHealthPowerUp.setHealth(initialHealth);
        assertTrue("Health should increase after collecting power-up",
                player.getHealth() > initialHealth);
    }
    @Test
    public void testSpeedPowerUpIncreasesSpeed() {
        Player player = Player.getInstance();
        double initialSpeedMultiplier = player.getSpeedMultiplier();
        PlayerDecorator playerWithSpeedPowerUp = new SpeedPowerUpDecorator();
        playerWithSpeedPowerUp.setSpeedMultiplier(initialSpeedMultiplier);
        assertTrue("Speed multiplier should increase after collecting power-up",
                player.getSpeedMultiplier() > initialSpeedMultiplier);
    }
}
