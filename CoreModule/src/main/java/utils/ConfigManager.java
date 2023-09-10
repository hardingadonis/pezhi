package utils;

import org.apache.logging.log4j.*;
import com.google.gson.*;
import lombok.*;

import java.io.*;
import java.nio.file.*;

@Getter
@Setter
public class ConfigManager {
    static final Logger logger = LogManager.getLogger(ConfigManager.class);

    private static ConfigManager instance = null;

    static {
        instance = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    private Config config;

    public void load() {
        this.config = new Config();

        this.config.setDatabaseLocation(null);

        File configFile = new File("configs.json");

        if (configFile.exists()) {
            try {
                Gson gson = new Gson();
                Reader reader = Files.newBufferedReader(Paths.get("configs.json"));

                this.config = gson.fromJson(reader, Config.class);
                reader.close();
            } catch (IOException | JsonIOException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public void save() {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            Writer writer = Files.newBufferedWriter(Paths.get("configs.json"));

            gson.toJson(this.config, writer);
            writer.close();
        } catch (IOException | JsonIOException ex) {
            logger.error(ex.getMessage());
        }
    }
}