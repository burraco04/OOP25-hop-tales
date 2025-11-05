package view.impl;

import javax.swing.JFrame;

import view.api.view;

public class SwingView implements view {

    private final JFrame frame;

    public SwingView() {
        this.frame = new JFrame("HOP TALES");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(800, 600);
       
    }

    @Override
    public void showMainMenu() {
        this.frame.setContentPane(new Menu(null, "gioca"));
        this.frame.setVisible(true);
       
    }
}
