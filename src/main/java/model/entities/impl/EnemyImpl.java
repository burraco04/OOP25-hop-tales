package model.entities.impl;

import model.entities.api.Enemy;
import model.entities.api.EnemySnapshot;
import model.entities.api.EnemyType;

/**
 * Default implementation of an enemy entity.
 */
public final class EnemyImpl implements Enemy {

    private static final double DEFAULT_SPEED = 100.0;

    private final double width;
    private final double height;
    private final EnemyType type;

    private double x;
    private double y;
    private boolean alive = true;
    private double vx = DEFAULT_SPEED;


    /**
     * Creates an enemy with the provided initial state.
     *
     * @param x starting left coordinate
     * @param y starting top coordinate
     * @param width enemy width
     * @param height enemy height
     * @param type enemy type
     */
    public EnemyImpl(final double x, final double y, final double width, final double height, final EnemyType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }


    /** {@inheritDoc} */
    @Override
    public void update(double deltaSecond) {
        if (!alive) {
            return;
        }
        x += vx * deltaSecond;
    }

    /** {@inheritDoc} */
    @Override
    public double getX() { 
        return x; 
    }

    /** {@inheritDoc} */
    @Override
    public double getY() { 
        return y; 
    }

    /** {@inheritDoc} */
    @Override
    public double getWidth() { 
        return width; 
    }

    /** {@inheritDoc} */
    @Override
    public double getHeight() { 
        return height; 
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAlive() { 
        return alive; 
    }

    /** {@inheritDoc} */
    @Override
    public EnemySnapshot getSnapshot() {
        return new EnemySnapshot(x, y, width, height, type, alive);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFloating() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isFloating'");
    }


    
}
