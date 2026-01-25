package view.impl;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.Timer;

import controller.KeyboardInputManager;
import controller.deserialization.level.EntityData;
import controller.deserialization.level.EntityFactory;
import controller.deserialization.level.LevelData;
import controller.deserialization.level.LevelLoader;
import model.Camera;
import model.GameConstants;
import model.World;
import view.utils.Draw;

public class Level1 extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int MILLISEC = 16;
    private static final double DELTA = MILLISEC/1000.0;
    private final World world;
    private Camera camera;
    private final KeyboardInputManager kim;

    public Level1(final String levelPath, final World world, final KeyboardInputManager kim) {
        this.world = world;
        this.kim = kim;
        final LevelData data = LevelLoader.load(levelPath);

        for (final EntityData e : data.getEntities()) {
           world.addEntities(EntityFactory.create(e));
        }

        new Timer(MILLISEC, e -> { update(); repaint();}).start();
        
        setBackground(Color.CYAN);
        this.addKeyListener(kim);
        
    }
 
private void update() {
    //Inizializzare camera solo quando il Jpanel ha
    if(camera == null && getWidth() > 0){
        camera = new Camera(world.getLevelWidth(), getWidth());
    }
    //Aggiorna player con DELTA = 16 millisecondi
    world.getPlayer().update(DELTA);

    //aggiorna camera per seguire player
    if (camera != null) {
        camera.update(
            (int) world.getPlayer().getX(),
            getWidth()
        );
    }
}

/**
 * {@inheritDoc}
 */
@Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        //Se la camera ancora non esiste non disegna niente
        if (camera == null) {
            return;
        }

        for (final var entity : world.getEntities()) {
         final var img = Draw.get(entity.getType());
         g.drawImage(img, entity.getX() * GameConstants.TILE_SIZE, entity.getY() * GameConstants.TILE_SIZE,
          GameConstants.TILE_SIZE, GameConstants.TILE_SIZE, null);
        }
    }

    public void focus() {
        this.requestFocusInWindow();
    }
}
