package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import controller.AudioManager;
import model.ShopModel.SkinId;

/**
 * Handles the persistence of collected coins on disk.
 */
public final class CoinStorage {

    private static final String DIR_NAME = ".fireboywatergirl_save";
    private static final String FILE_NAME = "coins.txt";
    private static final Logger LOGGER = Logger.getLogger(CoinStorage.class.getName());
    private static final int SKIN_FLAGS = 3;
    private static final int SHARK_INDEX = 0;
    private static final int PURPLE_INDEX = 1;
    private static final int GHOST_INDEX = 2;
    private static final String FLAG_ON = "1";
    private static final String FLAG_OFF = "0";
    private static int collectedCoins;
    private static final boolean[] PURCHASED_SKINS = new boolean[SKIN_FLAGS];

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
            final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                return;
            }
            collectedCoins = Integer.parseInt(lines.get(0).trim());
            loadSkinFlags(lines);
        } catch (final IOException | NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid coin save data, resetting to 0.", e);
            collectedCoins = 0;
            clearSkinFlags();
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
            final List<String> lines = new ArrayList<>();
            lines.add(Integer.toString(collectedCoins));
            lines.add(PURCHASED_SKINS[SHARK_INDEX] ? FLAG_ON : FLAG_OFF);
            lines.add(PURCHASED_SKINS[PURPLE_INDEX] ? FLAG_ON : FLAG_OFF);
            lines.add(PURCHASED_SKINS[GHOST_INDEX] ? FLAG_ON : FLAG_OFF);
            Files.write(tmp, lines, StandardCharsets.UTF_8,
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

    /**
     * Checks whether a skin has already been purchased.
     *
     * @param id skin identifier
     * @return {@code true} if purchased, {@code false} otherwise
     */
    public static boolean isSkinPurchased(final SkinId id) {
        if (id == SkinId.DEFAULT) {
            return true;
        }
        final int idx = skinIndex(id);
        return idx >= 0 && PURCHASED_SKINS[idx];
    }

    /**
     * Marks a skin as purchased and persists the information.
     *
     * @param id skin identifier
     */
    public static void markSkinPurchased(final SkinId id) {
        final int idx = skinIndex(id);
        if (idx >= 0) {
            PURCHASED_SKINS[idx] = true;
            saveTotalCoins();
        }
    }

    private static void loadSkinFlags(final List<String> lines) {
        clearSkinFlags();
        if (lines.size() > 1) {
            PURCHASED_SKINS[SHARK_INDEX] = FLAG_ON.equals(lines.get(1).trim());
        }
        if (lines.size() > 2) {
            PURCHASED_SKINS[PURPLE_INDEX] = FLAG_ON.equals(lines.get(2).trim());
        }
        if (lines.size() > 3) {
            PURCHASED_SKINS[GHOST_INDEX] = FLAG_ON.equals(lines.get(3).trim());
        }
    }

    private static void clearSkinFlags() {
        for (int i = 0; i < PURCHASED_SKINS.length; i++) {
            PURCHASED_SKINS[i] = false;
        }
    }

    private static int skinIndex(final SkinId id) {
        return switch (id) {
            case SHARK -> SHARK_INDEX;
            case PURPLE -> PURPLE_INDEX;
            case GHOST -> GHOST_INDEX;
            case DEFAULT -> -1;
        };
    }
}
