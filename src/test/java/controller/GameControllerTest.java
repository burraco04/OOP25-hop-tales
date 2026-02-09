package controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;
import java.util.List;

import controller.impl.PlayerController;
import model.GameConstants;
import model.World;
import model.entities.impl.PlayerImpl;
import model.objects.impl.door.Door;
import view.api.View;

/**
 * Tests basic GameController flow using a stub view.
 */
class GameControllerTest {

    @Test
    void testGameOverTriggersView() {
        final StubView view = new StubView();
        final GameController controller = new GameController(view, 1, "levels/Level1.json");
        controller.stop();

        final World world = controller.getWorldForTest();
        final PlayerImpl player = (PlayerImpl) world.getPlayer();
        reduceHealthToZero(player);

        controller.actionPerformed(new ActionEvent(this, 0, "tick"));

        assertTrue(view.gameOverShown);
    }

    @Test
    void testLevelCompletedTriggersView() {
        final StubView view = new StubView();
        final GameController controller = new GameController(view, 1, "levels/Level1.json");
        controller.stop();

        final World world = controller.getWorldForTest();

        final PlayerController playerController = controller.getPlayerControllerForTest();
        final int doorX = (int) world.getPlayer().getX();
        final int doorY = (int) world.getPlayer().getY();
        world.addEntities(List.of(new Door(doorX, doorY)));
        playerController.enterCastle();

        controller.actionPerformed(new ActionEvent(this, 0, "tick"));

        assertTrue(view.levelCompletedShown);
    }

    private static void reduceHealthToZero(final PlayerImpl player) {
        while (player.isAlive()) {
            player.applyDamage();
            if (player.isAlive()) {
                try {
                    Thread.sleep((long) (GameConstants.DAMAGE_COOLDOWN * 1000f) + 10L);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError("Test interrupted while waiting for damage cooldown.", e);
                }
            }
        }
    }

    private static final class StubView implements View {
        private boolean gameOverShown;
        private boolean levelCompletedShown;

        @Override
        public void showMainMenu() { }

        @Override
        public void showLevels() { }

        @Override
        public void showShop() { }

        @Override
        public void showOptions() { }

        @Override
        public void showLevel(final World world, final KeyboardInputManager kim) { }

        @Override
        public void showGameOver() {
            gameOverShown = true;
        }

        @Override
        public void showLevelCompleted() {
            levelCompletedShown = true;
        }

        @Override
        public void showLevel3() { }
    }
}
