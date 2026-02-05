package it.unibo.sampleapp;

import javax.swing.JFrame;

//import game.FireboyWatergirlLevel;

public class ThirdLevelTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Third Level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //frame.add(new FireboyWatergirlLevel());

        frame.setSize(800, 600); // O una dimensione adatta
        frame.setLocationRelativeTo(null); // centra la finestra
        frame.setVisible(true);
    }
}

