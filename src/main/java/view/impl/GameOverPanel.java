package view.impl;

import controller.api.ControllerMenu;
import controller.api.State;
import view.utils.ButtonFactory;

/**
 * Game over panel.
 */
public final class GameOverPanel extends AbstractResultPanel {
    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 36f;

    /**
     * Create a game over panel.
     *
     * @param controller the menu controller.
     * @param onClose a Runnable to handle the pressing of the button.
     */
    public GameOverPanel(final ControllerMenu controller, final Runnable onClose) {
        super(controller, onClose, "GAME OVER", TITLE_SIZE);
    }
}
