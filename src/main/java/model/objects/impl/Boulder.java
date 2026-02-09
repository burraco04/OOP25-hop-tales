package model.objects.impl;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.GameConstants;
import model.entities.impl.PlayerImpl;
import model.objects.api.AbstractWorldEntity;
import view.impl.FireboyWatergirlLevel;

/**
 * Boulder entity with simple physics and push interaction.
 */
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "Texture reference is shared and managed by the level renderer."
)
public final class Boulder extends AbstractWorldEntity {

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
        velocityY += GameConstants.LEVEL3_BOULDER_GRAVITY_PER_TICK;
        if (velocityY > GameConstants.LEVEL3_BOULDER_MAX_FALL_SPEED) {
            velocityY = GameConstants.LEVEL3_BOULDER_MAX_FALL_SPEED;
        }

        final int nextY = (int) (getY() + velocityY);

        if (!collides(world, getX(), nextY)) {
            setY(nextY);
        } else {
            velocityY = 0;
        }
    }

    private boolean collides(final FireboyWatergirlLevel world, final int nx, final int ny) {
        return world.isSolidAtPixel(nx + GameConstants.LEVEL3_BOULDER_CORNER_OFFSET_PIXELS,
                        ny + GameConstants.LEVEL3_BOULDER_CORNER_OFFSET_PIXELS, this)
                || world.isSolidAtPixel(nx + getW() - GameConstants.LEVEL3_BOULDER_OPPOSITE_CORNER_OFFSET_PIXELS,
                        ny + GameConstants.LEVEL3_BOULDER_CORNER_OFFSET_PIXELS, this)
                || world.isSolidAtPixel(nx + GameConstants.LEVEL3_BOULDER_CORNER_OFFSET_PIXELS,
                        ny + getH() - GameConstants.LEVEL3_BOULDER_OPPOSITE_CORNER_OFFSET_PIXELS, this)
                || world.isSolidAtPixel(nx + getW() - GameConstants.LEVEL3_BOULDER_OPPOSITE_CORNER_OFFSET_PIXELS,
                        ny + getH() - GameConstants.LEVEL3_BOULDER_OPPOSITE_CORNER_OFFSET_PIXELS, this);
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

        final boolean verticalOverlap =
                playerRect.y + playerRect.height > boulderRect.y + 2
                        && playerRect.y < boulderRect.y + boulderRect.height - 2;
        if (!verticalOverlap) {
            return;
        }

        // Only push when the player is on the side, not on top or bottom.
        if (velocityX > 0) {
            if (playerRect.x + playerRect.width > boulderRect.x + 2) {
                return;
            }
        } else {
            if (playerRect.x < boulderRect.x + boulderRect.width - 2) {
                return;
            }
        }

        final int step = velocityX > 0 ? 1 : -1;
        final int steps = (int) Math.abs(velocityX);

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
