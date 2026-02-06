package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import controller.impl.PlayerController;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Class responsible for taking the user's input.
 */
@SuppressFBWarnings(value = "EI2", justification = "Controller reference is shared across input handling.")
public final class KeyboardInputManager extends KeyAdapter {
    private final PlayerController playerController;

    /**
     * Create a {@link KeyboardInputManager}.
     *
     * @param playerController instance of the controller responsible for handling the inputs.
     */
    public KeyboardInputManager(final PlayerController playerController) {
        super();
        this.playerController = playerController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                playerController.setW();
                break;
            case KeyEvent.VK_A:
                playerController.setA();
                break;
            case KeyEvent.VK_S:
                playerController.setS();
                break;
            case KeyEvent.VK_D:
                playerController.setD();
                break;
            case KeyEvent.VK_SPACE:
                playerController.setSpace();
                break;
            case KeyEvent.VK_E:
                playerController.enterCastle();
                break;
            default:
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                playerController.negatesW();
                break;
            case KeyEvent.VK_A:
                playerController.negatesA();
                break;
            case KeyEvent.VK_S:
                playerController.negatesS();
                break;
            case KeyEvent.VK_D:
                playerController.negatesD();
                break;
            case KeyEvent.VK_SPACE:
                playerController.negatesSpace();
                break;
            case KeyEvent.VK_E:
                playerController.cancelEnterCastle();
                break;
            default:
                break;
        }
    }

}
