package model.objects.impl;

import view.impl.FireboyWatergirlLevel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import model.entities.impl.PlayerImpl;
import model.objects.api.AbstractWorldEntity;

/**
 * Boulder entity with simple physics and push interaction.
 */
public final class Boulder extends AbstractWorldEntity {

    private static final double GRAVITY = 0.35;
    private static final double MAX_FALL_SPEED = 10.0;
    private static final int CORNER_OFFSET = 1;
    private static final int OPPOSITE_OFFSET = 2;

    private double velocityY;
    private final BufferedImage tileTexture;
    private final int tileSize;

    /**
     * Creates a boulder.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     * @param tileTexture texture tile
     * @param tileSize tile size
     */
    public Boulder(
            final int x,
            final int y,
            final int w,
            final int h,
            final BufferedImage tileTexture,
            final int tileSize
    ) {
        super(x, y, w, h, "BOULDER");
        this.tileTexture = tileTexture;
        this.tileSize = tileSize;
    }

    /**
     * Returns the current vertical velocity.
     *
     * @return vertical velocity
     */
    public double getVelocityY() {
        return velocityY;
    }

    /**
     * Updates physics for the boulder.
     *
     * @param world game world
     */
    public void updatePhysics(final FireboyWatergirlLevel world) {
        velocityY += GRAVITY;
        if (velocityY > MAX_FALL_SPEED) {
            velocityY = MAX_FALL_SPEED;
        }

        final int nextY = (int) (getY() + velocityY);

        if (!collides(world, getX(), nextY)) {
            setY(nextY);
        } else {
            velocityY = 0;
        }
    }

    private boolean collides(final FireboyWatergirlLevel world, final int nx, final int ny) {
        return world.isSolidAtPixel(nx + CORNER_OFFSET, ny + CORNER_OFFSET, this)
                || world.isSolidAtPixel(nx + getW() - OPPOSITE_OFFSET, ny + CORNER_OFFSET, this)
                || world.isSolidAtPixel(nx + CORNER_OFFSET, ny + getH() - OPPOSITE_OFFSET, this)
                || world.isSolidAtPixel(nx + getW() - OPPOSITE_OFFSET, ny + getH() - OPPOSITE_OFFSET, this);
    }

    /**
     * Attempts to push the boulder by the given player.
     *
     * @param player player instance
     * @param world game world
     */
    public void tryPushBy(final PlayerImpl player, final FireboyWatergirlLevel world) {
        final Rectangle playerRect = new Rectangle(
                (int) Math.round(player.getX()),
                (int) Math.round(player.getY()),
                (int) Math.round(player.getWidth()),
                (int) Math.round(player.getHeight())
        );

        final Rectangle boulderRect = rect();
        if (!playerRect.intersects(boulderRect)) {
            return;
        }

        final double velocityX = player.getVelocityX();
        if (velocityX == 0) {
            return;
        }

        boolean verticalOverlap =
                playerRect.y + playerRect.height > boulderRect.y + 2
                        && playerRect.y < boulderRect.y + boulderRect.height - 2;
        if (!verticalOverlap) {
            return;
        }

        // Only push when the player is on the side, not on top.
        if (velocityX > 0) {
            if (playerRect.x + playerRect.width > boulderRect.x + 2) {
                return;
            }
        } else {
            if (playerRect.x < boulderRect.x + boulderRect.width - 2) {
                return;
            }
        }

        int step = velocityX > 0 ? 1 : -1;
        int steps = (int) Math.abs(velocityX);

        for (int i = 0; i < steps; i++) {
            final int nextX = getX() + step;
            if (!collides(world, nextX, getY())) {
                setX(nextX);
            } else {
                break;
            }
        }
    }

    @Override
    public void draw(final Graphics g) {
        drawTiled(g, tileTexture, tileSize);
    }
}
