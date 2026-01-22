package view.impl;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.deserialization.level.EntityData;
import controller.deserialization.level.EntityFactory;
import controller.deserialization.level.LevelData;
import controller.deserialization.level.LevelLoader;
import model.World;

public class Level1 extends JPanel {
    private final World world;

    public Level1(final String levelPath) {
        this.world = new World();

        final LevelData data = LevelLoader.load(levelPath);

        for (final EntityData e : data.getEntities()) {
            world.addEntity(EntityFactory.create(e));
        }

        new Timer(16, e -> repaint()).start();
        
        setBackground(Color.CYAN);
}

@Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        for (final var entity : world.getEntities()) {
            entity.draw(g);
        }
    }
}
