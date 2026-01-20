package controller.impl;

import controller.api.ControllerObserver;
import model.entities.api.Player;
import model.entities.impl.PlayerImpl;
import java.lang.Math;

/**
 * Controller responsible for the behaviour of {@link Player}.
 */
public final class PlayerController implements ControllerObserver {
    private static final int HEIGHT = 420;
    private static final int WIDTH = 240;
    private static final int PLAYER_SPEED = 3;
    private static final int GRAVITY = 1;    
    private boolean w;
    private boolean a;
    private boolean s;
    private boolean d;
    private boolean space;
    private final Player player;
    
    /**
     * Create a {@PlayerController}.
     */
    public PlayerController() {
        this.w = false;
        this.a = false;
        this.s = false;
        this.d = false;
        this.space = false;
        this.player = new PlayerImpl(0, 0, WIDTH, HEIGHT);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        if ((w || space) && !player.isFloating()) {
            player.setY(Math.max(((int) player.getY() + PLAYER_SPEED), 0));
        }
        if (a) {
            player.setX((int) player.getX() - PLAYER_SPEED);
        }
        if (d) {
            player.setX((int) player.getX() + PLAYER_SPEED);
        }
        if (s && player.isFloating()) {
            player.setY(Math.max(((int) player.getY() - PLAYER_SPEED), 0));
        }
        player.setY(Math.max(((int) player.getY() - GRAVITY), 0));
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
