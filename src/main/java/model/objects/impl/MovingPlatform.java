package model.objects.impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.GameConstants;
import model.objects.api.AbstractWorldEntity;

/**
 * Moving platform that can move vertically based on balance state.
 */
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "Texture reference is shared and managed by the level renderer."
)
public final class MovingPlatform extends AbstractWorldEntity {

    private final BufferedImage tileTexture;
    private final int tileSize;

    private final int startY;
    private int targetDy;
    private double speed = GameConstants.LEVEL3_PLATFORM_DEFAULT_SPEED;

    private boolean isLeftSide;

    private int prevX;
    private int prevY;

    /**
     * Creates a moving platform.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param w width
     * @param h height
     * @param tileTexture texture tile
     * @param tileSize tile size
     */
    public MovingPlatform(
            final int x,
            final int y,
            final int w,
            final int h,
            final BufferedImage tileTexture,
            final int tileSize
    ) {
        super(x, y, w, h, "PLATFORM");
        this.tileTexture = tileTexture;
        this.tileSize = tileSize;

        startY = getY();
        prevX = getX();
        prevY = getY();
    }

    /**
     * Configures the platform balance role.
     *
     * @param isLeft true if the platform is on the left side
     * @param dyWhenActive vertical offset when active
     * @param newSpeed movement speed
     */
    public void setBalanceRole(final boolean isLeft, final int dyWhenActive, final double newSpeed) {
        this.isLeftSide = isLeft;
        this.targetDy = dyWhenActive;
        this.speed = newSpeed;
    }

    /**
     * Updates the platform position based on the balance state.
     *
     * @param active true if the balance is active
     */
    public void updateBalance(final boolean active) {
        prevX = getX();
        prevY = getY();

        final int desiredY = active ? (startY + targetDy) : startY;
        int currentY = getY();

        if (currentY < desiredY) {
            currentY += (int) Math.ceil(speed);
        }
        if (currentY > desiredY) {
            currentY -= (int) Math.ceil(speed);
        }

        if (Math.abs(currentY - desiredY) <= 1) {
            currentY = desiredY;
        }

        setY(currentY);
    }

    /**
     * Returns the platform delta x since last update.
     *
     * @return delta x
     */
    public int deltaX() {
        return getX() - prevX;
    }

    /**
     * Returns the platform delta y since last update.
     *
     * @return delta y
     */
    public int deltaY() {
        return getY() - prevY;
    }

    /**
     * Returns whether the platform is on the left side.
     *
     * @return true if left side
     */
    public boolean isLeftSide() {
        return isLeftSide;
    }

    @Override
    public void draw(final Graphics g) {
        drawTiled(g, tileTexture, tileSize);
    }
}
