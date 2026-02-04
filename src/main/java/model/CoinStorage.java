package model;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

//gestisce il salvataggio delle monete raccolte su file
public final class CoinStorage {

    private static final String DIR_NAME = ".fireboywatergirl_save";
    private static final String FILE_NAME = "coins.txt";

    private CoinStorage() {}

    private static Path getSavePath() {
        String home = System.getProperty("user.home");
        Path dir = Paths.get(home, DIR_NAME);
        return dir.resolve(FILE_NAME);
    }

    public static int loadTotalCoins() {
        Path path = getSavePath();
        try {
            if (!Files.exists(path)) return 0;
            String s = Files.readString(path, StandardCharsets.UTF_8).trim();
            if (s.isEmpty()) return 0;
            return Integer.parseInt(s);
        } catch (java.io.IOException | java.lang.NumberFormatException e) {
            // se il file è corrotto o non leggibile, ripartiamo da 0
            return 0;
        }
    }

    public static void saveTotalCoins(int total) {
        Path path = getSavePath();
        try {
            Files.createDirectories(path.getParent());

            // scrittura atomica: scrive su file temp e poi sostituisce
            Path tmp = path.resolveSibling(FILE_NAME + ".tmp");
            Files.writeString(tmp, Integer.toString(total), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            // se fallisce non crashiamo il gioco
            System.err.println("Errore salvataggio monete: " + e.getMessage());
        }
    }

    public static int addCoins(int delta) {
        int total = loadTotalCoins() + delta;
        saveTotalCoins(total);
        return total;
    }
}
