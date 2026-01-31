package view.utils;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import model.GameConstants;

/**
 * create topbar for the button back.
 */
public class TopBarPanel extends JPanel {

private static final long serialVersionUID = 1L;

    /**
     * implements the top bar. 
     *
     * @param back button back
     * @return the pannel where put the button
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
