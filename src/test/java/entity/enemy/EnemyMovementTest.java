package entity.enemy;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.entities.api.Enemy;
import model.entities.api.EnemyType;
import model.entities.impl.JumperImpl;
import model.entities.impl.WalkerImpl;

public class EnemyMovementTest {

    private static final int MAX_UPDATE = 50;      
    private static final double DELTA = 1.0;        
    private static final double EPSILON = 1e-6; //for error using double  

    @Test
    void testWalkerMovement() {
        Enemy walker = new WalkerImpl(0, 0, EnemyType.WALKER);
        double initialX = walker.getX();
        double initialY = walker.getY();

        for (int i = 0; i < MAX_UPDATE; i++) {
            walker.update(DELTA);
        }

        assertTrue(Math.abs(walker.getX() - initialX) > EPSILON, "Walker should move horizontally");
        assertTrue(Math.abs(walker.getY() - initialY) < EPSILON, "Walker should not move vertically");
    }

    @Test
    void testJumperMovement() {
        JumperImpl jumper = new JumperImpl(0, 0, EnemyType.JUMPER);
        double initialX = jumper.getX();
        double initialY = jumper.getY();


        for (int i = 0; i < MAX_UPDATE; i++) {
            jumper.update(DELTA);
        }

        assertTrue(Math.abs(jumper.getX() - initialX) > EPSILON, "Jumper should move horizontally");
        assertTrue(Math.abs(jumper.getY() - initialY) > EPSILON, "Jumper should move vertically (jump)");
    }

}
