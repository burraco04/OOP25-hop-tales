package model.objects.impl;

import model.objects.api.WorldObject;

/**
 * Teleporter that can move the player to a target position.
 */
public final class Teleporter implements WorldObject {

    private final int x;
    private final int y;
    private final int w;
    private final int h;

    private int targetX;
    private int targetY;

    /**
     * Create a teleporter.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     */
    public Teleporter(final int x, final int y, final int w, final int h) {
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
        return "TELEPORTER";
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
     * Set the teleport destination.
     *
     * @param tx target x coordinate
     * @param ty target y coordinate
     */
    public void setTarget(final int tx, final int ty) {
        this.targetX = tx;
        this.targetY = ty;
    }

    /**
     * Return the target x coordinate.
     *
     * @return target x
     */
    public int getTargetX() {
        return targetX;
    }

    /**
     * Return the target y coordinate.
     *
     * @return target y
     */
    public int getTargetY() {
        return targetY;
    }
}
