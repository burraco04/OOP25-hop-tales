package model.objects.impl;

import model.objects.api.WorldObject;

/**
 * Moving platform that can shift vertically.
 */
public final class MovingPlatform implements WorldObject {

    private final int x;
    private int y;
    private final int w;
    private final int h;

    private final int startY;
    private int targetDy;
    private double speed = 1.0;

    private boolean isLeftSide;

    private int prevX;
    private int prevY;

    /**
     * Create a moving platform.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     */
    public MovingPlatform(final int x, final int y, final int w, final int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.startY = y;
        this.prevX = x;
        this.prevY = y;
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
        return "PLATFORM";
    }

    /**
     * Return the width.
     *
     * @return width
     */
    public int getW() {
        return w;
    }

    /**
     * Return the height.
     *
     * @return height
     */
    public int getH() {
        return h;
    }

    /**
     * Configure the balancing role.
     *
     * @param isLeft whether this platform is on the left side
     * @param dyWhenActive vertical delta when active
     * @param speedValue movement speed
     */
    public void setBalanceRole(final boolean isLeft, final int dyWhenActive, final double speedValue) {
        this.isLeftSide = isLeft;
        this.targetDy = dyWhenActive;
        this.speed = speedValue;
    }

    /**
     * Return whether the platform is on the left side.
     *
     * @return true if left side
     */
    public boolean isLeftSide() {
        return isLeftSide;
    }

    /**
     * Update the vertical balance position.
     *
     * @param active whether the balancing trigger is active
     */
    public void updateBalance(final boolean active) {
        prevX = x;
        prevY = y;

        final int desiredY = active ? (startY + targetDy) : startY;
        final int step = (int) Math.ceil(speed);

        if (y < desiredY) {
            y += step;
        }
        if (y > desiredY) {
            y -= step;
        }

        if (Math.abs(y - desiredY) <= 1) {
            y = desiredY;
        }
    }

    /**
     * Return the horizontal delta since last update.
     *
     * @return delta x
     */
    public int deltaX() {
        return x - prevX;
    }

    /**
     * Return the vertical delta since last update.
     *
     * @return delta y
     */
    public int deltaY() {
        return y - prevY;
    }
}
