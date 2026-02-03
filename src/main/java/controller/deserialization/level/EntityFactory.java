package controller.deserialization.level;

import java.util.ArrayList;
import java.util.List;

import model.entities.api.Enemy;
import model.entities.api.EnemyType;
import model.entities.impl.JumperImpl;
import model.entities.impl.WalkerImpl;
import model.objects.api.WorldObject;
import model.objects.impl.brick.Brick;
import model.objects.impl.brick.BrickCastle;
import model.objects.impl.brick.Plank;
import model.objects.impl.brick.PowerupBlock;
import model.objects.impl.collectable.Coin;
import model.objects.impl.collectable.Powerup;
import model.objects.impl.door.Door;
import model.objects.impl.door.DoorTop;
import model.objects.impl.grass.Dirt;
import model.objects.impl.grass.DirtTop;
import model.objects.impl.grass.FloatingDirtMid;
import model.objects.impl.grass.FloatingDirtRight;
import model.objects.impl.grass.FloatingDirtleLeft;
import model.objects.impl.grass.FloatingGrass;
import model.objects.impl.grass.FloatingGrassLeft;
import model.objects.impl.grass.FloatingGrassRight;
import model.objects.impl.grass.Grass;
import model.objects.impl.grass.GreenGrass;
import model.objects.impl.lava.Lava;
import model.objects.impl.lava.TopLava;
import model.objects.impl.lava.Water;
import model.objects.impl.lava.WaterTop;

/**
 * Factory class responsible for creating game entities from deserialized level data.
 * This class converts raw {@link EntityData} objects into
 * actual game objects that can be used inside the world model.
 */
public final class EntityFactory {
    /**
     * Private constructor.
     */
    private EntityFactory() { 

    }

    /**
     * Creates one or more game objects based on the provided entity data.
     * If the entity does not contain macro information, a single object is generated. Otherwise, 
     * multiple objects may be created depending on the macro configuration.
     * 
     *
     * @param data the deserialized data describing the entity
     * @return a list of game objects generated from the given data
     */
    public static List<WorldObject> create(final EntityData data) {

        final List<WorldObject> entities = new ArrayList<>();

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
     * This method acts as a factory for individual entities, converting 
     * the raw string identifier into the corresponding concrete object.
     *
     * @param type the string identifier representing the entity type
     * @param x the x coordinate of the entity in the level
     * @param y the y coordinate of the entity in the level
     * @return the newly created WorldObject instance
     */
    private static WorldObject createSingle(final String type, final int x, final int y) {
        return switch (type) {
            case "door" -> new Door(x, y);
            case "door_top" -> new DoorTop(x, y);
            case "lava" -> new Lava(x, y);
            case "lava_top" -> new TopLava(x, y);
            case "floating_grass" -> new FloatingGrass(x, y);
            case "floating_grass_left" -> new FloatingGrassLeft(x, y);
            case "floating_grass_right" -> new FloatingGrassRight(x, y);
            case "powerup_block" -> new PowerupBlock(x, y);
            case "powerup" -> new Powerup(x, y);
            case "grass" -> new Grass(x, y);
            case "brick" -> new Brick(x, y);
            case "green_grass" -> new GreenGrass(x, y);
            case "coin_gold" -> new Coin(x, y);
            case "brick_castle" -> new BrickCastle(x, y);
            case "block_planks" -> new Plank(x, y);
            case "dirt_block" -> new Dirt(x, y);
            case "top_dirt_block" -> new DirtTop(x, y);
            case "water" -> new Water(x, y);
            case "water_top" -> new WaterTop(x, y);
            case "floating_dirt_middle" -> new FloatingDirtMid(x, y);
            case "floating_dirt_left" -> new FloatingDirtleLeft(x, y);
            case "floating_dirt_right" -> new FloatingDirtRight(x, y);
            default -> throw new IllegalArgumentException("Tipo entita sconosciuta: " + type);
        };
    }

    /**
     * create the enemies.
     *
     * @param data entity
     * @return the enemy
     */
    public static Enemy createEnemy(final EntityData data) {
        return switch (data.getType()) {
            case "walker" -> new WalkerImpl(data.getX(), data.getY(), EnemyType.WALKER);
            case "jumper" -> new JumperImpl(data.getX(), data.getY(), EnemyType.JUMPER);
            default -> throw new IllegalArgumentException("Tipo nemico sconosciuto: " + data.getType());
        };
    }
}


