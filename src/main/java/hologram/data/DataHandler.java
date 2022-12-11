package hologram.data;

import hologram.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DataHandler {

    private static final HashMap<String, String> defaults = new HashMap<>();

    static {
        // DEFAULT CONFIG VALUES

        defaults.put("scale", "5.0");
    }

    private static DataHandler ourInstance;
    private File configFile;
    private FileConfiguration config;
    private File saveFile;
    private FileConfiguration saveConfiguration;


    static {
        try {
            ourInstance = new DataHandler();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataHandler getInstance() {
        return ourInstance;
    }

    public DataHandler() throws IOException {
        if (!Hologram.getInstance().getDataFolder().exists()){
            new File(Hologram.getInstance().getDataFolder().toURI()).mkdir();
        }

        // initialize usefull files
        initConfigFile();
        initSaveFile();
    }

    private void initConfigFile() {
        this.configFile = new File(Hologram.getInstance().getDataFolder(), "config.yml");

        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.config = YamlConfiguration.loadConfiguration(this.configFile);

            initDefaults();
            return;
        }
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    private void initSaveFile() {


        this.saveFile = new File(Hologram.getInstance().getDataFolder(), "config.yml");

        if (!this.saveFile.exists()) {
            try {
                this.saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        this.saveConfiguration = YamlConfiguration.loadConfiguration(this.saveFile);
    }

    // TODO make loading for save file and make saving for spawned objects objects.

    private FileConfiguration getconfig() {
        return config;
    }

    private void initDefaults() {
        // initialize defaults

        Bukkit.getLogger().info("Loaded defaults for: "+defaults.keySet());

        for (String key : defaults.keySet()) {
            setConfig(key, defaults.get(key));
        }
    }

    public void saveConfig() {

        try {
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConfig(String name, Object value) {
        config.set(name, value);
        saveConfig();
    }

    public String getconfigValueString(String path) {
        String temp;
        try {
            temp = (String) config.get(path);
        } catch (NullPointerException e ){
            throw new RuntimeException("Cannot get string value");
        }

        return temp;
    }

    public boolean getConfigValueBoolean(String path) {
        boolean temp;
        try {
            temp = (Boolean) config.get(path);
        } catch (NullPointerException e ){
            throw new RuntimeException("Cannot get boolean value");
        }

        return temp;
    }

    public Float getConfigValueFloat(String path) {
        Float temp;
        try {
            temp = Float.parseFloat((String) config.get(path));
        } catch (NullPointerException e ){
            throw new RuntimeException("Cannot get float value");
        }

        return temp;
    }
}
