package controller.deserialization.level;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

/**
 * change the file json in gson.
 */
public class LevelLoader {

    private static final Gson GSON = new Gson();
    
    private LevelLoader() {

    }

    /**
     * load the files json.
     *
     * @param path of the files
     * @return the GSON
     */
    public static final LevelData load(final String path) {
        final var in = LevelLoader.class.getClassLoader().getResourceAsStream(path);

        if (in == null) {
            throw new IllegalArgumentException("File non trovato in resources: " + path);
        }
         try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, LevelData.class);
        } catch (final Exception e) {
            throw new RuntimeException("Errore nel parsing JSON ");
        }
    }
}
