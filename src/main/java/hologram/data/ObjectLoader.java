package hologram.data;

import hologram.Hologram;
import hologram.utils.Vector3;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;

public class ObjectLoader {

    // we have loaded objects so we can reuse them
    private ArrayList<LoadedObject> loadedObjects = new ArrayList<>();

    public ObjectLoader() throws IOException {
        // load all the objects in hologram folder
        loadObjFiles();
    }

    public boolean hasObjectByName(String name) {
        if (name == null) {
            return false;
        }
        for (LoadedObject loadedObject : loadedObjects) {
            if (Objects.equals(loadedObject.getName(), name)) {
                return true;
            }
        }

        return false;
    }

    public LoadedObject getObjectByName(String name) {
        if (name == null) {
            return null;
        }
        for (LoadedObject loadedObject : loadedObjects) {
            if (Objects.equals(loadedObject.getName(), name)) {
                return loadedObject;
            }
        }

        return null;
    }


    private void loadObjFiles() throws IOException {
        if (Hologram.getInstance().getDataFolder().listFiles() == null) {
            return;
        }

        ArrayList<File> listOfFiles = new ArrayList<>();
        for (File file : Hologram.getInstance().getDataFolder().listFiles()) {
            listOfFiles.add(file);
        }

        // TODO make so it doesnt load objects at start rather load them when you need them

        // load
        for (File file : listOfFiles) {

            if (file.getName().endsWith(".obj")) {
                // TODO implement some checks to check if it really contains the values of obj file

                FileReader objectFileReader;
                objectFileReader=new FileReader(file);

                BufferedReader objectBuffReader=new BufferedReader(objectFileReader);
                String line;
                ArrayList<Vector3> vertecies = new ArrayList<>();
                ArrayList<ArrayList<Integer>> faces = new ArrayList<>();

                HashMap<Vector3, ArrayList<Integer>> faceIndexToColor = new HashMap<>();

                // material one
                // material two after we rad some line

                // list of all of the vertex gourpd in order of material colors
                ArrayList<ArrayList<Integer>> faceIndexInOrderOfColor = new ArrayList<>();

                ArrayList<Integer> faceIndexesOfOneColor = new ArrayList<>();

                int faceIndex = 0;
                boolean added = false;

                // main extraction point
                while((line=objectBuffReader.readLine())!=null) {

                    // TODO make logic to show materials that are not in order (because in obj they are scattered around)

                    if (line.startsWith("usemtl ")) {

                        if (added) {
                            // IMPORTANT add it to the list of materials
                            faceIndexInOrderOfColor.add(faceIndexesOfOneColor);
                            // reset the list
                            faceIndexesOfOneColor = new ArrayList<>();
                        }
                        // we dont want to add the first one since its going to be empty
                        added = true;
                    }

                    // verticies
                    if (line.startsWith("v ")) {
                        vertecies.add(new Vector3(line));
                    }

                    //faces
                    if (line.startsWith("f ")) {

                        // we split the line in many elements
                        String[] verteciesString = line.split(" ");
                        // our expected output
                        ArrayList<Integer> indecies = new ArrayList<>();

                        for (String vertex : verteciesString) {
                            // in obj format the 1st element of the n/n/n format is vertex that is connected
                            // the first one is going to not contain a number
                            try{
                                indecies.add(Integer.valueOf(vertex.split("/")[0]));
                            } catch (NumberFormatException ignored) {
                            }
                        }
                        faces.add(indecies);
                        faceIndexesOfOneColor.add(faceIndex);
                        faceIndex++;
                    }
                }

                // add the last unsaved one
                faceIndexInOrderOfColor.add(faceIndexesOfOneColor);

                // FIND THE FILE MATERIAL FILE OF THIS OBJ
                File materialFile = null;

                for (File file1 : listOfFiles) {

                    //Bukkit.getLogger().info(""+(Objects.equals(file1.getName().split("\\.")[0], file.getName().split("\\.")[0]))+"   "+(Objects.equals(file1.getName().split("\\.")[1], "mtl")));

                    if (Objects.equals(file1.getName().split("\\.")[0], file.getName().split("\\.")[0]) &&
                        Objects.equals(file1.getName().split("\\.")[1], "mtl")
                    ) {
                        materialFile = file1;
                    }
                }

                if (materialFile == null) {
                    throw new RuntimeException("There is no material file for "+file.getName());
                }


                FileReader materialFileReader=new FileReader(materialFile);
                BufferedReader materialFileBuffReader=new BufferedReader(materialFileReader);

                ArrayList<Vector3> colors = new ArrayList<>();
                // loop over lines

                while((line=materialFileBuffReader.readLine())!=null) {

                    // Kd is diffusion layer
                    if (line.startsWith("Kd ")) {

                        String[] deffuseString = line.split(" ");
                        StringBuilder deffuseStringOut = new StringBuilder();

                        for (String string : deffuseString) {
                            try {
                                Double.parseDouble(string);
                                deffuseStringOut.append(string).append(" ");
                            } catch (NumberFormatException ignored) {
                            }
                        }

                        colors.add(new Vector3(deffuseStringOut.toString()));
                    }
                }

                // we have vertexIndexesOfAllMaterials and

                int index = 0;
                for (Vector3 color : colors) {
                    faceIndexToColor.put(color, faceIndexInOrderOfColor.get(index));
                    index++;
                }

                // TODO REMOVED DEBUG LINE BELLOW
                Bukkit.getLogger().info("COLORS:  "+colors.size()+" for "+file.getName()+" loaded groups of materials: "+faceIndexToColor.size());

                loadedObjects.add(new LoadedObject(file.getName().toLowerCase(), vertecies, faces, faceIndexToColor));
            }
        }
    }

    public ArrayList<LoadedObject> getLoadedObjects() {
        return this.loadedObjects;
    }
}
