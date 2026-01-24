package view.impl;

import javax.swing.JFrame;

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
    private final JFrame frame;
    private ControllerMenu controller;

    /**
     * swing view.
     */
    public SwingView() {
        this.frame = new JFrame("PENALDO & PESSI");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(WIDTH, HEIGHT);
        AudioManager.load("MenuOST", "/sounds/MenuSoundtrack.wav");
        AudioManager.play("MenuOST");
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
    public void showLevel1(final World world, final KeyboardInputManager kim) {
        Level1 l1 = new Level1("Level/Level1.json", world, kim);
        this.frame.setContentPane(l1);
        this.frame.revalidate();
        this.frame.repaint();
        l1.focus(); 
    }

}
