package com.example.myapplication;

import static org.junit.Assert.assertEquals;


import android.view.KeyEvent;
import com.example.myapplication.model.Player;
import com.example.myapplication.model.Wall;
import com.example.myapplication.viewmodels.MainGameActivity;
import com.example.myapplication.views.MainActivity;
import com.example.myapplication.views.MoveLeftStrategy;
import com.example.myapplication.views.MoveRightStrategy;
import com.example.myapplication.views.MoveUpStrategy;
import com.example.myapplication.views.MoveDownStrategy;
import com.example.myapplication.views.MovementStrategy;

import org.junit.Test;

import java.security.Key;

public class Sprint3UnitTests {
    @Test
    public void testMoveLeft() {
        Player player = Player.getInstance();
        float initialX = player.getX();
        MovementStrategy strategy = new MoveLeftStrategy();
        player.setMovementStrategy(strategy);
        player.performMovement();
        assertEquals(initialX - 50.0, player.getX(), 0);
    }

    @Test
    public void testMoveRight() {
        Player player = Player.getInstance();
        float initialX = player.getX();
        MovementStrategy strategy = new MoveRightStrategy();
        player.setMovementStrategy(strategy);
        player.performMovement();
        assertEquals(initialX + 50.0, player.getX(), 0);
    }

    @Test
    public void testMoveUp() {
        Player player = Player.getInstance();
        float initialY = player.getY();
        MovementStrategy strategy = new MoveUpStrategy();
        player.setMovementStrategy(strategy);
        player.performMovement();
        assertEquals(initialY - 50.0, player.getY(), 0);
    }

    @Test
    public void testMoveDown() {
        Player player = Player.getInstance();
        float initialY = player.getY();
        MovementStrategy strategy = new MoveDownStrategy();
        player.setMovementStrategy(strategy);
        player.performMovement();
        assertEquals(initialY + 50.0, player.getY(), 0);
    }

    @Test
    public void testSetWall() {
        Wall wall = new Wall(150, 80, 1050, 150);
        wall.setLeft(0);
        wall.setRight(2000);
        wall.setTop(50);
        wall.setBottom(100);

        assertEquals( 0, wall.getLeft(), 0);
        assertEquals(2000, wall.getRight(), 0);
        assertEquals(50, wall.getTop(), 0);
        assertEquals(100, wall.getBottom(), 0);
    }

    @Test
    public void testWallCollide() {
        Wall wall = new Wall(150, 80, 1050, 150);
        assertEquals(true, wall.collidesWith(500, 100));
        assertEquals(false, wall.collidesWith(900, 200));
    }
}
