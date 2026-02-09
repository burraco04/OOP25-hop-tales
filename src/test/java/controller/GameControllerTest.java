package controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;

import controller.impl.PlayerController;
import model.World;
import model.entities.impl.PlayerImpl;
import view.api.View;

/**
 * Tests basic GameController flow using a stub view.
 */
class GameControllerTest {

    @Test
    void testGameOverTriggersView() throws Exception {
        final StubView view = new StubView();
        final GameController controller = new GameController(view, 1, "levels/Level1.json");
        controller.stop();

        final World world = getField(controller, "world", World.class);
        final PlayerImpl player = (PlayerImpl) world.getPlayer();
        setField(player, "healthPoints", 0);

        controller.actionPerformed(new ActionEvent(this, 0, "tick"));

        assertTrue(view.gameOverShown);
    }

    @Test
    void testLevelCompletedTriggersView() throws Exception {
        final StubView view = new StubView();
        final GameController controller = new GameController(view, 1, "levels/Level1.json");
        controller.stop();

        final World world = getField(controller, "world", World.class);
        final PlayerImpl player = (PlayerImpl) world.getPlayer();
        setField(player, "healthPoints", 1);

        final PlayerController playerController = getField(controller, "playerController", PlayerController.class);
        setField(playerController, "levelCompleted", true);

        controller.actionPerformed(new ActionEvent(this, 0, "tick"));

        assertTrue(view.levelCompletedShown);
    }

    private static <T> T getField(final Object target, final String name, final Class<T> type) throws Exception {
        final Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return type.cast(field.get(target));
    }

    private static void setField(final Object target, final String name, final Object value) throws Exception {
        final Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
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
