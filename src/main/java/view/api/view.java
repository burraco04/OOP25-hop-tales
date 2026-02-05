package view.api;

import controller.KeyboardInputManager;
import model.World;

/**
 * boh.
 */

public interface View {
/**
 * manda al menu.
 */
void showMainMenu();

/**
 * scegli il livello.
 */
void showLevels();

/**
 * open shop.
 */
void showShop();

/**
 * open options.
 */
void showOptions();

/**
 * Display the first level.
 *
 * @param world the world of the first level.
 * @param kim a {@link KeyboardInputManager} instance.
 */
void showLevel1(World world, KeyboardInputManager kim);

/**
 * Display the third level.
 */
void showLevel3();
}
