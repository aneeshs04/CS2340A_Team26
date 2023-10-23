package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


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
    public void testCollisionWithWall() {
        Wall wall = new Wall(100, 100, 200, 200);
        Player player = Player.getInstance();
        player.setX(150);
        player.setY(150);

        assertTrue(wall.collidesWith((int) player.getX(), (int) player.getY()));
    }

    @Test
    public void testNoCollisionWithWall() {
        Wall wall = new Wall(100, 100, 200, 200);
        Player player = Player.getInstance();
        player.setX(50);
        player.setY(50);

        assertFalse(wall.collidesWith((int) player.getX(), (int) player.getY()));
    }

}
