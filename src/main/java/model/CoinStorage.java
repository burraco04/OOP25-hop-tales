package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.AudioManager;

/**
 * Handles the persistence of collected coins on disk.
 */
public final class CoinStorage {

    private static final String DIR_NAME = ".fireboywatergirl_save";
    private static final String FILE_NAME = "coins.txt";
    private static final Logger LOGGER = Logger.getLogger(CoinStorage.class.getName());
    private static int collectedCoins;

    private CoinStorage() { }

    /**
     * Builds the path for the coins save file.
     *
     * @return the save file path
     */
    private static Path getSavePath() {
        final String home = System.getProperty("user.home");
        final Path dir = Paths.get(home, DIR_NAME);
        return dir.resolve(FILE_NAME);
    }

    /**
     * Loads the total amount of coins from disk into memory.
     */
    public static void loadTotalCoins() {
        final Path path = getSavePath();
        try {
            if (!Files.exists(path)) {
                return;
            }
            final String s = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (s.isEmpty()) {
                return;
            }
            collectedCoins = Integer.parseInt(s);
        } catch (final IOException | NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid coin save data, resetting to 0.", e);
        }
    }

    /**
     * Writes the current total amount of coins to disk.
     */
    public static void saveTotalCoins() {
        final Path path = getSavePath();
        try {
            final Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            // scrittura atomica: scrive su file temp e poi sostituisce
            final Path tmp = path.resolveSibling(FILE_NAME + ".tmp");
            Files.writeString(tmp, Integer.toString(collectedCoins), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (final IOException e) {
            LOGGER.log(Level.WARNING, "Unable to persist coins to disk.", e);
        }
    }

    /**
     * Increments the total coins by the standard coin value and persists.
     */
    public static void collectCoin() {
        collectedCoins += GameConstants.COIN_VALUE;
        AudioManager.play("coin_sound");
        saveTotalCoins();
    }

    /**
     * Returns the total number of collected coins.
     *
     * @return collected coins
     */
    public static int getCoins() {
        return collectedCoins;
    }

    /**
     * Deducts the skin cost from the saved coin total and persists.
     */
    public static void paySkinFromShop() {
        collectedCoins -= GameConstants.SKIN_COST;
        saveTotalCoins();
    }
}
