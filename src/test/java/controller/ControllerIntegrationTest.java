package controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

import controller.impl.CollectablesController;
import controller.impl.EnemyController;
import controller.impl.PlayerController;
import model.World;

/**
 * Smoke tests for the main controllers.
 */
class ControllerIntegrationTest {

    @Test
    void testControllersUpdateWithoutErrors() {
        final World world = new World(1);
        final PlayerController playerController = new PlayerController(world);
        final EnemyController enemyController = new EnemyController(world);
        final CollectablesController collectablesController = new CollectablesController(world);

        assertDoesNotThrow(playerController::update);
        assertDoesNotThrow(enemyController::update);
        assertDoesNotThrow(collectablesController::update);
    }
}
