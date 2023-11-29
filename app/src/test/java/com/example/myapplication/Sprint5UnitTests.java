package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.junit.Assert.*;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import com.example.myapplication.model.HealthPowerUpDecorator;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.PlayerDecorator;
import com.example.myapplication.model.ScoreCountdown;
import com.example.myapplication.model.SpeedPowerUpDecorator;
import com.example.myapplication.model.StarPowerUpDecorator;
import com.example.myapplication.model.Weapon;
import com.example.myapplication.viewmodels.MainGameActivity;
import com.example.myapplication.viewmodels.WeaponViewModel;

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

    @Test
    public void testPlayerMovingRightSwordSwingsLeft() {
        Player player = Player.getInstance();
        Weapon weapon = Weapon.getInstance();

        player.setPlayerDirection("left");
        player.registerObserver(weapon);
        player.performMovement();
        player.notifyObservers();
        String weaponSwingDirection = weapon.getWeaponSwingDirection();

        assertEquals("left", weaponSwingDirection);
    }

    @Test
    public void testPlayerMovingRightSwordSwingsUp() {
        Player player = Player.getInstance();
        Weapon weapon = Weapon.getInstance();

        player.setPlayerDirection("up");
        player.registerObserver(weapon);
        player.performMovement();
        player.notifyObservers();
        String weaponSwingDirection = weapon.getWeaponSwingDirection();

        assertEquals("up", weaponSwingDirection);
    }

    @Test
    public void testPlayerMovingRightSwordSwingsDown() {
        Player player = Player.getInstance();
        Weapon weapon = Weapon.getInstance();

        player.setPlayerDirection("down");
        player.registerObserver(weapon);
        player.performMovement();
        player.notifyObservers();
        String weaponSwingDirection = weapon.getWeaponSwingDirection();

        assertEquals("down", weaponSwingDirection);
    }

//    @Test
//    public void testPlayerWeaponHitBoxRight() {
//        Player player = Player.getInstance();
//        Weapon weapon = Weapon.getInstance();
//
//        weapon.setWeaponSwingDirection("right");
//        assertEquals(weapon.getDestRect(), new Rect((int) weapon.getX()
//                , (int) (weapon.getY() - 300 / 2), (int) (weapon.getX() + 150)
//                , (int) (weapon.getY() + 300 / 2)));
//    }
//    @Test
//    public void testInvinciblePowerUpGivesInvicibility() {
//        Player player = Player.getInstance();
//        PlayerDecorator playerwithInvincibility = new StarPowerUpDecorator();
//        playerwithInvincibility.activateInvincibility(1000);
//        assertTrue("collecting stars should give player invincibility",
//                player.getInvincibility() == true);
//    }
//
//    @Test
//    public void testInvinciblePowerUpLastsWithTime (){
//        Player player = Player.getInstance();
//        player.activateInvincibility(1000);
//        assertTrue(player.getInvincibility() == true);
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            assertTrue(player.getInvincibility() == false);
//        }, 1000);
//    }
    
    @Test
    public void testWeaponCooldown() {
        Weapon weapon = Weapon.getInstance();
        weapon.setAttackCooldown(true);
        assertTrue("Weapon should be on cooldown", weapon.isAttackCooldown());
    }
}
