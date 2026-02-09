package level;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

import model.GameConstants;
import model.level.LevelBuilder;
import model.level.LevelModel;

/**
 * Tests for building the third level entities and associations.
 */
final class Level3BuilderTest {
    private static final int EXPECTED_DOOR_COUNT = 6;

    /**
     * Verifies that the raw map is loaded into the model and entities are created.
     */
    @Test
    void testLoadMapCreatesEntities() {
        final LevelModel model = new LevelModel();

        LevelBuilder.loadMap(model);

        assertAll(
            () -> assertEquals(model.getRawMap().length, model.getRows()),
            () -> assertEquals(model.getRawMap()[0].length(), model.getCols()),
            () -> assertNotNull(model.getMap()),
            () -> assertFalse(model.getDoors().isEmpty()),
            () -> assertFalse(model.getCoins().isEmpty()),
            () -> assertFalse(model.getTeleporters().isEmpty()),
            () -> assertFalse(model.getPlatforms().isEmpty()),
            () -> assertFalse(model.getBoulders().isEmpty()),
            () -> assertFalse(model.getButtons().isEmpty())
        );
    }

    /**
     * Verifies that teleport and door associations are built for the level.
     */
    @Test
    void testBuildAssociationsCreatesMappings() {
        final LevelModel model = new LevelModel();

        LevelBuilder.loadMap(model);
        LevelBuilder.buildAssociations(model);

        assertAll(
            () -> assertEquals(EXPECTED_DOOR_COUNT, model.getButtonToDoorId().size()),
            () -> assertEquals(EXPECTED_DOOR_COUNT, model.getDoorPosToId().values().stream()
                    .collect(java.util.stream.Collectors.toSet()).size()),
            () -> assertEquals(expectedTeleportSources(), model.getTeleportDestTile().size())
        );
    }

    private static int expectedTeleportSources() {
        return GameConstants.LEVEL3_TELEPORT_SOURCE_1_ROW_INDEXES.length
                + GameConstants.LEVEL3_TELEPORT_SOURCE_2_ROW_INDEXES.length
                + GameConstants.LEVEL3_TELEPORT_SOURCE_3_ROW_INDEXES.length
                + GameConstants.LEVEL3_TELEPORT_SOURCE_4_ROW_INDEXES.length;
    }
}
