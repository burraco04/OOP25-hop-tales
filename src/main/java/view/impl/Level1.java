package view.impl;

import model.Camera;

import java.awt.Color;
import java.awt.Graphics;
import java.util.logging.Level;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.KeyboardInputManager;
import controller.deserialization.level.EntityData;
import controller.deserialization.level.EntityFactory;
import controller.deserialization.level.LevelData;
import controller.deserialization.level.LevelLoader;
import model.World;

public class Level1 extends JPanel {
    private final World world;
    private final Camera camera;
    private final KeyboardInputManager kim;

    public Level1(final String levelPath, final World world, final KeyboardInputManager kim) {
        this.world = world;
        this.kim = kim;
        this.camera = new Camera(world.getLevelWidth(), this.getWidth());
        final LevelData data = LevelLoader.load(levelPath);

        for (final EntityData e : data.getEntities()) {
            world.addEntity(EntityFactory.create(e));
        }

        new Timer(16, e -> { update(); repaint();}).start();
        
        setBackground(Color.CYAN);
        this.addKeyListener(kim);
        
}

//Non c è player 
private void update() {
    final var player = world.getPlayer();
    camera.update(
        (int) player.getX(),
        this.getWidth()
    );
}


@Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        for (final var entity : world.getEntities()) {
            entity.draw(g, camera.getX());
        }
    }

    public void focus() {
        this.requestFocusInWindow();
    }
}
