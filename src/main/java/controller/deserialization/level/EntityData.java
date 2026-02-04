package controller.deserialization.level;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Represents the data of a single entity loaded from a level file.
 * This class is used during deserialization to transfer raw information
 * about an entity before it is converted into a game object.
 */
@SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "Fields populated by JSON deserialization (Gson)")
public class EntityData {

    private String type;
    private int x;
    private int y;
    private MacroData macro;

    /**
     * Returns the type of the entity.
     *
     * @return the string identifier representing the entity type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the x coordinate of the entity in the level.
     *
     * @return the x position of the entity
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the entity in the level.
     *
     * @return the y position of the entity
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the macro data associated with this entity, if present.
     * Macro data is used to store additional configuration for complex entities.
     *
     * @return the MacroData object linked to this entity, or null if not defined
     */
    public MacroData getMacro() {
        return macro;
    }
}
