package hologram.data;

import hologram.Hologram;
import hologram.utils.Utils;
import hologram.utils.Vector3;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SpawnedObject {

    private Random random = new Random();
    private String id;
    private Location location;
    private LoadedObject loadedObject;
    private ArrayList<Block> spawnedBlocks = new ArrayList<>();
    private World world;

    private ArrayList<Location> alreadyPlacedBlockLocations = new ArrayList<>();

    public SpawnedObject(String id, Location location, LoadedObject loadedObject, World world) {
        this.id = id;
        this.location = location;
        this.loadedObject = loadedObject;
        this.world = world;

        spawnObject();
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public LoadedObject getLoadedObject() {
        return loadedObject;
    }

    // spawns object blocks and also their color
    private void spawnObject() {

        double scale = (double) Hologram.getDataHandler().getConfigValueFloat("scale");

        for (Vector3 color : loadedObject.getFaceIndexToColor().keySet()) {

            new BukkitRunnable() {
                @Override
                public void run() {

                    for (Integer faceIndex : loadedObject.getFaceIndexToColor().get(color)) {

                        // we have face index and color


                        ArrayList<Integer> face = loadedObject.getFaces().get(faceIndex);

                        // face has vertex index in it

                        ArrayList<Vector3> vertexes = new ArrayList<>();

                        // -1 because it is saved in 1,2,3,4 format and starts at 1
                        face.forEach(integer -> vertexes.add(loadedObject.getVertecies().get(integer-1)));


                        Vector3 middlePoint = Utils.findMiddlePoint(vertexes);

                        Location faceLocationInWorld = new Location(world
                                , Math.round(location.getX() + middlePoint.getX()*scale)
                                , Math.round(location.getY() + middlePoint.getY()*scale)
                                , Math.round(location.getZ() + middlePoint.getZ()*scale));

                        ArrayList<Location> locationsForBlocks = new ArrayList<>();

                        locationsForBlocks.add(faceLocationInWorld);

                        vertexes.add(middlePoint);

                        // convert from vector 3 to locations
                        Utils.getPointsInsidePolygon(vertexes).forEach(vector3 -> locationsForBlocks.add(new Location(world,
                                location.getX() + vector3.getX()*scale,
                                location.getY() + vector3.getY()*scale,
                                location.getZ() + vector3.getZ()*scale
                        )));


                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Location blockLocationInTheWorld : locationsForBlocks) {
                                    if (!alreadyPlacedBlockLocations.contains(blockLocationInTheWorld)) {
                                        alreadyPlacedBlockLocations.add(blockLocationInTheWorld);

                                        // TODO the most performance intence task is the line bellow because you have to loop over all the colors
                                        Material material = Utils.blockFromColor((int) Math.round(color.getX()*255), (int) Math.round(color.getY()*255), (int) Math.round(color.getZ()*255));

                                        Block block = blockLocationInTheWorld.getBlock();
                                        block.setType(material);
                                        spawnedBlocks.add(block);
                                    }
                                }
                            }
                        }.runTask(Hologram.getInstance());
                    }
                }
            }.runTask(Hologram.getInstance());
        }
    }

    public void removeObject() {
        ArrayList<Block> removedBlocks = new ArrayList<>();

        for (Block block : spawnedBlocks) {
            block.setType(Material.AIR);
            removedBlocks.add(block);
        }

        // to not cause ConcurrentModificationException
        for (Block block : removedBlocks) {
            spawnedBlocks.remove(block);
        }
        Bukkit.getLogger().info("Despawned "+removedBlocks.size()+" blocks on object: "+id);
    }
}
