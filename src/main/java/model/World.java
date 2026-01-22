package model;

import java.util.ArrayList;
import java.util.List;
import model.entity.Entity;



public class World {
    private final List<Entity> entities = new ArrayList<>();

    public void addEntity(final Entity e) {
        entities.add(e);
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
