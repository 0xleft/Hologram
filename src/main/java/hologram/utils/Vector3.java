package hologram.utils;

import org.bukkit.Bukkit;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Vector3 {

    private ArrayList<Double> vector3;

    public Vector3(double x, double y, double z) {
        this.vector3 = new ArrayList<>();
        this.vector3.add(x);
        this.vector3.add(y);
        this.vector3.add(z);
    }

    public Vector3(String vectorString) {
        ArrayList<Double> vector = parseStringToVector(vectorString);

        this.vector3 = new ArrayList<>();
        this.vector3.add(vector.get(0));
        this.vector3.add(vector.get(1));
        this.vector3.add(vector.get(2));
    }

    public static Vector3 getMiddlePoint(ArrayList<Vector3> vectors) {
        if (vectors.size() < 2) {
            throw new RuntimeException("Cannot get middle point between less than two vectors");
        }

        Vector3 finalVector = new Vector3(0, 0, 0);

        for (int i = 0; i < vectors.size()-1; i++) {
            finalVector = vectors.get(i);
            finalVector = Utils.Lerp3(finalVector, vectors.get(i+1), 0.5);
        }

        return finalVector;
    }

    public static Vector3 addVectors(Vector3 vector1, Vector3 vector2) {
        return new Vector3(vector1.getX()+vector2.getX(), vector1.getY()+vector2.getY(), vector1.getZ()+vector2.getZ());
    }

    private ArrayList<Double> parseStringToVector(String vectorString ) {
        if (vectorString == null) {
            throw new RuntimeException("vectorString cannot be null");
        }

        ArrayList<Double> vector = new ArrayList<>();
        for (String value : vectorString.split(" ")) {
            boolean caught = false;
            double temp = 0; // it will get turned into something either way
            try{
                temp = Double.parseDouble(value);
            } catch (NumberFormatException ignored) {
                caught = true;
            }
            if (!caught) {
                vector.add(temp);
            }
        }
        return vector;
    }

    public double magnitude3D() {
        return Math.sqrt(this.vector3.get(0) * vector3.get(0) + vector3.get(1) * vector3.get(1) + vector3.get(2) * vector3.get(2));
    }

    public Vector3 norm() {
        return new Vector3(
                vector3.get(0) / magnitude3D(),
                vector3.get(1) / magnitude3D(),
                vector3.get(2) / magnitude3D()
        );
    }

    public Vector3 round() {
        return new Vector3(
                (double) Math.round(this.vector3.get(0)),
                (double) Math.round(this.vector3.get(1)),
                (double) Math.round(this.vector3.get(2)));
    }

    public double getX() {
        return this.vector3.get(0);
    }

    public double getY() {
        return this.vector3.get(1);
    }

    public double getZ() {
        return this.vector3.get(2);
    }

    public ArrayList<Double> getVector3() {
        return vector3;
    }

    public void setX(double x) {
        this.vector3.set(0, x);
    }

    public void setY(double y) {
        this.vector3.set(1, y);
    }

    public void setZ(double z) {
        this.vector3.set(2, z);
    }
}
