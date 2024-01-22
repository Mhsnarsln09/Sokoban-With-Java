
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class GameConfig {
    private static GameConfig instance;
    private Properties config;

    private GameConfig() {
        config = new Properties();
        try {
            config.load(Files.newInputStream(Paths.get("src/config.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameConfig getInstance() {
        if (instance == null) {
            instance = new GameConfig();
        }
        return instance;
    }

    public String getProperty(String key) {
        return config.getProperty(key);
    }
}
