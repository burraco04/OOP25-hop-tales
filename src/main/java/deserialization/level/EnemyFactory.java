package deserialization.level;

import model.entities.api.Enemy;
import model.entities.api.EnemyType;
import model.entities.impl.JumperImpl;
import model.entities.impl.WalkerImpl;

/**
 * Factory responsible for creating Enemy instances.
 */
public final class EnemyFactory {

    /**
     * Constructor for enemyfactory.
     */
    private EnemyFactory() {

    }

    /**
     * Create an Enemy from deserialized EntityData.
     *
     * @param data entity data
     * @return the Enemy created
     */
    public static Enemy createEnemy(final EntityData data) {
        return switch (data.getType()) {
            case "walker" -> new WalkerImpl(
                data.getX(), 
                data.getY(), 
                EnemyType.WALKER
            );
            case "jumper" -> new JumperImpl(
                data.getX(), 
                data.getY(), 
                EnemyType.JUMPER
            );
            default -> throw new IllegalArgumentException("Unknown enemy: " + data.getType());
        };
    }

}
