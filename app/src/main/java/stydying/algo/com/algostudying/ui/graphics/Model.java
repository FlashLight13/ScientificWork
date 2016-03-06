package stydying.algo.com.algostudying.ui.graphics;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stydying.algo.com.algostudying.utils.StreamUtils;
import stydying.algo.com.algostudying.utils.vectors.Vector2f;
import stydying.algo.com.algostudying.utils.vectors.Vector3f;

public class Model {
    public static final int COORDS_PER_VERTEX = 3;
    public static final int TEXTURE_DATA_SIZE = 2;

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textureCoordinates = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private HashMap<String, Material> materials = new HashMap<>();
    private List<Face> faces = new ArrayList<>();

    private FloatBuffer verticesBuffer;
    private IntBuffer indicesBuffer;
    private FloatBuffer texturesBuffer;
    private FloatBuffer normalsBuffer;

    private boolean enableSmoothShading = true;

    public boolean hasTextureCoordinates() {
        return textureCoordinates.size() > 0;
    }

    public boolean hasNormals() {
        return normals.size() > 0;
    }

    public void init() {
        initGeometryBuffers();
        initFaceBuffers();
    }

    private void initGeometryBuffers() {
        ArrayList<Float> ver = new ArrayList<>();
        for (Face face : faces) {
            for (int index : face.vertexIndices) {
                ver.add(vertices.get(index - 1).x);
                ver.add(vertices.get(index - 1).y);
                ver.add(vertices.get(index - 1).z);
            }
        }

        float[] buffer__ = new float[ver.size()];
        for (int i = 0; i < ver.size(); i++) {
            buffer__[i] = ver.get(i);
        }

        ByteBuffer verticesByteBuffer = ByteBuffer.allocateDirect(buffer__.length * StreamUtils.BYTES_IN_FLOAT);
        verticesByteBuffer.order(ByteOrder.nativeOrder());
        verticesBuffer = verticesByteBuffer.asFloatBuffer();
        verticesBuffer.put(buffer__);
        verticesBuffer.position(0);

        // todo optimize this code
        ArrayList<Float> normalsArray = new ArrayList<>();
        for (Face face : faces) {
            for (int normalIndex : face.normalIndices) {
                normalsArray.add(normals.get(normalIndex - 1).x);
                normalsArray.add(normals.get(normalIndex - 1).y);
                normalsArray.add(normals.get(normalIndex - 1).z);
            }
        }

        float[] buffer = new float[normalsArray.size()];
        for (int i = 0; i < normalsArray.size(); i++) {
            buffer[i] = normalsArray.get(i);
        }

        ByteBuffer normalsByteBuffer = ByteBuffer.allocateDirect(normalsArray.size() * StreamUtils.BYTES_IN_FLOAT);
        normalsByteBuffer.order(ByteOrder.nativeOrder());
        normalsBuffer = normalsByteBuffer.asFloatBuffer();
        normalsBuffer.put(buffer).position(0);
    }

    private void initFaceBuffers() {
        final int facesArraySize = faces.size() * COORDS_PER_VERTEX;
        int[] indicesArray = new int[facesArraySize];

        for (int i = 0; i < faces.size(); i++) {
            int[] indicesVertex = new int[COORDS_PER_VERTEX];
            int[] vertexIndicesArray = faces.get(i).getVertexIndices();
            for (int j = 0; j < COORDS_PER_VERTEX; j++) {
                indicesVertex[j] = vertexIndicesArray[j] - 1;
            }
            System.arraycopy(indicesVertex, 0, indicesArray, i * COORDS_PER_VERTEX, indicesVertex.length);
        }
        ByteBuffer indicesByteBuffer = ByteBuffer.allocateDirect(indicesArray.length * StreamUtils.BYTES_IN_INT);
        indicesByteBuffer.order(ByteOrder.nativeOrder());
        indicesBuffer = indicesByteBuffer.asIntBuffer();
        indicesBuffer.put(indicesArray);
        indicesBuffer.position(0);

        // todo optimize this code
        ArrayList<Float> texturesArray = new ArrayList<>();
        for (Face face : faces) {
            for (int textureIndex : face.textureCoordinateIndices) {
                texturesArray.add(textureCoordinates.get(textureIndex - 1).x);
                texturesArray.add(textureCoordinates.get(textureIndex - 1).y);
            }
        }

        float[] buffer = new float[texturesArray.size()];
        for (int i = 0; i < texturesArray.size(); i++) {
            buffer[i] = texturesArray.get(i);
        }

        ByteBuffer texturesByteBuffer = ByteBuffer.allocateDirect(texturesArray.size() * StreamUtils.BYTES_IN_FLOAT);
        texturesByteBuffer.order(ByteOrder.nativeOrder());
        texturesBuffer = texturesByteBuffer.asFloatBuffer();
        texturesBuffer.put(buffer).position(0);
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

    public List<Face> getFaces() {
        return faces;
    }

    public FloatBuffer getTextureBuffer() {
        return texturesBuffer;
    }

    public FloatBuffer getVerticesBuffer() {
        return this.verticesBuffer;
    }

    public IntBuffer getIndiciesBuffer() {
        return this.indicesBuffer;
    }

    public FloatBuffer getNormalsBuffer() {
        return this.normalsBuffer;
    }

    public boolean isSmoothShadingEnabled() {
        return enableSmoothShading;
    }

    public void setSmoothShadingEnabled(boolean smoothShadingEnabled) {
        this.enableSmoothShading = smoothShadingEnabled;
    }

    public HashMap<String, Material> getMaterials() {
        return materials;
    }

    public static class Material implements Serializable {
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
    }

    public static class Face implements Serializable {
        private static final long serialVersionUID = -6260903088995265709L;

        private final int[] vertexIndices = {-1, -1, -1};
        private final int[] normalIndices = {-1, -1, -1};
        private final int[] textureCoordinateIndices = {-1, -1, -1};
        private Material material;

        public Material getMaterial() {
            return material;
        }

        public boolean hasNormals() {
            return normalIndices[0] != -1;
        }

        public boolean hasTextureCoordinates() {
            return textureCoordinateIndices[0] != -1;
        }

        public int[] getVertexIndices() {
            return vertexIndices;
        }

        public int[] getTextureCoordinateIndices() {
            return textureCoordinateIndices;
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