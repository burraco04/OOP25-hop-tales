package view.api;

import controller.KeyboardInputManager;
import model.World;

/**
 * Represents the graphical user interface of the application.
 * This interface defines the operations used by the controller
 * to display different screens and interact with the user.
 */

public interface View {
/**
 * Displays the main menu screen.
 */
void showMainMenu();

/**
 * Displays the screen used to select a game level.
 */
void showLevels();

/**
 * Opens and displays the game shop screen.
 */
void showShop();

/**
 * Displays the options and settings screen.
 */
void showOptions();

/**
 * Display the first level.
 *
 * @param world the world of the first level.
 * @param kim a {@link KeyboardInputManager} instance.
 */
void showLevel(World world, KeyboardInputManager kim);

/**
 * Show the game over panel.
 */
void showGameOver();

/**
 * Show the level completed panel.
 */
void showLevelCompleted();
void showLevel1(World world, KeyboardInputManager kim);

/**
 * Display the third level.
 */
void showLevel3();
}
