package stydying.algo.com.algostudying.utils;

import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import stydying.algo.com.algostudying.ui.graphics.Model;
import stydying.algo.com.algostudying.utils.vectors.Vector2f;
import stydying.algo.com.algostudying.utils.vectors.Vector3f;

public class OBJLoader {
    public static Model loadTexturedModel(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        Model m = new Model();
        Model.Material currentMaterial = new Model.Material();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            }
            if (line.startsWith("mtllib ")) {
                String materialFileName = line.split(" ")[1];
                File materialFile = new File(f.getParentFile().getAbsolutePath() + "/" + materialFileName);
                BufferedReader materialFileReader = new BufferedReader(new FileReader(materialFile));
                String materialLine;
                Model.Material parseMaterial = new Model.Material();
                String parseMaterialName = "";
                while ((materialLine = materialFileReader.readLine()) != null) {
                    if (materialLine.startsWith("#")) {
                        continue;
                    }
                    if (materialLine.startsWith("newmtl ")) {
                        if (!parseMaterialName.equals("")) {
                            m.getMaterials().put(parseMaterialName, parseMaterial);
                        }
                        parseMaterialName = materialLine.split(" ")[1];
                        parseMaterial = new Model.Material();
                    } else if (materialLine.startsWith("Ns ")) {
                        parseMaterial.specularCoefficient = Float.valueOf(materialLine.split(" ")[1]);
                    } else if (materialLine.startsWith("Ka ")) {
                        String[] rgb = materialLine.split(" ");
                        parseMaterial.ambientColour[0] = Float.valueOf(rgb[1]);
                        parseMaterial.ambientColour[1] = Float.valueOf(rgb[2]);
                        parseMaterial.ambientColour[2] = Float.valueOf(rgb[3]);
                    } else if (materialLine.startsWith("Ks ")) {
                        String[] rgb = materialLine.split(" ");
                        parseMaterial.specularColour[0] = Float.valueOf(rgb[1]);
                        parseMaterial.specularColour[1] = Float.valueOf(rgb[2]);
                        parseMaterial.specularColour[2] = Float.valueOf(rgb[3]);
                    } else if (materialLine.startsWith("Kd ")) {
                        String[] rgb = materialLine.split(" ");
                        parseMaterial.diffuseColour[0] = Float.valueOf(rgb[1]);
                        parseMaterial.diffuseColour[1] = Float.valueOf(rgb[2]);
                        parseMaterial.diffuseColour[2] = Float.valueOf(rgb[3]);
                    } else if (materialLine.startsWith("map_Kd")) {
                        parseMaterial.texture = BitmapFactory.decodeFile(f.getParentFile().getAbsolutePath() + "/" + materialLine.split(" ")[1]);
                    } else {
                        System.err.println("[MTL] Unknown Line: " + materialLine);
                    }
                }
                m.getMaterials().put(parseMaterialName, parseMaterial);
                materialFileReader.close();
            } else if (line.startsWith("usemtl ")) {
                currentMaterial = m.getMaterials().get(line.split(" ")[1]);
            } else if (line.startsWith("v ")) {
                String[] xyz = line.split(" ");
                float x = Float.valueOf(xyz[1]);
                float y = Float.valueOf(xyz[2]);
                float z = Float.valueOf(xyz[3]);
                m.getVertices().add(new Vector3f(x, y, z));
            } else if (line.startsWith("vn ")) {
                String[] xyz = line.split(" ");
                float x = Float.valueOf(xyz[1]);
                float y = Float.valueOf(xyz[2]);
                float z = Float.valueOf(xyz[3]);
                m.getNormals().add(new Vector3f(x, y, z));
            } else if (line.startsWith("vt ")) {
                String[] xyz = line.split(" ");
                float s = Float.valueOf(xyz[1]);
                float t = Float.valueOf(xyz[2]);
                m.getTextureCoordinates().add(new Vector2f(s, t));
            } else if (line.startsWith("f ")) {
                String[] faceIndices = line.split(" ");
                int[] vertexIndicesArray = {Integer.parseInt(faceIndices[1].split("/")[0]),
                        Integer.parseInt(faceIndices[2].split("/")[0]), Integer.parseInt(faceIndices[3].split("/")[0])};
                int[] textureCoordinateIndicesArray = {-1, -1, -1};
                if (m.hasTextureCoordinates()) {
                    textureCoordinateIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[1]);
                    textureCoordinateIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[1]);
                    textureCoordinateIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[1]);
                }
                int[] normalIndicesArray = {0, 0, 0};
                if (m.hasNormals()) {
                    normalIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[2]);
                    normalIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[2]);
                    normalIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[2]);
                }
                m.getFaces().add(new Model.Face(vertexIndicesArray, normalIndicesArray,
                        textureCoordinateIndicesArray, currentMaterial));
            } else if (line.startsWith("s ")) {
                boolean enableSmoothShading = !line.contains("off");
                m.setSmoothShadingEnabled(enableSmoothShading);
            } else {
                System.err.println("[OBJ] Unknown Line: " + line);
            }
        }
        reader.close();
        return m;
    }
}