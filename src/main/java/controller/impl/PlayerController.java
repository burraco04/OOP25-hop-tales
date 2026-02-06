package controller.impl;

import controller.AudioManager;
import controller.api.ControllerObserver;
import model.GameConstants;
import model.World;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.entities.api.Player;

/**
 * Controller responsible for the behaviour of {@link Player}.
 */
@SuppressFBWarnings(value = "EI2", justification = "World is shared across controllers by design.")
public final class PlayerController implements ControllerObserver {
    private boolean w;
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean space;
    private double jumpRemaining;
    private final World world;
    private boolean enteringCastle;
    private boolean levelCompleted;

    /**
     * Create a {@link PlayerController}.
     *
     * @param world the world instance.
     */
    public PlayerController(final World world) {
        this.w = false;
        this.a = false;
        this.s = false;
        this.d = false;
        this.space = false;
        this.world = world;
        this.enteringCastle = false;
        this.levelCompleted = false;
        AudioManager.load("player_damaged", "/sounds/PlayerDamaged.wav");
        AudioManager.setVolume(AudioManager.getClip("player_damaged"), AudioManager.getMusicVolume());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        final Player player = world.getPlayer();
        double x = player.getX();
        double y = player.getY();
        final int tileX = (int) Math.floor(x);
        final int tileY = (int) Math.floor(y);
        if (this.enteringCastle && world.enteringCastle(
            (int) x,
            (int) y,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        )) {
            this.enteringCastle = false;
            this.levelCompleted = true;
        }

        final boolean onGround = world.collidesWithSolid(
            tileX,
            tileY + 1,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        );
        if (jumpRemaining == 0 && (w || space) && onGround) {
            jumpRemaining = GameConstants.JUMP_HEIGHT;
        }
        if (jumpRemaining > 0) {
            if (world.collidesWithPowerupBlockFromBelow(tileX, tileY)) {
                jumpRemaining = 0;
            } else {
                final double step = Math.min(GameConstants.JUMP_STEP, jumpRemaining);
                final double targetY = Math.max(y - step, 0.0);
                final int targetTileY = (int) Math.floor(targetY);
                if (targetY <= 0.0) {
                    y = 0.0;
                    jumpRemaining = 0;
                } else if (!world.collidesWithSolid(
                    tileX,
                    targetTileY,
                    GameConstants.PLAYER_WIDTH_TILES,
                    GameConstants.PLAYER_HEIGHT_TILES
                )) {
                    y = targetY;
                    jumpRemaining -= step;
                } else {
                    jumpRemaining = 0;
                }
            }
        }
        if (a) {
            final double targetX = Math.max(x - GameConstants.PLAYER_SPEED, 0.0);
            final int targetTileX = (int) Math.floor(targetX);
            if (!world.collidesWithSolid(
                targetTileX,
                tileY,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                x = targetX;
            }
        }
        if (d) {
            final double targetX = x + GameConstants.PLAYER_SPEED;
            final int targetTileX = (int) Math.floor(targetX);
            if (!world.collidesWithSolid(
                targetTileX,
                tileY,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                x = targetX;
            }
        }
        final int updatedTileX = (int) Math.floor(x);
        final int updatedTileY = (int) Math.floor(y);
        final boolean groundedAfterMove = world.collidesWithSolid(
            updatedTileX,
            updatedTileY + 1,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        );
        if (s && !groundedAfterMove) {
            final double targetY = y + GameConstants.PLAYER_SPEED;
            final int targetTileY = (int) Math.floor(targetY);
            if (!world.collidesWithSolid(
                updatedTileX,
                targetTileY,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                y = targetY;
            }
        }
        if (jumpRemaining == 0 && !groundedAfterMove) {
            final double targetY = y + GameConstants.GRAVITY;
            final int targetTileY = (int) Math.floor(targetY);
            if (!world.collidesWithSolid(
                updatedTileX,
                targetTileY,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                y = targetY;
            }
        }
        final int hazardTileX = (int) Math.floor(x);
        final int hazardTileY = (int) Math.floor(y);
        if (world.collidesWithHazard(hazardTileX, hazardTileY)
            || world.collidesWithEnemy(hazardTileX, hazardTileY)) {
            player.applyDamage();
        }
        player.setX(x);
        player.setY(y);
    }

    /**
     * Set variable responsible for the W key true.
     */
    public void setW() {
        this.w = true;
    }

    /**
     * Set variable responsible for the A key true.
     */
    public void setA() {
        this.a = true;
    }

    /**
     * Set variable responsible for the S key true.
     */
    public void setS() {
        this.s = true;
    }

    /**
     * Set variable responsible for the D key true.
     */
    public void setD() {
        this.d = true;
    }

    /**
     * Set variable responsible for the Space key true.
     */
    public void setSpace() {
        this.space = true;
    }

    /**
     * Set variable responsible for the W key false.
     */
    public void negatesW() {
        this.w = false;
    }

    /**
     * Set variable responsible for the A key false.
     */
    public void negatesA() {
        this.a = false;
    }

    /**
     * Set variable responsible for the S key false.
     */
    public void negatesS() {
        this.s = false;
    }

    /**
     * Set variable responsible for the D key false.
     */
    public void negatesD() {
        this.d = false;
    }

    /**
     * Set variable responsible for the Space key false.
     */
    public void negatesSpace() {
        this.space = false;
    }

    /**
     * Set the enteringCastle variable true.
     */
    public void enterCastle() {
        this.enteringCastle = true;
    }

    /**
     * Set the enteringCastle variable false.
     */
    public void cancelEnterCastle() {
        this.enteringCastle = false;
    }

    /**
     * Check whether the level completion has been triggered.
     *
     * @return {@code true} if the level is completed
     */
    public boolean isLevelCompleted() {
        return levelCompleted;
    }

}
