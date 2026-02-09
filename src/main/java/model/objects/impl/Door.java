package model.objects.impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.objects.api.AbstractWorldEntity;

/**
 * Door entity that can be opened to allow passage.
 */
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "Texture reference is shared and managed by the level renderer."
)
public final class Door extends AbstractWorldEntity {

    private boolean open;
    private final BufferedImage tileTexture;
    private final int tileSize;

    /**
     * Creates a new door.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     * @param tileTexture texture tile
     * @param tileSize tile size
     */
    public Door(
            final int x,
            final int y,
            final int w,
            final int h,
            final BufferedImage tileTexture,
            final int tileSize
    ) {
        super(x, y, w, h, "DOOR");
        this.tileTexture = tileTexture;
        this.tileSize = tileSize;
    }

    /**
     * Returns whether the door is open.
     *
     * @return true if open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets the door open state.
     *
     * @param open true to open
     */
    public void setOpen(final boolean open) {
        this.open = open;
    }

    @Override
    public void draw(final Graphics g) {
        if (open) {
            return;
        }
        drawTiled(g, tileTexture, tileSize);
    }
}
