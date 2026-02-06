package model.objects.impl;

import model.objects.api.WorldObject;

/**
 * Button pad that can be pressed and released.
 */
public final class ButtonPad implements WorldObject {

    private final int x;
    private final int y;
    private final int w;
    private final int h;
    private boolean pressed;

    /**
     * Create a button pad.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     */
    public ButtonPad(final int x, final int y, final int w, final int h) {
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
        return "BUTTON";
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
     * Whether the button is pressed.
     *
     * @return true if pressed
     */
    public boolean isPressed() {
        return pressed;
    }

    /**
     * Press the button.
     */
    public void press() {
        pressed = true;
    }

    /**
     * Release the button.
     */
    public void release() {
        pressed = false;
    }
}
