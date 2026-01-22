package controller.deserialization.level;

import java.util.List;

public class LevelData {
    private String name;
    private int spawnPointX;
    private int spawnPointY;
    private List<EntityData> entities;

    public String getName() {
        return name;
    }

    public int getSpawnPointX() {
        return spawnPointX;
    }

    public int getSpawnPointY() {
        return spawnPointY;
    }

    public List<EntityData> getEntities() {
        return entities;
    }
}
