package deserialization.level;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * MacroData is used when an entity must generate multiple objects
 * or requires size information, such as blocks created through "fill" operations.
 */
@SuppressFBWarnings(value = "UWF_UNWRITTEN_FIELD", justification = "Fields populated by JSON deserialization (Gson)")
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
