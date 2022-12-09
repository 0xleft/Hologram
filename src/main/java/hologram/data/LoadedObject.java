package hologram.data;

import hologram.utils.Vector3;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadedObject {

    private ArrayList<Vector3> vertecies;
    private String name;
    private ArrayList<ArrayList<Integer>> faces;
    private HashMap<Vector3, ArrayList<Integer>> faceIndexToColor;

    public LoadedObject(String name, ArrayList<Vector3> vertecies, ArrayList<ArrayList<Integer>> faces, HashMap<Vector3, ArrayList<Integer>> faceIndexToColor) {
        this.vertecies = vertecies;
        this.name = name;
        this.faces = faces;
        this.faceIndexToColor = faceIndexToColor;
    }

    public ArrayList<Vector3> getVertecies() {
        return vertecies;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ArrayList<Integer>> getFaces() {
        return faces;
    }

    public HashMap<Vector3, ArrayList<Integer>> getFaceIndexToColor() {
        return faceIndexToColor;
    }
}
