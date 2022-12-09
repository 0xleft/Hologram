package hologram.spawners;

import hologram.Hologram;
import hologram.data.LoadedObject;
import hologram.data.SpawnedObject;
import hologram.utils.Vector3;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ObjectSpawner {

    private ArrayList<LoadedObject> loadedObjects;
    private ArrayList<SpawnedObject> spawnedObjects = new ArrayList<>();

    public ObjectSpawner(ArrayList<LoadedObject> loadedObjects) {
        this.loadedObjects = loadedObjects;
    }

    public boolean hasObjectById(String id) {
        if (id == null) {
            return false;
        }

        for (SpawnedObject spawnedObject : spawnedObjects) {
            if (Objects.equals(spawnedObject.getId(), id)) {
                return true;
            }
        }

        return false;
    }

    public int taskListLength() {
        return spawnedObjects.size();
    }

    public void spawnObject(String name, Location location, String id, World world) {
        spawnedObjects.add(new SpawnedObject(id, location, Hologram.getObjectLoader().getObjectByName(name), world));
    }

    public void despawnObject(String id) {
        for (SpawnedObject spawnedObject : spawnedObjects) {

            if (Objects.equals(spawnedObject.getId(), id)) {
                spawnedObject.removeObject();
                spawnedObjects.remove(spawnedObject);
                return;
            }
        }
    }
}
