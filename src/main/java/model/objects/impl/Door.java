package model.objects.impl;

import model.objects.api.WorldObject;

/**
 * Door that can be opened or closed.
 */
public final class Door implements WorldObject {

    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private boolean open;

    /**
     * Create a door.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     */
    public Door(final int x, final int y, final int w, final int h) {
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
        return "DOOR";
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
     * Check whether the door is open.
     *
     * @return true if open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Open the door.
     */
    public void open() {
        open = true;
    }

    /**
     * Close the door.
     */
    public void close() {
        open = false;
    }
}
