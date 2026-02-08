package model.objects.api;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Base class for world entities that have a position and size.
 */
public abstract class AbstractWorldEntity implements WorldObject {

    private int x;
    private int y;
    private int w;
    private int h;
    private final String type;

    /**
     * Creates a world entity.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     * @param type entity type
     */
    public AbstractWorldEntity(final int x, final int y, final int w, final int h, final String type) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.type = type;
    }

    @Override
    public final int getX() {
        return x;
    }

    @Override
    public final int getY() {
        return y;
    }

    /**
     * Returns the width of the entity.
     *
     * @return width
     */
    public final int getW() {
        return w;
    }

    /**
     * Returns the height of the entity.
     *
     * @return height
     */
    public final int getH() {
        return h;
    }

    @Override
    public final String getType() {
        return type;
    }

    /**
     * Updates the x coordinate.
     *
     * @param x new x coordinate
     */
    protected final void setX(final int x) {
        this.x = x;
    }

    /**
     * Updates the y coordinate.
     *
     * @param y new y coordinate
     */
    protected final void setY(final int y) {
        this.y = y;
    }

    /**
     * Translates the entity by a delta.
     *
     * @param dx delta x
     * @param dy delta y
     */
    public final void translate(final int dx, final int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Returns the bounding rectangle.
     *
     * @return bounds
     */
    public final Rectangle rect() {
        return new Rectangle(x, y, w, h);
    }

    /**
     * Checks if this entity intersects the given rectangle.
     *
     * @param r rectangle to test
     * @return true if it intersects
     */
    public final boolean intersects(final Rectangle r) {
        return rect().intersects(r);
    }

    /**
     * Checks if this entity contains a point.
     *
     * @param px x coordinate
     * @param py y coordinate
     * @return true if the point is inside
     */
    public final boolean contains(final int px, final int py) {
        return rect().contains(px, py);
    }

    /**
     * Draws a tiled texture in the entity area.
     *
     * @param g graphics context
     * @param tile texture tile
     * @param tileSize tile size
     */
    protected final void drawTiled(final Graphics g, final BufferedImage tile, final int tileSize) {
        if (tile == null) {
            return;
        }

        for (int yy = y; yy < y + h; yy += tileSize) {
            for (int xx = x; xx < x + w; xx += tileSize) {
                final int ww = Math.min(tileSize, (x + w) - xx);
                final int hh = Math.min(tileSize, (y + h) - yy);

                g.drawImage(tile,
                        xx, yy, xx + ww, yy + hh,
                        0, 0, ww, hh,
                        null);
            }
        }
    }

    /**
     * Draws the entity.
     *
     * @param g graphics context
     */
    public abstract void draw(Graphics g);
}
