package view.impl;

import javax.swing.JFrame;
import javax.swing.JDialog;

import controller.AudioManager;
import controller.KeyboardInputManager;
import controller.api.ControllerMenu;
import model.World;
import view.api.View;

/**
 * make view.
 */
public class SwingView implements View {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final String MENU_OST_NAME = "MenuOST";
    private final JFrame frame;
    private ControllerMenu controller;

    /**
     * swing view.
     */
    public SwingView() {
        this.frame = new JFrame("PENALDO & PESSI");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(WIDTH, HEIGHT);
        AudioManager.load(MENU_OST_NAME, "/sounds/MenuSoundtrack.wav");
        AudioManager.play(MENU_OST_NAME);
        AudioManager.setMusicVolume(0.1f);
    }

    /**
     * set controller.
     *
     * @param controller creo controller
     */
    public void setController(final ControllerMenu controller) {
    this.controller = controller;
    }

    /**
     * mostra il pannello del menu.
     */
    @Override
    public void showMainMenu() {
        this.frame.setContentPane(new Menu(this.controller));
        this.frame.setVisible(true);
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * mostra il pannello dei livelli.
     */
    @Override
    public void showLevels() {
        this.frame.setContentPane(new ChooseLevelPanel(this.controller));
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * mostra il pannello dei livelli.
     */
    @Override
    public void showShop() {
        this.frame.setContentPane(new Shop(this.controller));
        this.frame.revalidate();
        this.frame.repaint();
    }

    /**
     * mostra il pannello dei livelli.
     */
    @Override
    public void showOptions() {
        this.frame.setContentPane(new Options(this.controller));
        this.frame.revalidate();
        this.frame.repaint();
    }


    @Override
    public void showLevel(final World world, final KeyboardInputManager kim) {
        final Level level = new Level(world.getJsonPath(), world, kim);
        this.frame.setContentPane(level);
        this.frame.revalidate();
        this.frame.repaint();
        level.focus(); 
    }

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
