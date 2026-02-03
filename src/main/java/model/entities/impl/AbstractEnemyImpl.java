package model.entities.impl;

import model.Collider;
import model.GameConstants;
import model.entities.api.Enemy;
import model.entities.api.EnemySnapshot;
import model.entities.api.EnemyType;

/**
 * Default implementation of an enemy entity.
 */
public abstract class AbstractEnemyImpl implements Enemy {

    protected int direction = 1;
    protected Collider collider;
    private final double width;
    private final double height;
    private final EnemyType type;
    private double x;
    private double y;
    private boolean alive = true;

    /**
     * Creates an enemy with the provided initial state.
     *
     * @param x starting left coordinate
     * @param y starting top coordinate
     * @param width enemy width
     * @param height enemy height
     * @param type enemy type
     */
    public AbstractEnemyImpl(final double x, final double y, final double width, final double height, final EnemyType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    public abstract void update(final double deltaSecond);

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
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setY(final double y) {
        this.y = y;
    }

    @Override
    public EnemyType getType() {
        return type;
    }


    /**
     * Sets the collider for this enemy.
     *
     * @param collider the collider to assign
     */
    public void setCollider(final Collider collider) {
        this.collider = collider;
    }

    /**
     * Checks if the enemy can move to a specific position.
     *
     * @param nextX target x-coordinate
     * @param nextY target y-coordinate
     * @return true if the enemy can move there, false otherwise
     */
    protected boolean canMoveTo(final double nextX, final double nextY) {
        if (collider == null) {
            return true;
        }
        final int tileX = (int) Math.floor(nextX);
        final int tileY = (int) Math.floor(nextY);
        return !collider.collidesWithSolid(
            tileX,
            tileY,
            (int) width,
            (int) height
        );
    }

    /**
     * Checks if the enemy is standing on solid ground.
     *
     * @param x x-coordinate of the enemy
     * @param y y-coordinate of the enemy
     * @return true if the enemy is on ground, false otherwise
     */
    protected boolean isOnGround(final double x, final double y) {
        if (collider == null) {
            return false;
        }
        final int tileX = (int) Math.floor(x);
        final int tileY = (int) Math.floor(y);
        return collider.collidesWithSolid(
            tileX,
            tileY + 1,
            (int) width,
            (int) height
        );
    }

    /**
     * Return the current direction of the movement of the enemy.
     */
    protected int getDirection() {
    return direction;
    }

    protected void reverseDirection() {
        direction *= -1;
    }

    /**
     * Moves the enemy horizontally, respecting collisions and level boundaries.
     *
     * @param deltaX horizontal displacement
     */
    protected void moveHorizontal(double deltaX) {
        double x = getX();
        double targetX = x + deltaX;
        if (canMoveTo(targetX, getY())) {
            setX(targetX);
        } else {
            reverseDirection();
        }

        // Level boundaries
        if (x < 0.0) { setX(0.0); direction = 1; }
        else if (x > GameConstants.LEVEL_1_WIDTH - width) { 
            setX(GameConstants.LEVEL_1_WIDTH - width); 
            direction = -1; 
        }
    }

    /**
     * Applies gravity to the enemy.
     *
     * @param gravityStep amount to move downwards
     */
    protected void applyGravity(double gravityStep) {
        double y = getY();
        if (!isOnGround(getX(), y)) {
            double targetY = y + gravityStep;
            if (canMoveTo(getX(), targetY)) {
                setY(targetY);
            }
        }
    }

}
