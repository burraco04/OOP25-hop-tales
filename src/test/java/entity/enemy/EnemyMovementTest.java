package entity.enemy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.entities.api.Enemy;
import model.entities.api.EnemyType;
import model.entities.impl.JumperImpl;
import model.entities.impl.WalkerImpl;

/**
 * Test class to check enemy movement behavior.
 * Ensures that:
 * - Walker enemies move horizontally
 * - Jumper enemies move horizontally and eventually jump
 */
class EnemyMovementTest {

    private static final int MOVE_TEST = 50;
    private static final double DELTA = 1.0 / 60.0;
    private static final int WALKER_X = 2;
    private static final int WALKER_Y = 4;
    private static final int JUMPER_X = 4;
    private static final int JUMPER_Y = 8;
    // Small tolerance used for comparing double values to avoid precision errors
    private static final double EPSILON = 0.0001;

    /**
     * Test that a Walker enemy moves horizontally when updated.
     */
    @Test
    void testWalkerMovesHorizontally() {

        final Enemy walker = new WalkerImpl(WALKER_X, WALKER_Y, EnemyType.WALKER);

        final double startX = walker.getX();

        walker.update(DELTA);

        assertTrue(Math.abs(walker.getX() - startX) > EPSILON, "Walker should move horizontally");
    }

    /**
     * Test that a Jumper enemy moves horizontally when updated.
     */
    @Test
    void testJumperMovesHorizontally() {

        final Enemy jumper = new JumperImpl(JUMPER_X, JUMPER_Y, EnemyType.JUMPER);

        final double startX = jumper.getX();

        jumper.update(DELTA);

        assertTrue(Math.abs(jumper.getX() - startX) > EPSILON, "Jumper should move horizontally");
    }

    /**
     * Test that a Jumper enemy eventually jumps after several updates.
     */
    @Test
    void testJumperEventuallyJumps() {

        final Enemy jumper = new JumperImpl(JUMPER_X, JUMPER_Y, EnemyType.JUMPER);

        final double startY = jumper.getY();
        boolean jumped = false;

        for (int i = 0; i < MOVE_TEST; i++) {
            jumper.update(DELTA);
            if (jumper.getY() < startY) {
                jumped = true;
                break;
            }
        }

        assertTrue(jumped, "Jumper should eventually jump");
    }

}
