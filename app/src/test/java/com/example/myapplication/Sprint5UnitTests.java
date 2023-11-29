package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.myapplication.model.HealthPowerUpDecorator;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.PlayerDecorator;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.model.SpeedPowerUpDecorator;
import com.example.myapplication.model.StarPowerUpDecorator;
import com.example.myapplication.model.Weapon;


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

    @Test
    public void testScoreChangesWithTime() {
        Player player = Player.getInstance();
        ScoreCountdown scoreCountdown = ScoreCountdown.getInstance(60000, 1000);
        int initialScore = player.getScore();
        scoreCountdown.onTick(50000); // Simulate half a minute has passed
        assertNotEquals("Score should have decreased", initialScore, player.getScore());
    }

    @Test
    public void testPlayerMovingRightSwordSwingsRight() {
        Player player = Player.getInstance();
        Weapon weapon = Weapon.getInstance();

        player.setPlayerDirection("right");
        player.performMovement();
        String weaponSwingDirection = weapon.getWeaponSwingDirection();

        assertEquals("right", weaponSwingDirection);
    }


}
