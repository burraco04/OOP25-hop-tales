package view.impl;

import controller.api.ControllerMenu;
import view.api.AbstractResultPanel;

/**
 * Level completed panel.
 */
public final class LevelCompletedPanel extends AbstractResultPanel {
    private static final long serialVersionUID = 1L;
    private static final float TITLE_SIZE = 32f;

    /**
     * Create a level completed panel.
     *
     * @param controller the menu controller.
     * @param onClose action to close the dialog.
     */
    public LevelCompletedPanel(final ControllerMenu controller, final Runnable onClose) {
        super(controller, onClose, "LEVEL COMPLETED", TITLE_SIZE);
    }
}
