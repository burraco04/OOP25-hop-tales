package controller.deserialization.level;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This class contains all the information necessary to recreate a level,
 * including its name, spawn position, entities and enemies.
 * It is used as an intermediate object during deserialization.
 */
@SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "Fields populated by JSON deserialization (Gson)")
public class LevelData {
    private int spawnPointX;
    private int spawnPointY;
    private List<EntityData> entities;
    private List<EntityData> enemies;

    /**
     * Returns the X coordinate of the player spawn point.
     *
     * @return the x coordinate of the spawn position
     */
    public int getSpawnPointX() {
        return spawnPointX;
    }

    /**
     * Returns the Y coordinate of the player spawn point.
     *
     * @return the y coordinate of the spawn position
     */
    public int getSpawnPointY() {
        return spawnPointY;
    }

    /**
     * Returns the list of entities defined in the level.
     *
     * @return a list of EntityData objects representing level entities
     */
    public List<EntityData> getEntities() {
        return new ArrayList<>(entities);
    }

    /**
     * Returns the list of enemies defined in the level.
     *
     * @return a list of EntityData objects representing enemies
     */
    public List<EntityData> getEnemies() {
        return new ArrayList<>(enemies);
    }
}
