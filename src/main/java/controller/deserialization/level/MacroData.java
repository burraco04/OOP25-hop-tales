package controller.deserialization.level;

/**
 * MacroData is used when an entity must generate multiple objects
 * or requires size information, such as blocks created through "fill" operations.
 */
public final class MacroData {

    private String type;
    private int width;
    private int height;

/**
 * Returns the type of macro operation associated with this data.
 *
 * @return the macro type identifier
 */
public String getType() {
    return type; 
}

/**
 * Returns the width value defined for this macro.
 *
 * @return the width associated with the macro
 */
public int getWidth() { 
    return width;
}

/**
 * Returns the height value defined for this macro.
 *
 * @return the height associated with the macro
 */
public int getHeight() {
    return height;
}
}
