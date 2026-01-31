package model.entities.impl;

import model.GameConstants;
import model.entities.api.EnemyType;

public class WalkerImpl extends EnemyImpl{

    private static final double SPEED = 100.0;

    public WalkerImpl(double x, double y, EnemyType type) {
        super(
            x, 
            y, 
            GameConstants.ENEMY_WITDH, 
            GameConstants.ENEMY_HEIGHT, 
            EnemyType.WALKER
        );
    }

    @Override
    public void update(final double deltaSeconds) {
        setX(getX() + SPEED * deltaSeconds);
    }

    
}
