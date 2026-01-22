package model;

import java.util.ArrayList;
import java.util.List;

import model.entity.Entity;



public class World {
    private final List<Entity> entities = new ArrayList<>();

    //Serve anche il player per la camera

    private int levelWidth;

    public void addEntity(final Entity e) {
        entities.add(e);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    // setter della dimensione del livello (da JSON)
    public void setLevelSize(int width, int height) {
        this.levelWidth = width;
    }

    // getter larghezza livello
    public int getLevelWidth() {
        return levelWidth;
    }
}
