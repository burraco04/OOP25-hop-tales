package controller.deserialization.level;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;


public class LevelLoader {

    private static final Gson GSON = new Gson();
    

    private LevelLoader(){}

    public static LevelData load(final String path) {
        final var in = LevelLoader.class.getClassLoader().getResourceAsStream(path);

        if(in == null){
            throw new IllegalArgumentException("File non trovato in resources: " + path);
        }
         try (var reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            return GSON.fromJson(reader, LevelData.class);
        } catch (Exception e) {
            throw new RuntimeException("Errore nel parsing JSON ");
        }
    }
}
