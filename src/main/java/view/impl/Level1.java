package view.impl;

import model.Camera;

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
    private final Camera camera = new Camera();
    private final static int MILLISEC = 16;

    /**
     * carica il livello1.
     *
     * @param levelPath percorso del filejson
     */
    public Level1(final String levelPath) {
        this.world = new World();

        final LevelData data = LevelLoader.load(levelPath);

        for (final EntityData e : data.getEntities()) {
           world.addEntities(EntityFactory.create(e));
        }

        new Timer(MILLISEC, e -> { update(); repaint();}).start();
        
        setBackground(Color.CYAN);
}

//Non c è player 
private void update() {
    /*final var player = world.getPlayer();
    camera.update(
        player.getX(),
        getWidth(),
        world.getLevelWidth()
    );*/
}

/**
 * paint the pannel.
 */
@Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        for (final var entity : world.getEntities()) {
            entity.draw(g, camera.getX(), entity.getX(), entity.getY());
        }
    }
}
