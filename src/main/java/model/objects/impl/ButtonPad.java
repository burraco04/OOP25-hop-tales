package model.objects.impl;

import java.awt.Graphics;
import model.objects.api.AbstractWorldEntity;

/**
 * Button pad entity, rendered as part of the background.
 */
public final class ButtonPad extends AbstractWorldEntity {

    /**
     * Creates a button pad.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     */
    public ButtonPad(final int x, final int y, final int w, final int h) {
        super(x, y, w, h, "BUTTON");
    }

    /** {@inheritDoc} */
    @Override
    public void draw(final Graphics g) {
        // Not drawn directly: it is part of the background.
    }
}
