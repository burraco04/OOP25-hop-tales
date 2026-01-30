package model.entities.impl;

import model.entities.api.EnemyType;

public class WalkerImpl extends EnemyImpl{

    private static final double SPEED = 100.0;

    public WalkerImpl(double x, double y, double width, double height, EnemyType type) {
        super(x, y, width, height, EnemyType.WALKER);
    }

    @Override
    public void update(final double deltaSeconds) {
        setX(getX() + SPEED * deltaSeconds);
    }

    
}
