package hologram;

import hologram.commands.HologramDespawnCommand;
import hologram.commands.HologramSpawnCommand;
import hologram.data.DataHandler;
import hologram.data.ObjectLoader;
import hologram.spawners.ObjectSpawner;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;

public final class Hologram extends JavaPlugin {

    private static Hologram instance;
    private static DataHandler dataHandler;
    private static ObjectLoader objectLoader;
    private static ObjectSpawner objectSpawner;

    @Override
    public void onEnable() {
        instance = this;

        // data handler
        try {
            dataHandler = new DataHandler();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // object loader
        try {
            objectLoader = new ObjectLoader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        objectSpawner = new ObjectSpawner(objectLoader.getLoadedObjects());

        getCommand("spawnhologram").setExecutor(new HologramSpawnCommand());
        getCommand("despawnhologram").setExecutor(new HologramDespawnCommand());
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static Hologram getInstance() {
        return instance;
    }

    public static ObjectLoader getObjectLoader() {
        return objectLoader;
    }

    public static DataHandler getDataHandler() {
        return dataHandler;
    }

    public static ObjectSpawner getObjectSpawner() {
        return objectSpawner;
    }
}
