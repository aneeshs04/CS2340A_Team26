package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.myapplication.model.BigDemon;

import com.example.myapplication.model.Player;

public class Sprint4UnitTests {
    @Test
    public void testCollision() {
        Player player = Player.getInstance();
        player.setX(500);
        player.setY(500);
        BigDemon enemy = new BigDemon(500, 500);
        player.registerObserver(enemy);
        player.notifyObservers();
        assertEquals(enemy.contactWithPlayer(), true);
    }

    @Test
    public void testNotifyObserver() {
        Player player = Player.getInstance();
        BigDemon enemy = new BigDemon(500, 500);
        player.registerObserver(enemy);
        player.notifyObservers();
        assertEquals(enemy.getPlayerX(), player.getX(), 0);
        assertEquals(enemy.getPlayerY(), player.getY(), 0);

    }

    @Test
    public void testNotifyObserverAfterMovement() {
        Player player = Player.getInstance();
        BigDemon enemy = new BigDemon(500, 500);
        player.registerObserver(enemy);
        player.setX(player.getX() - 50);
        player.setY(player.getY() - 50);
        assertEquals(enemy.getPlayerX(), player.getX(), 0);
        assertEquals(enemy.getPlayerY(), player.getY(), 0);

    }

    @Test
    public void testDecreaseHealthAfterContact() {
        Player player = Player.getInstance();
        BigDemon enemy = new BigDemon(500, 500);
        player.registerObserver(enemy);
        player.setX(500);
        player.setY(500);
        enemy.setPower(100);
        player.setHealth(200);
        enemy.updatePlayerHealth(enemy.contactWithPlayer(), enemy.getPower(), player.getHealth());
        assertEquals(100, player.getHealth(), 0);
    }

}
