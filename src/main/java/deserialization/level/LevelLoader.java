package deserialization.level;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

/**
 * Utility class responsible for loading level data from JSON files.
 * This class uses the Gson library to deserialize JSON resources
 * into {@link LevelData} objects that can be used by the game engine.
 */
public final class LevelLoader {

    private static final Gson GSON = new Gson();

    /**
     * private constructor.
     */
    private LevelLoader() {

    }

    /**
     * Loads a level from a JSON resource file and converts it into a LevelData object.
     *
     * @param path the path to the JSON file inside the resources folder
     * @return a LevelData instance containing all the information of the loaded
     */
    public static LevelData load(final String path) {
        final var in = LevelLoader.class.getClassLoader().getResourceAsStream(path);

        if (in == null) {
            throw new IllegalArgumentException("File not found in resources: " + path);
        }
         try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, LevelData.class);
        } catch (final IOException e) {
        throw new UncheckedIOException(e);
        }
    }
}
