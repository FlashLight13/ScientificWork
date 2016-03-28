package stydying.algo.com.algostudying.ui.graphics;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import stydying.algo.com.algostudying.utils.StreamUtils;
import stydying.algo.com.algostudying.utils.vectors.Vector2f;
import stydying.algo.com.algostudying.utils.vectors.Vector3f;

public class Model {
    public static final int COORDS_PER_VERTEX = 3;
    public static final int TEXTURE_DATA_SIZE = 2;

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textureCoordinates = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private List<Face> faces = new ArrayList<>();
    private Map<String, Material> materials = new HashMap<>();

    public boolean hasTextureCoordinates() {
        return textureCoordinates.size() > 0;
    }

    public boolean hasNormals() {
        return normals.size() > 0;
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public List<Vector2f> getTextureCoordinates() {
        return textureCoordinates;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public Map<String, Material> getMaterials() {
        return materials;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public class Material implements Serializable {
        private static final long serialVersionUID = -5014509996991649228L;

        @Override
        public String toString() {
            return "Material{" +
                    "specularCoefficient=" + specularCoefficient +
                    ", ambientColour=" + ambientColour +
                    ", diffuseColour=" + diffuseColour +
                    ", specularColour=" + specularColour +
                    '}';
        }

        /**
         * Between 0 and 1000.
         */
        public float specularCoefficient = 100;
        public float[] ambientColour = {0.2f, 0.2f, 0.2f};
        public float[] diffuseColour = {0.3f, 1, 1};
        public float[] specularColour = {1, 1, 1};
        public Bitmap texture = null;
        public String name;
    }

    public class Face implements Serializable {
        private static final long serialVersionUID = -6260903088995265709L;

        private final int[] vertexIndices = {-1, -1, -1};
        private final int[] normalIndices = {-1, -1, -1};
        private final int[] textureCoordinateIndices = {-1, -1, -1};
        private Material material;

        private FloatBuffer ver;
        private FloatBuffer norm;
        private FloatBuffer tex;

        public Material getMaterial() {
            return material;
        }

        public FloatBuffer getTextures() {
            if (tex == null) {
                ArrayList<Float> texturesArray = new ArrayList<>();
                for (int textureIndex : textureCoordinateIndices) {
                    texturesArray.add(textureCoordinates.get(textureIndex - 1).x);
                    texturesArray.add(textureCoordinates.get(textureIndex - 1).y);
                }

                float[] buffer = new float[texturesArray.size()];
                for (int i = 0; i < texturesArray.size(); i++) {
                    buffer[i] = texturesArray.get(i);
                }

                ByteBuffer texturesByteBuffer = ByteBuffer.allocateDirect(texturesArray.size() * StreamUtils.BYTES_IN_FLOAT);
                texturesByteBuffer.order(ByteOrder.nativeOrder());
                FloatBuffer texturesBuffer = texturesByteBuffer.asFloatBuffer();
                texturesBuffer.put(buffer).position(0);
                tex = texturesBuffer;
            }
            return tex;
        }

        public FloatBuffer getVertices() {
            if (ver == null) {
                float[] buffer = new float[vertexIndices.length * 3];
                for (int i = 0; i < vertexIndices.length; i++) {
                    buffer[3 * i] = vertices.get(vertexIndices[i] - 1).x;
                    buffer[3 * i + 1] = vertices.get(vertexIndices[i] - 1).y;
                    buffer[3 * i + 2] = vertices.get(vertexIndices[i] - 1).z;
                }

                ByteBuffer verticesByteBuffer = ByteBuffer.allocateDirect(buffer.length * StreamUtils.BYTES_IN_FLOAT);
                verticesByteBuffer.order(ByteOrder.nativeOrder());
                FloatBuffer verticesBuffer = verticesByteBuffer.asFloatBuffer();
                verticesBuffer.put(buffer);
                verticesBuffer.position(0);
                ver = verticesBuffer;
            }
            return ver;
        }

        public FloatBuffer getNormals() {
            if (norm == null) {
                float[] buffer = new float[normalIndices.length * 3];
                for (int i = 0; i < normalIndices.length; i++) {
                    buffer[3 * i] = normals.get(normalIndices[i] - 1).x;
                    buffer[3 * i + 1] = normals.get(normalIndices[i] - 1).y;
                    buffer[3 * i + 2] = normals.get(normalIndices[i] - 1).z;
                }

                ByteBuffer verticesByteBuffer = ByteBuffer.allocateDirect(buffer.length * StreamUtils.BYTES_IN_FLOAT);
                verticesByteBuffer.order(ByteOrder.nativeOrder());
                FloatBuffer verticesBuffer = verticesByteBuffer.asFloatBuffer();
                verticesBuffer.put(buffer);
                verticesBuffer.position(0);
                norm = verticesBuffer;
            }
            return norm;
        }

        public Face(int[] vertexIndices, int[] normalIndices, int[] textureCoordinateIndices, Material material) {
            this.vertexIndices[0] = vertexIndices[0];
            this.vertexIndices[1] = vertexIndices[1];
            this.vertexIndices[2] = vertexIndices[2];
            this.textureCoordinateIndices[0] = textureCoordinateIndices[0];
            this.textureCoordinateIndices[1] = textureCoordinateIndices[1];
            this.textureCoordinateIndices[2] = textureCoordinateIndices[2];
            this.normalIndices[0] = normalIndices[0];
            this.normalIndices[1] = normalIndices[1];
            this.normalIndices[2] = normalIndices[2];
            this.material = material;
        }
    }
}