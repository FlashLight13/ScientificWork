package stydying.algo.com.algostudying.ui.graphics;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.text.TextUtils;

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

    public static final int VERTICIES_BUFFER = 0;
    public static final int TEXTURES_BUFFER = 1;
    public static final int NORMALS_BUFFER = 2;

    private List<Vector3f> vertices = new ArrayList<>();
    private List<Vector2f> textureCoordinates = new ArrayList<>();
    private List<Vector3f> normals = new ArrayList<>();
    private List<Face> faces = new ArrayList<>();
    private Map<String, Material> materials = new HashMap<>();

    private Map<String, DrawingBlock> drawings;

    public boolean hasTextureCoordinates() {
        return textureCoordinates.size() > 0;
    }

    public boolean hasNormals() {
        return normals.size() > 0;
    }

    public void addTextureCoord(Vector2f newTextureCoord) {
        textureCoordinates.add(newTextureCoord);
    }

    public void addVertex(Vector3f newVertex) {
        vertices.add(newVertex);
    }

    public void addNormal(Vector3f newNormal) {
        normals.add(newNormal);
    }

    public Map<String, Material> getMaterials() {
        return materials;
    }

    public void addFace(Face face) {
        this.faces.add(face);
    }

    public void initDrawings() {
        if (drawings == null) {
            drawings = new HashMap<>();
            Material material;
            for (Face face : faces) {
                material = face.getMaterial();
                if (drawings.containsKey(material.name)) {
                    DrawingBlock drawingBlock = drawings.get(face.getMaterial().name);
                    drawingBlock.addVer(face.getVer());
                    drawingBlock.addTex(face.getTex());
                    drawingBlock.addNorm(face.getNorm());
                } else {
                    DrawingBlock drawingBlock = new DrawingBlock(material);
                    drawingBlock.addVer(face.getVer());
                    drawingBlock.addNorm(face.getNorm());
                    drawingBlock.addTex(face.getTex());
                    drawings.put(material.name, drawingBlock);
                }
            }
            for (DrawingBlock block : getDrawingBlocks()) {
                block.bindBuffers();
            }
        }
    }

    public void release() {
        if (drawings != null) {
            drawings.clear();
            drawings = null;
        }
    }

    public Iterable<DrawingBlock> getDrawingBlocks() {
        return drawings.values();
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

        @Override
        public boolean equals(Object o) {
            return o instanceof Material && TextUtils.equals(name, ((Material) o).name);
        }
    }

    public class Face implements Serializable {
        private static final long serialVersionUID = -6260903088995265709L;

        private final int[] vertexIndices = {-1, -1, -1};
        private final int[] normalIndices = {-1, -1, -1};
        private final int[] textureCoordinateIndices = {-1, -1, -1};
        private Material material;

        private float[] getVer() {
            float[] buffer = new float[vertexIndices.length * 3];
            for (int i = 0; i < vertexIndices.length; i++) {
                buffer[3 * i] = vertices.get(vertexIndices[i] - 1).x;
                buffer[3 * i + 1] = vertices.get(vertexIndices[i] - 1).y;
                buffer[3 * i + 2] = vertices.get(vertexIndices[i] - 1).z;
            }
            return buffer;
        }

        private float[] getTex() {
            float[] texturesArray = new float[2 * textureCoordinateIndices.length];
            for (int i = 0; i < textureCoordinateIndices.length; i++) {
                texturesArray[2 * i] = textureCoordinates.get(textureCoordinateIndices[i] - 1).x;
                texturesArray[2 * i + 1] = textureCoordinates.get(textureCoordinateIndices[i] - 1).y;
            }
            return texturesArray;
        }

        private float[] getNorm() {
            float[] buffer = new float[normalIndices.length * 3];
            for (int i = 0; i < normalIndices.length; i++) {
                buffer[3 * i] = normals.get(normalIndices[i] - 1).x;
                buffer[3 * i + 1] = normals.get(normalIndices[i] - 1).y;
                buffer[3 * i + 2] = normals.get(normalIndices[i] - 1).z;
            }
            return buffer;
        }

        public Material getMaterial() {
            return material;
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

    public static class DrawingBlock {
        private List<Float> ver = new ArrayList<>();
        private List<Float> norm = new ArrayList<>();
        private List<Float> tex = new ArrayList<>();
        private Material material;

        private int[] buffers;
        private int vertexesCount;
        private boolean isBound;

        public DrawingBlock(Material material) {
            this.material = material;
        }

        public void addVer(float[] ver) {
            for (float current : ver) {
                this.ver.add(current);
            }
        }

        public void addNorm(float[] norm) {
            for (float current : norm) {
                this.norm.add(current);
            }
        }

        public void addTex(float[] tex) {
            for (float current : tex) {
                this.tex.add(current);
            }
        }

        public int[] getBuffers() {
            return buffers;
        }

        public Material getMaterial() {
            return material;
        }

        public int getVertexesCount() {
            return vertexesCount;
        }

        public void bindBuffers() {
            if (!isBound) {
                buffers = new int[3];
                GLES20.glGenBuffers(buffers.length, buffers, 0);

                float[] verArray = toArray(ver);
                float[] texArray = toArray(tex);
                float[] normArray = toArray(norm);


                ByteBuffer verticesByteBuffer = ByteBuffer.allocateDirect(verArray.length * StreamUtils.BYTES_IN_FLOAT);
                verticesByteBuffer.order(ByteOrder.nativeOrder());
                FloatBuffer verticesBuffer = verticesByteBuffer.asFloatBuffer();
                verticesBuffer.put(verArray);
                verticesBuffer.position(0);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[VERTICIES_BUFFER]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                        verticesBuffer.capacity() * StreamUtils.BYTES_IN_FLOAT,
                        verticesBuffer,
                        GLES20.GL_STATIC_DRAW);
                vertexesCount = verticesBuffer.capacity() / 3;
                verticesBuffer.limit(0);

                ByteBuffer texturesByteBuffer = ByteBuffer.allocateDirect(texArray.length * StreamUtils.BYTES_IN_FLOAT);
                texturesByteBuffer.order(ByteOrder.nativeOrder());
                FloatBuffer texturesBuffer = texturesByteBuffer.asFloatBuffer();
                texturesBuffer.put(texArray).position(0);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[TEXTURES_BUFFER]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                        texturesBuffer.capacity() * StreamUtils.BYTES_IN_FLOAT,
                        texturesBuffer,
                        GLES20.GL_STATIC_DRAW);
                texturesBuffer.limit(0);

                ByteBuffer normsByteBuffer = ByteBuffer.allocateDirect(normArray.length * StreamUtils.BYTES_IN_FLOAT);
                normsByteBuffer.order(ByteOrder.nativeOrder());
                FloatBuffer normBuffer = normsByteBuffer.asFloatBuffer();
                normBuffer.put(normArray);
                normBuffer.position(0);
                GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[NORMALS_BUFFER]);
                GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                        normBuffer.capacity() * StreamUtils.BYTES_IN_FLOAT,
                        normBuffer,
                        GLES20.GL_STATIC_DRAW);

                normBuffer.limit(0);
                isBound = true;
            }
        }

        private float[] toArray(List<Float> list) {
            float[] result = new float[list.size()];
            for (int i = 0; i < list.size(); i++) {
                result[i] = list.get(i);
            }
            return result;
        }
    }
}