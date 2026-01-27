package controller.impl;

import controller.api.ControllerObserver;
import model.GameConstants;
import model.World;
import model.entities.api.Player;

/**
 * Controller responsible for the behaviour of {@link Player}.
 */
public final class PlayerController implements ControllerObserver {
    private boolean w;
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean space;
    private int jumpRemaining;
    private final World world;
    
    /**
     * Create a {@PlayerController}.
     * 
     * @param player the player instance.
     */
    public PlayerController(final World world) {
        this.w = false;
        this.a = false;
        this.s = false;
        this.d = false;
        this.space = false;
        this.world = world;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        final Player player = world.getPlayer();
        int x = (int) player.getX();
        int y = (int) player.getY();

        final boolean onGround = world.collidesWithSolid(
            x,
            y + 1,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        );
        if (jumpRemaining == 0 && (w || space) && onGround) {
            jumpRemaining = GameConstants.JUMP_HEIGHT;
        }
        if (jumpRemaining > 0) {
            if (world.collidesWithPowerupBlockFromBelow(
                x,
                y,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                jumpRemaining = 0;
            } else {
                final int step = Math.min(GameConstants.JUMP_STEP, jumpRemaining);
                final int targetY = Math.max(y - step, 0);
                if (targetY == 0) {
                    y = 0;
                    jumpRemaining = 0;
                } else if (!world.collidesWithSolid(
                    x,
                    targetY,
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
            final int targetX = Math.max(x - GameConstants.PLAYER_SPEED, 0);
            if (!world.collidesWithSolid(
                targetX,
                y,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                x = targetX;
            }
        }
        if (d) {
            final int targetX = x + GameConstants.PLAYER_SPEED;
            if (!world.collidesWithSolid(
                targetX,
                y,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                x = targetX;
            }
        }
        final boolean groundedAfterMove = world.collidesWithSolid(
            x,
            y + 1,
            GameConstants.PLAYER_WIDTH_TILES,
            GameConstants.PLAYER_HEIGHT_TILES
        );
        if (s && !groundedAfterMove) {
            final int targetY = y + GameConstants.PLAYER_SPEED;
            if (!world.collidesWithSolid(
                x,
                targetY,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                y = targetY;
            }
        }
        if (jumpRemaining == 0 && !groundedAfterMove) {
            final int targetY = y + GameConstants.GRAVITY;
            if (!world.collidesWithSolid(
                x,
                targetY,
                GameConstants.PLAYER_WIDTH_TILES,
                GameConstants.PLAYER_HEIGHT_TILES
            )) {
                y = targetY;
            }
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

}
