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
    private File hologramInfoFile;
    private FileConfiguration hologramInfo;

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
        this.hologramInfoFile = new File(Hologram.getInstance().getDataFolder(), "hologram.yml");

        if (!this.hologramInfoFile.exists()) {
            try {
                this.hologramInfoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.hologramInfo = YamlConfiguration.loadConfiguration(this.hologramInfoFile);

            initDefaults();
            return;
        }
        this.hologramInfo = YamlConfiguration.loadConfiguration(this.hologramInfoFile);
    }

    public FileConfiguration gethologramInfo() {
        return hologramInfo;
    }

    private void initDefaults() {
        // initialize defaults

        Bukkit.getLogger().info("Loaded defaults for: "+defaults.keySet());

        for (String key : defaults.keySet()) {
            sethologramInfo(key, defaults.get(key));
        }
    }

    public void savehologramInfo() {
        try {
            this.hologramInfo.save(this.hologramInfoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sethologramInfo(String name, Object value) {
        hologramInfo.set(name, value);
        savehologramInfo();
    }

    public String getHologramInfoValueString(String path) {
        String temp;
        try {
            temp = (String) hologramInfo.get(path);
        } catch (NullPointerException e ){
            throw new RuntimeException("Cannot get string value");
        }

        return temp;
    }

    public boolean getHologramInfoValueBoolean(String path) {
        boolean temp;
        try {
            temp = (Boolean) hologramInfo.get(path);
        } catch (NullPointerException e ){
            throw new RuntimeException("Cannot get boolean value");
        }

        return temp;
    }

    public Float getHologramInfoValueFloat(String path) {
        Float temp;
        try {
            temp = (Float) hologramInfo.get(path);
        } catch (NullPointerException e ){
            throw new RuntimeException("Cannot get float value");
        }

        return temp;
    }
}
