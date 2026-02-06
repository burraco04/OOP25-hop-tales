package model.entities.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.Collider;
import model.entities.api.Enemy;
import model.entities.api.EnemyType;

/**
 * Default implementation of an enemy entity.
 */
@SuppressWarnings("PMD.ImmutableField")
public abstract class AbstractEnemyImpl implements Enemy {

    private static final double LEFT_BOUNDARY = 0.0;
    private static final int LEFT_DIRECTION = -1;
    private static final int RIGHT_DIRECTION = 1;
    private int direction = LEFT_DIRECTION;
    private Collider collider;
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
    public abstract void update(double deltaSecond);

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

    /**
     * Get the type of the enemy object.
     *
     * @return the {@EnemyType}.
     */
    @Override
    public EnemyType getType() {
        return type;
    }

    /**
     * Sets the collider for this enemy.
     *
     * @param collider the collider
     */
    @SuppressFBWarnings(value = "EI2", justification = "Collider is shared across world entities by design.")
    public void setCollider(final Collider collider) {
        this.collider = collider;
    }

    /**
     * Checks if the enemy can move to a specific position.
     *
     * @param nextX target x coordinate
     * @param nextY target y coordinate
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
     * @param posX x coordinate of the enemy
     * @param posY y coordinate of the enemy
     * @return true if the enemy is on ground, false otherwise
     */
    protected boolean isOnGround(final double posX, final double posY) {
        return collider == null || collider.collidesWithSolid(
            (int) Math.floor(posX), 
            (int) Math.floor(posY + height), 
            (int) width, 
            1
        );
    }

    /**
     * Return the current direction of the movement of the enemy.
     *
     * @return integr representing the direction of the enemy
     */
    protected int getDirection() {
        return direction;
    }

    /**
     * Reverse the direction of the horizontal movement of the enemy.
     */
    protected void reverseDirection() {
        direction *= -1;
    }

    /**
     * Moves the enemy horizontally, respecting collisions and level boundaries.
     *
     * @param deltaX horizontal displacement
     */
    protected void moveHorizontal(final double deltaX) {

        final double targetX = getX() + deltaX; 
        if (canMoveTo(targetX, getY())) { 
            setX(targetX); 
        } else { 
            reverseDirection(); 
        }

        if (getX() < LEFT_BOUNDARY) { 
            setX(LEFT_BOUNDARY); 
            direction = RIGHT_DIRECTION; 
        } else if (collider != null && getX() > collider.getLevelWidth() - width) { 
            setX(collider.getLevelWidth() - width); 
            direction = LEFT_DIRECTION; 
        }
    }

    /**
     * Applies gravity to the enemy.
     *
     * @param gravityStep amount to move downwards
     */
    protected void applyGravity(final double gravityStep) {

        final double targetY = getY() + gravityStep;
        if (!isOnGround(getX(), getY()) && canMoveTo(getX(), targetY)) {
                setY(targetY);
        }
    }

}
