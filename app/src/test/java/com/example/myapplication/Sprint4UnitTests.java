import org.junit.Test;
import static org.junit.Assert.*;

import com.example.myapplication.model.BigDemon;
import com.example.myapplication.model.ChortDemon;
import com.example.myapplication.model.BigDemonFactory;
import com.example.myapplication.model.ChortFactory;
import com.example.myapplication.model.Enemy;
import com.example.myapplication.model.EnemyFactory;
import com.example.myapplication.model.Player;

public class Sprint4UnitTests {

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
