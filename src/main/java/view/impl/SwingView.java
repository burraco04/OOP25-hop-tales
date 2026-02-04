package view.impl;

import javax.swing.JFrame;
import javax.swing.JDialog;

import controller.AudioManager;
import controller.KeyboardInputManager;
import controller.api.ControllerMenu;
import model.GameConstants;
import model.World;
import view.api.View;

/**
 * This class represents the main graphical view of the application.
 */
public class SwingView implements View {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final String MENU_OST_NAME = "MenuOST";
    private final JFrame frame;
    private ControllerMenu controller;

    /**
     * Initializes the main application window and configures basic settings such as size and audio.
     */
    public SwingView() {
        this.frame = new JFrame("PENALDO & PESSI");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(WIDTH, HEIGHT);
        AudioManager.load(MENU_OST_NAME, "/sounds/MenuSoundtrack.wav");
        AudioManager.play(MENU_OST_NAME);
        AudioManager.setMusicVolume(GameConstants.STARTING_VOLUME);
    }

    /**
     * sets the controller for this view.
     *
     * @param controller the menu controller used to manage user interactions
     */
    public void setController(final ControllerMenu controller) {
    this.controller = controller;
    }

    /**
     * show the main menu panel.
     */
    @Override
    public void showMainMenu() {
        this.frame.setContentPane(new Menu(this.controller));
        this.frame.setVisible(true);
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * show the level selection panel.
     */
    @Override
    public void showLevels() {
        this.frame.setContentPane(new ChooseLevelPanel(this.controller));
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * show the shop panel.
     */
    @Override
    public void showShop() {
        this.frame.setContentPane(new Shop(this.controller));
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * show the option panel.
     */
    @Override
    public void showOptions() {
        this.frame.setContentPane(new Options(this.controller));
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * Mostra il livello selezionato.
     *
     * @param world the instance containing all the objects and entities.
     * @param kim object responsible of handling keyboard inputs.
     */
    @Override
    public void showLevel(final World world, final KeyboardInputManager kim) {
        final Level level = new Level(world.getJsonPath(), world, kim);
        this.frame.setContentPane(level);
        this.frame.revalidate();
        this.frame.repaint();
        level.focus();
    }

    /**
     * Mostra la schermata di Game Over.
     */
    @Override
    public void showGameOver() {
        final JDialog dialog = new JDialog(this.frame, "Game Over", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setContentPane(new GameOverPanel(this.controller, dialog::dispose));
        dialog.pack();
        dialog.setLocationRelativeTo(this.frame);
        dialog.setVisible(true);
    }

}
