package entity.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import deserialization.level.EnemyFactory;
import deserialization.level.EntityData;
import model.entities.api.Enemy;
import model.entities.api.EnemyType;

/**
 * Test class to verify that enemies are correctly created
 * from deserialized EntityData using EnemyFactory.
 */
class EnemyGenerationTest {

    private static final int WALKER_X = 2;
    private static final int WALKER_Y = 4;
    private static final int JUMPER_X = 4;
    private static final int JUMPER_Y = 8;

    /**
     * Test that a Walker enemy is correctly created with
     * the specified coordinates and type.
     */
    @Test
    void testWalkerCreation() {

        final EntityData data = new EntityData() {

            @Override public String getType() { 
                return "walker"; 
            }

            @Override public int getX() { 
                return WALKER_X; 
            }

            @Override public int getY() { 
                return WALKER_Y; 
            }
        };

        final Enemy enemy = EnemyFactory.createEnemy(data);

        assertEquals(EnemyType.WALKER, enemy.getType());
        assertEquals(WALKER_X, enemy.getX());
        assertEquals(WALKER_Y, enemy.getY());
    }

    /**
     * Test that a Jumper enemy is correctly created with
     * the specified coordinates and type.
     */
    @Test
    void testJumperCreation() {

        final EntityData data = new EntityData() {

            @Override public String getType() { 
                return "jumper"; 
            }

            @Override public int getX() { 
                return JUMPER_X; 
            }

            @Override public int getY() { 
                return JUMPER_Y; 
            }
        };

        final Enemy enemy = EnemyFactory.createEnemy(data);

        assertEquals(EnemyType.JUMPER, enemy.getType());
        assertEquals(JUMPER_X, enemy.getX());
        assertEquals(JUMPER_Y, enemy.getY());
    }

}
