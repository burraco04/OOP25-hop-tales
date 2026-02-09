package model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import model.ShopModel.SkinId;

/**
 * Tests for CoinStorage persistence and skin flags.
 */
final class CoinStorageTest {
    private static final String USER_HOME_PROPERTY = "user.home";
    private static final String SAVE_DIR = ".fireboywatergirl_save";
    private static final String SAVE_FILE = "coins.txt";
    private static final String FLAG_ON = "1";
    private static final String FLAG_OFF = "0";
    private static final String COINS_VALUE = "42";
    private static final int COINS_VALUE_INT = 42;

    /**
     * Verifies that coins and skin flags are loaded from disk.
     *
     * @throws IOException if the temp file cannot be created
     */
    @Test
    void testLoadCoinsAndSkinsFromFile() throws IOException {
        final Path tempHome = Files.createTempDirectory("fwg_home");
        final String originalHome = System.getProperty(USER_HOME_PROPERTY);
        System.setProperty(USER_HOME_PROPERTY, tempHome.toString());

        try {
            final Path saveFile = prepareSaveFile(tempHome, List.of(COINS_VALUE, FLAG_ON, FLAG_OFF, FLAG_ON));

            CoinStorage.loadTotalCoins();

            assertAll(
                () -> assertEquals(COINS_VALUE_INT, CoinStorage.getCoins()),
                () -> assertTrue(CoinStorage.isSkinPurchased(SkinId.DEFAULT)),
                () -> assertTrue(CoinStorage.isSkinPurchased(SkinId.SHARK)),
                () -> assertFalse(CoinStorage.isSkinPurchased(SkinId.PURPLE)),
                () -> assertTrue(CoinStorage.isSkinPurchased(SkinId.GHOST)),
                () -> assertTrue(Files.exists(saveFile))
            );
        } finally {
            System.setProperty(USER_HOME_PROPERTY, originalHome);
        }
    }

    /**
     * Verifies that marking a skin persists the flag on disk.
     *
     * @throws IOException if the temp file cannot be created
     */
    @Test
    void testMarkSkinPurchasedPersistsFlag() throws IOException {
        final Path tempHome = Files.createTempDirectory("fwg_home");
        final String originalHome = System.getProperty(USER_HOME_PROPERTY);
        System.setProperty(USER_HOME_PROPERTY, tempHome.toString());

        try {
            final Path saveFile = prepareSaveFile(tempHome, List.of(FLAG_OFF, FLAG_OFF, FLAG_OFF, FLAG_OFF));

            CoinStorage.loadTotalCoins();
            CoinStorage.markSkinPurchased(SkinId.SHARK);

            final List<String> lines = Files.readAllLines(saveFile, StandardCharsets.UTF_8);
            assertEquals(FLAG_ON, lines.get(1).trim());
        } finally {
            System.setProperty(USER_HOME_PROPERTY, originalHome);
        }
    }

    private static Path prepareSaveFile(final Path home, final List<String> lines) throws IOException {
        final Path dir = home.resolve(SAVE_DIR);
        Files.createDirectories(dir);
        final Path saveFile = dir.resolve(SAVE_FILE);
        Files.write(saveFile, lines, StandardCharsets.UTF_8);
        return saveFile;
    }
}
