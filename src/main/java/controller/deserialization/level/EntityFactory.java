package controller.deserialization.level;

import model.entity.Entity;
import model.entity.grass.Grass;

public final class EntityFactory {

    public static Entity create(final EntityData data) {

        switch (data.getType()) {
            case "grass":
                return new Grass(data.getX(), data.getY());
            default:
                throw new IllegalArgumentException("Tipo sconosciuto");
        }
        }
}

