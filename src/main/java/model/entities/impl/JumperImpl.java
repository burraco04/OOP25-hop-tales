package model.entities.impl;

import model.GameConstants;
import model.entities.api.EnemyType;

public class JumperImpl extends EnemyImpl {

    private static final double SPEED = 10.0;
    private static final double JUMP_HEIGHT = 8.0;
    private boolean goingUp = true;
    private final double baseY;

    public JumperImpl(double x, double y, EnemyType type) {
        super(
            x, 
            y, 
            GameConstants.ENEMY_WITDH, 
            GameConstants.ENEMY_HEIGHT, 
            EnemyType.JUMPER
        );
        this.baseY = y;
    }

    @Override
    public void update(final double deltaSeconds) {
        setX(getX() + SPEED * deltaSeconds);

        if (goingUp) {
            setY(getY() - SPEED * deltaSeconds);
            if (getY() <= baseY - JUMP_HEIGHT) {
                goingUp = false;
            }
        } else {
            setY(getY() + SPEED * deltaSeconds);
            if (getY() >= baseY) {
                setY(baseY);
                goingUp = true;
            }
        }
    }

}
