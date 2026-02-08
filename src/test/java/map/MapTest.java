package map;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import deserialization.level.EntityData;
import deserialization.level.EntityFactory;
import deserialization.level.LevelData;
import deserialization.level.LevelLoader;
import model.GameConstants;
import model.World;
import model.objects.api.WorldObject;

/**
 * Automated tests for map loading, entity creation and world initialization.
 */
class MapTest {
    private static final String LEVEL_1_PATH = "levels/Level1.json";
    private static final String LEVEL_2_PATH = "levels/Level2.json";
    private static final int SPAWN_PLAYER_Y = 24;
    private static final int SOLID_TILE_Y = 26;
    private static final int EMPTY_TILE_X = 10;
    private static final int EMPTY_TILE_Y = 5;
    private static final int HAZARD_X = 100;
    private static final int HAZARD_Y = 27;
    private static final int CASTLE_X = 191;
    private static final int CASTLE_Y = 20;

    // verifies the correct loading and deserialization of the first level.
    @Test
    void testLoadLevel1Data() {
        final LevelData level = LevelLoader.load(LEVEL_1_PATH);

        assertAll(
            () -> assertNotNull(level),
            () -> assertEquals(1, level.getSpawnPointX()),
            () -> assertEquals(SPAWN_PLAYER_Y, level.getSpawnPointY()),
            () -> assertFalse(level.getEntities().isEmpty()),
            () -> assertFalse(level.getEnemies().isEmpty())
        );
    }

    //verifies that the EntityFactory is able to correctly instantiate all entity types defined in the level configuration files.
    @Test
    void testEntityFactorySupportsAllMapEntities() {
        assertEntityFactoryCoverageForLevel(LEVEL_1_PATH);
        assertEntityFactoryCoverageForLevel(LEVEL_2_PATH);
    }


    //verifies the correctness of collision detection within the game world using the tiles defined in the first level map.
    @Test
    void testWorldCollisionFromLevel1MapTiles() {
        final World world = loadWorldWithEntities(1, LEVEL_1_PATH);

        assertAll(
            () -> assertTrue(world.collidesWithSolid(0, SOLID_TILE_Y, 1, 1)),
            () -> assertFalse(world.collidesWithSolid(EMPTY_TILE_X, EMPTY_TILE_Y, 1, 1)),
            () -> assertTrue(world.collidesWithHazard(HAZARD_X, HAZARD_Y)),
            () -> assertTrue(world.enteringCastle(CASTLE_X, CASTLE_Y, 1, 2))
        );
    }

    //verifies that each World instance is correctly initialized based on the level identifier.
    @Test
    void testWorldMetadataByLevel() {
        final World level1 = new World(1);
        final World level2 = new World(2);

        assertAll(
            () -> assertEquals("levels/Level1.json", level1.getJsonPath()),
            () -> assertEquals("levels/Level2.json", level2.getJsonPath()),
            () -> assertEquals(GameConstants.LEVEL_1_WIDTH, level1.getLevelWidth()),
            () -> assertEquals(GameConstants.LEVEL_2_WIDTH, level2.getLevelWidth())
        );
    }

    private static void assertEntityFactoryCoverageForLevel(final String levelPath) {
        final LevelData levelData = LevelLoader.load(levelPath);
        final List<WorldObject> created = new ArrayList<>();

        for (final EntityData entityData : levelData.getEntities()) {
            assertDoesNotThrow(() -> created.addAll(EntityFactory.create(entityData)));
        }

        assertFalse(created.isEmpty());
    }

    private static World loadWorldWithEntities(final int levelId, final String levelPath) {
        final World world = new World(levelId);
        final LevelData levelData = LevelLoader.load(levelPath);

        for (final EntityData entityData : levelData.getEntities()) {
            world.addEntities(EntityFactory.create(entityData));
        }

        return world;
    }
}
