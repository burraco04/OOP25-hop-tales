package controller.deserialization.level;

import java.util.List;

/**
 * save all the entety.
 */
public class LevelData {
    private String name;
    private int spawnPointX;
    private int spawnPointY;
    private List<EntityData> entities;

    /**
     * name of the level.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * name of the level.
     *
     * @return x
     */
    public int getSpawnPointX() {
        return spawnPointX;
    }

    /**
     * name of the level.
     *
     * @return y
     */
    public int getSpawnPointY() {
        return spawnPointY;
    }

    /**
     * name of the level.
     *
     * @return entitites
     */
    public List<EntityData> getEntities() {
        return entities;
    }
}
