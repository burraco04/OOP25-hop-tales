package view.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.GameConstants;

/**
 * Creates a top bar containing a back button.
 */
public class TopBarPanel extends JPanel {

private static final long serialVersionUID = 1L;

    /**
     * Builds the top bar panel.
     *
     * @param back the back button
     * @return the pannel containing the back button
     */
    public JPanel buildTopPanel(final JButton back) {
        final JPanel topBar = new JPanel(new BorderLayout());

        topBar.add(back, BorderLayout.EAST);
        final int h = back.getPreferredSize().height;
        topBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        topBar.setBackground(GameConstants.BACK_COLOR);

        return topBar;
    }
}
