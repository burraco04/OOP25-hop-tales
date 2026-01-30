package model.entities.impl;

import model.entities.api.EnemyType;

public class JumperImpl extends EnemyImpl {

    private static final double SPEED = 50.0;
    private static final double JUMP_HEIGHT = 150.0;
    private boolean goingUp = true;
    private final double baseY;

    public JumperImpl(double x, double y, double width, double height, EnemyType type) {
        super(x, y, width, height, EnemyType.JUMPER);
        this.baseY = y;
    }

    @Override
    public void update(final double deltaSeconds) {
        setX(getX() + SPEED * deltaSeconds);

        if (goingUp) {
            setY(getY() + SPEED * deltaSeconds);
            if (getY() >= baseY + JUMP_HEIGHT) {
                goingUp = false;
            }
        } else {
            setY(getY() - SPEED * deltaSeconds);
            if (getY() <= baseY) {
                setY(baseY);
                goingUp = true;
            }
        }
    }

}
