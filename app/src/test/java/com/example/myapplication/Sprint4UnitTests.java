package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import static org.junit.Assert.*;
import com.example.myapplication.model.BigDemon;
import com.example.myapplication.model.BigDemonFactory;
import com.example.myapplication.model.ChortDemon;
import com.example.myapplication.model.ChortFactory;
import com.example.myapplication.model.Enemy;
import com.example.myapplication.model.EnemyFactory;

import com.example.myapplication.model.ImpFactory;
import com.example.myapplication.model.MoveDownStrategy;
import com.example.myapplication.model.MoveLeftStrategy;
import com.example.myapplication.model.MoveRightStrategy;
import com.example.myapplication.model.MoveUpStrategy;
import com.example.myapplication.model.NecromancerFactory;
import com.example.myapplication.model.Player;


public class Sprint4UnitTests {
    @Test
    public void testPlayerEnemyCollision() {
        Player player = Player.getInstance();
        player.setX(500);
        player.setY(500);
        BigDemon enemy = new BigDemon(500, 500);
        player.registerObserver(enemy);
        player.notifyObservers();
        assertEquals(enemy.contactWithPlayer(), true);
    }

    @Test
    public void testEnemiesHaveDifferentMovementPatterns() {
        EnemyFactory necroDemonFactor = new NecromancerFactory();
        Enemy necromancerDemon = necroDemonFactor.createEnemy(100, 100);
        EnemyFactory chortDemonFactory = new ChortFactory();
        Enemy chortDemon = chortDemonFactory.createEnemy(100, 100);
        necromancerDemon.move();
        chortDemon.move();
        assertNotEquals(chortDemon.getX(), necromancerDemon.getX());
        assertNotEquals(chortDemon.getY(), necromancerDemon.getY());
    }

    @Test
    public void testEnemiesHaveDifferentAttributes() {
        EnemyFactory bigDemonFactory = new BigDemonFactory();
        Enemy bigDemon = bigDemonFactory.createEnemy(100, 100);
        EnemyFactory chortDemonFactory = new ChortFactory();
        Enemy chortDemon = chortDemonFactory.createEnemy(100, 100);
        EnemyFactory impDemonFactory = new ImpFactory();
        Enemy impDemon = impDemonFactory.createEnemy(100, 100);
        EnemyFactory necroDemonFactor = new NecromancerFactory();
        Enemy necromancerDemon = necroDemonFactor.createEnemy(100, 100);
        assertTrue(bigDemon.getMovementSpeed() != chortDemon.getMovementSpeed());
        assertTrue(impDemon.getSize() != necromancerDemon.getSize());
    }

    @Test
    public void testEnemyMovementWithinScreenBounds() {
        Player player = Player.getInstance();
        EnemyFactory bigDemonFactory = new BigDemonFactory();
        Enemy bigDemon = bigDemonFactory.createEnemy(100, 100);
        EnemyFactory chortDemonFactory = new ChortFactory();
        Enemy chortDemon = chortDemonFactory.createEnemy(100, 100);
        bigDemon.move();
        assertTrue(bigDemon.getX() >= 0 && bigDemon.getY() >= 0);
        chortDemon.move();
        assertTrue(chortDemon.getX() >= 0 && chortDemon.getY() >= 0);
    }

    @Test
    public void testRealTimeHPUpdateOnCollision() {
        Player player = Player.getInstance();
        EnemyFactory bigDemonFactory = new BigDemonFactory();
        Enemy bigDemon = bigDemonFactory.createEnemy(100, 100);
        player.setHealth(100);
        player.setX(bigDemon.getX());
        player.setY(bigDemon.getY());
        player.notifyObservers();
        player.setX(500);
        player.setY(500);
        BigDemon enemy = new BigDemon(500, 500);
        player.registerObserver(enemy);
        assertEquals(player.getHealth(), 100);
    }


    @Test
    public void testPlayerEnemyNoCollision() {
        Player player = Player.getInstance();
        Enemy enemy = new BigDemon((int) player.getX() + 100, (int) player.getY() + 100);
        assertFalse(enemy.contactWithPlayer());
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

    @Test
    public void testCreateEnemy() {
        EnemyFactory factory = new BigDemonFactory();
        Enemy enemy = factory.createEnemy(100, 100);

        assertNotNull(enemy);
        assertTrue(enemy instanceof BigDemon);
    }

    @Test
    public void testEnemyAttributes() {
        Enemy enemy = new BigDemon(100, 100);

        assertEquals(100, enemy.getX(), 0.01);
        assertEquals(100, enemy.getY(), 0.01);
        assertTrue(enemy.getHealth() > 0);
        assertTrue(enemy.getMovementSpeed() > 0);
    }

    @Test
    public void testDifferentEnemyTypes() {
        EnemyFactory factory1 = new BigDemonFactory();
        EnemyFactory factory2 = new ChortFactory();

        Enemy enemy1 = factory1.createEnemy(100, 100);
        Enemy enemy2 = factory2.createEnemy(100, 100);

        assertNotEquals(enemy1.getClass(), enemy2.getClass());
    }

    @Test
    public void testEnemyMovement() {
        Enemy enemy = new BigDemon(100, 100);
        enemy.move();

        assertTrue(enemy.getX() != 100 || enemy.getY() != 100);
    }

    @Test
    public void testDifferentEnemyMovementPatterns() {
        Enemy enemy1 = new BigDemon(100, 100);
        Enemy enemy2 = new ChortDemon(100, 100);

        enemy1.move();
        enemy2.move();
    }
}
