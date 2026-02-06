package model.objects.impl;

import model.objects.api.WorldObject;

/**
 * Simple physics-driven boulder object.
 */
public final class Boulder implements WorldObject {

    private static final double GRAVITY_STEP = 0.35;
    private static final double MAX_FALL_SPEED = 10.0;
    private int x;
    private int y;
    private final int w;
    private final int h;
    private double vy;

    /**
     * Create a boulder at the given position.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     */
    public Boulder(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /** {@inheritDoc} */
    @Override
    public int getX() {
        return x;
    }

    /** {@inheritDoc} */
    @Override
    public int getY() {
        return y;
    }

    /** {@inheritDoc} */
    @Override
    public String getType() {
        return "BOULDER";
    }

    /**
     * Return the width of the boulder.
     *
     * @return width
     */
    public int getW() {
        return w;
    }

    /**
     * Return the height of the boulder.
     *
     * @return height
     */
    public int getH() {
        return h;
    }

    /**
     * Return the current vertical speed.
     *
     * @return vertical speed
     */
    public double getVy() {
        return vy;
    }

    /**
     * Set the current vertical speed.
     *
     * @param vy new vertical speed
     */
    public void setVy(final double vy) {
        this.vy = vy;
    }

    /**
     * Set the x coordinate.
     *
     * @param x new x coordinate
     */
    public void setX(final int x) {
        this.x = x;
    }

    /**
     * Set the y coordinate.
     *
     * @param y new y coordinate
     */
    public void setY(final int y) {
        this.y = y;
    }

    /**
     * Apply gravity to the boulder.
     */
    public void applyGravity() {
        vy += GRAVITY_STEP;
        if (vy > MAX_FALL_SPEED) {
            vy = MAX_FALL_SPEED;
        }
    }

    /**
     * Apply the current vertical speed to the position.
     */
    public void stepVertical() {
        y = (int) (y + vy);
    }

    /**
     * Stop the vertical movement.
     */
    public void stopVertical() {
        vy = 0;
    }
}
