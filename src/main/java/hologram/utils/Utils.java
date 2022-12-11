package hologram.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;

// static class for utils in this project
public class Utils {

    public static Vector3 Lerp3(Vector3 vector1, Vector3 vector2, double distance) {
        Vector3 finalVector = new Vector3(0,0,0);

        // interpolate for each coordinate
        finalVector.setX((vector1.getX() + distance * (vector2.getX() - vector1.getX())));
        finalVector.setY((vector1.getY() + distance * (vector2.getY() - vector1.getY())));
        finalVector.setZ((vector1.getZ() + distance * (vector2.getZ() - vector1.getZ())));

        return finalVector;
    }

    public static ArrayList<Vector3> getPointsInsidePolygon(ArrayList<Vector3> vertexes) {
        ArrayList<Vector3> points = new ArrayList<>();

        // we are going to use scanline algoritm for this.

        int top = Integer.MIN_VALUE;
        int left = Integer.MAX_VALUE;
        int bottom = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int back = Integer.MAX_VALUE;
        int front = Integer.MIN_VALUE;

        for (Vector3 vertex : vertexes) {
            if (vertex.getX() > right) {
                right = (int) vertex.getX();
            }
            if (vertex.getX() < left) {
                left = (int) vertex.getX();
            }
            if(vertex.getY() > top){
                top = (int) vertex.getY();
            }
            if(vertex.getY() < bottom){
                bottom = (int) vertex.getY();
            }
            if(vertex.getZ() < back){
                back = (int) vertex.getZ();
            }
            if(vertex.getZ() > front){
                front = (int) vertex.getZ();
            }
        }


        // TODO fix the part is that only the middle points between vertexes is detected as being part of the line
        // go front back to front
        for (int z = back; z <= front; z+=1) {

            // go from bottom to top
            for (int y = bottom; y <= top; y+=1) {

                for (int x = left; x <= right; x+=1) {

                    // we form lines and check if the point is on the line
                    for (int i = 0; i < vertexes.size()-1; i++) {

                        Vector3 start = vertexes.get(i);
                        Vector3 finish = vertexes.get(i+1);

                        if (Utils.pointOnLine(start, finish, new Vector3(x, y, z), Integer.MAX_VALUE)) {
                            points.add(new Vector3(x, y, z));
                        }
                    }

                }

            }

        }

        return points;
    }

    public static boolean pointOnLine(Vector3 start, Vector3 finish, Vector3 point, int minDistance) {

        Vector3 lineVector = new Vector3(
                finish.getX() - start.getX(),
                finish.getY() - start.getY(),
                finish.getZ() - start.getZ());

        Vector3 lineNorm = lineVector.norm();

        Vector3 lineSP = new Vector3(
                point.getX() - start.getX(),
                point.getY() - start.getY(),
                point.getZ() - start.getZ()
        );

        double lineDot = lineNorm.getX()*lineSP.getX() + lineNorm.getY()*lineSP.getY() + lineNorm.getZ()*lineSP.getZ();

        Vector3 pointDistance = new Vector3(
                start.getX() + lineDot * lineNorm.getX(),
                start.getY() + lineDot * lineNorm.getY(),
                start.getZ() + lineDot * lineNorm.getZ()
        );

        if (pointDistance.magnitude3D() <= minDistance) {
            return true;
        }

        return false;
    }

    private static boolean linesIntersect2D(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4) {
        // https://math.stackexchange.com/questions/767047/line-segment-intersection

        double end = point1.getX()*(
                point3.getX()*(point2.getY()-point4.getY())
                +point4.getX()*(point3.getY()-point2.getY()))

                +point2.getX()*(point3.getX()*(point4.getY() - point1.getY())
                +point4.getX()*(point1.getY() - point3.getY()));

        double determinator = (point1.getX() - point2.getX())*(point3.getY()-point4.getY())
                +(point4.getX()-point3.getX())*(point1.getY()-point2.getY());

        double x = end/determinator;

        return point1.getX() < x && x < point2.getX() && point3.getX() < x && x < point4.getX();
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
