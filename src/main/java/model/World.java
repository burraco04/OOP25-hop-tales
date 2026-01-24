package model;

import java.util.ArrayList;
import java.util.List;

import model.objects.api.Tangible;

/**
 * create class world.
 */
public class World {
    private final List<Tangible> entities = new ArrayList<>();
    private int levelWidth;

    /**
     * add all the entities at the world
     *
     * @param list list of entitties
     */
    public void addEntities(final List<Tangible> list) {
        entities.addAll(list);
    }

    /**
     * return the entities
     *
     * @return the entities
     */
    public List<Tangible> getEntities() {
        return entities;
    }

   public void setLevelSize(int width, int height) {
        this.levelWidth = width;
    }
    
    public int getLevelWidth() {
        return levelWidth;
    }
}
