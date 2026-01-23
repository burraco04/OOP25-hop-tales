package model.entities.api;

/**
 * Immutable description of an enemy's state used by the view layer.
 */
public final class EnemySnapshot {

    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final EnemyType type;
    private final boolean alive;

    /**
     * Builds a new enemy snapshot.
     *
     * @param x left coordinate
     * @param y top coordinate
     * @param width snapshot width
     * @param height snapshot height
     * @param type enemy type
     * @param alive {@code true} if the enemy is alive
     */
    public EnemySnapshot(
        final double x,
        final double y,
        final double width,
        final double height,
        final EnemyType type,
        final boolean alive
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.alive = alive;
    }

    /**
     * @return the enemy's left coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * @return the enemy's top coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * @return the enemy's width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the enemy's height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return the enemy type.
     */
    public EnemyType getType() {
        return type;
    }

    /**
     * @return {@code true} when the enemy is alive.
     */
    public boolean isAlive() {
        return alive;
    }
}
