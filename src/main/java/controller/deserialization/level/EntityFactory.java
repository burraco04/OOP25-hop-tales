package controller.deserialization.level;

import java.util.ArrayList;
import java.util.List;

import model.objects.api.Tangible;
import model.objects.impl.brick.Brick;
import model.objects.impl.grass.Grass;
import model.objects.impl.grass.GreenGrass;

/**
 * create the entity.
 */
public final class EntityFactory {

    private EntityFactory() { 

    }

    /**
     * create data. 
     *
     * @param data data
     * @return lista tangibile
     */
    public final static List<Tangible> create(final EntityData data) {
    
     final List<Tangible> entities = new ArrayList<>();

     if (data.getMacro() == null) {
            entities.add(createSingle(data.getType(), data.getX(), data.getY()));
            return entities;
        }
    final MacroData macro = data.getMacro();

        switch (macro.getType()) {
            case "fill" -> {
                final int width = macro.getWidth();
                final int height = macro.getHeight();

                for (int dx = 0; dx < width; dx++) {
                    for (int dy = 0; dy < height; dy++) {
                        entities.add(createSingle(
                                data.getType(),
                                data.getX() + dx,
                                data.getY() + dy
                        ));
                    }
                }
            }
            default -> throw new IllegalArgumentException("Macro sconosciuta: " + macro.getType());
        }

        return entities;
    }

    /**
     * create every single entity.
     *
     * @param type type of entity
     * @param x parameter x
     * @param y parameter y
     * @return the new entety
     */
    private static Tangible createSingle(final String type, final int x, final int y) {
        return switch (type) {
            case "grass" -> new Grass(x, y);
            case "brick" -> new Brick(x, y);
            case "green_grass" -> new GreenGrass(x, y);
            default -> throw new IllegalArgumentException("Tipo entita sconosciuta: ");
        };
    }
}


