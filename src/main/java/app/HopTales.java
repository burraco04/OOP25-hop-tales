package app;

import controller.Controller;
import model.Model;
import view.View;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Entry point for the Hop Tales platformer application.
 */
public final class HopTales {
    private HopTales() {
    }

    /**
     * Launches the Swing application on the event dispatch thread.
     *
     * @param args command line arguments (unused)
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(HopTales::launch);
    }

    /**
     * Builds the MVC components and shows the main window.
     */
    private static void launch() {
        final Model model = new Model();
        final View view = new View();
        final Controller controller = new Controller(model, view);

        final JFrame frame = new JFrame("Hop Tales");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(view);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                controller.stop();
            }
        });

        view.addKeyListener(controller);
        SwingUtilities.invokeLater(view::requestFocusInWindow);

        controller.start();
    }
}
