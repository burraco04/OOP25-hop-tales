package model.objects.impl;

import java.awt.Graphics;
import model.objects.api.AbstractWorldEntity;

/**
 * Teleporter entity, rendered as part of the background.
 */
public final class Teleporter extends AbstractWorldEntity {

    /**
     * Creates a teleporter.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     */
    public Teleporter(final int x, final int y, final int w, final int h) {
        super(x, y, w, h, "TELEPORTER");
    }

    @Override
    public void draw(final Graphics g) {
        // Not drawn directly: it is part of the background.
    }
}
