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
        Player player = world.getPlayer();
        if (jumpRemaining == 0 && (w || space) && !player.isFloating()) {
            jumpRemaining = GameConstants.JUMP_HEIGHT;
        }
        if (jumpRemaining > 0) {
            final int step = Math.min(GameConstants.JUMP_STEP, jumpRemaining);
            player.setY(Math.max(((int) player.getY() - step), 0));
            jumpRemaining -= step;
        }
        if (a) {
            player.setX((int) player.getX() - GameConstants.PLAYER_SPEED);
        }
        if (d) {
            player.setX((int) player.getX() + GameConstants.PLAYER_SPEED);
        }
        if (s && player.isFloating()) {
            player.setY(Math.max((int) player.getY() + GameConstants.PLAYER_SPEED, 0));
        }
        if (jumpRemaining == 0) {
            player.setY(Math.min(Math.max((int) player.getY() + GameConstants.GRAVITY, 0), 25));
        }
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
