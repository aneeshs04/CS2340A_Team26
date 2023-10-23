package com.example.myapplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testPlayerWin() {
        Player player = Player.getInstance();
        player.setWon(true);

        assertTrue(player.getWon());
    }

    @Test
    public void testCollisionOnLeftEdge() {
        Wall wall = new Wall(100, 100, 200, 200);
        Player player = Player.getInstance();
        player.setX(100);
        player.setY(150);

        assertFalse(wall.collidesWith((int) player.getX(), (int) player.getY()));
    }

    @Test
    public void testCollisionOnTopEdge() {
        Wall wall = new Wall(100, 100, 200, 200);
        Player player = Player.getInstance();
        player.setX(150);
        player.setY(100);

        assertFalse(wall.collidesWith((int) player.getX(), (int) player.getY()));
    }

    @Test
    public void testCollisionOnRightEdge() {
        Wall wall = new Wall(100, 100, 200, 200);
        Player player = Player.getInstance();
        player.setX(200);
        player.setY(150);

        assertFalse(wall.collidesWith((int) player.getX(), (int) player.getY()));
    }

    @Test
    public void testCollisionOnBottomEdge() {
        Wall wall = new Wall(100, 100, 200, 200);
        Player player = Player.getInstance();
        player.setX(150);
        player.setY(200);

        assertFalse(wall.collidesWith((int) player.getX(), (int) player.getY()));
    }

    @Test
    public void testWallCollisionInside() {
        Wall wall = new Wall(10, 10, 20, 20);
        assertTrue(wall.collidesWith(15, 15));
    }

    @Test
    public void testWallCollisionOutsideLeft() {
        Wall wall = new Wall(10, 10, 20, 20);
        assertFalse(wall.collidesWith(5, 15));
    }
}
