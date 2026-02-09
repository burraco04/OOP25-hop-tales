package it.unibo.sampleapp;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Manual test launcher for the third level window.
 */
public final class ThirdLevelTest {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;

    private ThirdLevelTest() {
    }

    /**
     * Entry point used to manually launch the third level window.
     *
     * @param args command line args
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame("Third Level");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // frame.add(new FireboyWatergirlLevel());

            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT); // O una dimensione adatta
            frame.setLocationRelativeTo(null); // centra la finestra
            frame.setVisible(true);
        });
    }
}
