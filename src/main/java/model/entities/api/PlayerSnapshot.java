package model.entities.api;

/**
 * Immutable description of the player's state used by the view layer.
 */
public final class PlayerSnapshot {
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final boolean facingRight;
    private final boolean onGround;

    /**
     * Builds a new player snapshot.
     *
     * @param x left coordinate
     * @param y top coordinate
     * @param width snapshot width
     * @param height snapshot height
     * @param facingRight direction flag
     * @param onGround ground contact flag
     */
    public PlayerSnapshot(
        final double x,
        final double y,
        final double width,
        final double height,
        final boolean facingRight,
        final boolean onGround
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.facingRight = facingRight;
        this.onGround = onGround;
    }

    /**
     * @return the player's left coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * @return the player's top coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * @return the player's width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return the player's height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * @return {@code true} when the player is facing right.
     */
    public boolean isFacingRight() {
        return facingRight;
    }

    /**
     * @return {@code true} if the player is standing on a surface.
     */
    public boolean isOnGround() {
        return onGround;
    }
}
