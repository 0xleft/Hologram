package hologram.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.*;
import java.util.ArrayList;

public class Utils {

    public static Vector3 Lerp3(Vector3 vector1, Vector3 vector2, double distance) {
        Vector3 finalVector = new Vector3(0,0,0);

        // interpolate for each coordinate
        finalVector.setX((vector1.getX() + distance * (vector2.getX() - vector1.getX())));
        finalVector.setY((vector1.getY() + distance * (vector2.getY() - vector1.getY())));
        finalVector.setZ((vector1.getZ() + distance * (vector2.getZ() - vector1.getZ())));

        return finalVector;
    }

    public static Vector3 findMiddlePoint(ArrayList<Vector3> vectors) {
        Vector3 finalVector = new Vector3(0,0,0);

        if (vectors.size() < 2) {
            throw new RuntimeException("Cannot find midpoint between less than two numbers");
        }

        double sumX = 0;
        double sumY = 0;
        double sumZ = 0;

        for (Vector3 vector : vectors) {
            sumX += vector.getX();
            sumY += vector.getY();
            sumZ += vector.getZ();
        }

        finalVector.setX(sumX/vectors.size());
        finalVector.setY(sumY/vectors.size());
        finalVector.setZ(sumZ/vectors.size());

        return finalVector;
    }

    //https://www.spigotmc.org/threads/easiest-way-of-comparing-rgb-values-to-a-minecraft-block.513312/
    public static Material blockFromColor(int red, int green, int blue) {

        Material material;

        int distance = Integer.MAX_VALUE;
        String closestBlockName = "";

        for (String block : BlockColorData.BlockColorDataMap.keySet()) {

            String colorString = BlockColorData.BlockColorDataMap.get(block);

            // color string to vector
            Vector3 color = new Vector3(colorString);

            int dist = (int) (Math.abs(color.getX() - red) + Math.abs(color.getY() - green) + Math.abs(color.getZ() - blue));
            if (dist < distance) {
                distance = dist;
                closestBlockName = block;
            }
        }

        material = Material.getMaterial((closestBlockName).toUpperCase());

        if (material == null) {
            Bukkit.getLogger().info(closestBlockName);
            return Material.BLACK_CONCRETE;
        }

        return material;
    }
}
