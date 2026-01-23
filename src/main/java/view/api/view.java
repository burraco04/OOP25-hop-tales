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
 * open options.
 */
void showLevel1(World world, KeyboardInputManager kim);
}
