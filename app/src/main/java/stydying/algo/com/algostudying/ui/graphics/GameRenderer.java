package stydying.algo.com.algostudying.ui.graphics;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import stydying.algo.com.algostudying.R;
import stydying.algo.com.algostudying.game.GameWorld;
import stydying.algo.com.algostudying.game.objects.GameObject;
import stydying.algo.com.algostudying.utils.StreamUtils;
import stydying.algo.com.algostudying.utils.vectors.Vector3i;

public class GameRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "LessonTwoRenderer";

    private float[] mModelMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];

    private int mMVPMatrixHandle;
    private int mMVMatrixHandle;
    private int mLightPosHandle;
    private int mPositionHandle;
    private int mNormalHandle;

    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;

    private final float[] mLightPosInModelSpace = new float[]{0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];

    private int mPerVertexProgramHandle;
    private int mPointProgramHandle;

    private Context context;
    private GameWorld gameWorld;
    private Camera camera;
    private FPSCounter fpsCounter = new FPSCounter();

    private Map<String, Integer> loadedMaterials = new HashMap<>();

    public GameRenderer(Context context) {
        this.context = context;
        this.camera = new Camera(context);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        final int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, loadShader(R.raw.vertex_shader));
        final int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, loadShader(R.raw.fragment_shader));

        camera.init();
        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});

        final int pointVertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER, loadShader(R.raw.point_vertex_shader));
        final int pointFragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, loadShader(R.raw.point_fragment_shader));
        mPointProgramHandle = createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle,
                new String[]{"a_Position"});

        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
        mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_TexCoordinate");
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        long time = SystemClock.uptimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        GLES20.glUseProgram(mPerVertexProgramHandle);
        if (gameWorld != null) {
            Iterator<GameObject> gameObjectIterator = gameWorld.getObjectsIterator();
            GameObject gameObject;
            Vector3i coordinates;
            while (gameObjectIterator.hasNext()) {
                gameObject = gameObjectIterator.next();
                if (gameObject == null) {
                    continue;
                }
                coordinates = gameObject.getCoordinates();
                if (gameObject.getModel() != null) {
                    Matrix.setIdentityM(mLightModelMatrix, 0);
                    Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
                    Matrix.rotateM(mLightModelMatrix, 0, angleInDegrees, 0.0f, 1.0f, 0.0f);
                    Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

                    Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
                    Matrix.multiplyMV(mLightPosInEyeSpace, 0, camera.mViewMatrix(), 0, mLightPosInWorldSpace, 0);

                    Matrix.setIdentityM(mModelMatrix, 0);
                    Matrix.translateM(mModelMatrix, 0, coordinates.x, coordinates.y, coordinates.z);
                    Matrix.rotateM(mModelMatrix, 0, 0.5f, 0.0f, 1.0f, 0.0f);

                    for (Model.Face face : gameObject.getModel().getFaces()) {
                        drawFace(face);
                    }
                }
            }
        }

        GLES20.glUseProgram(mPointProgramHandle);
        drawLight();
        fpsCounter.logFrame();
    }

    private void drawFace(Model.Face face) {
        if (face == null) {
            return;
        }
        int mTextureDataHandle = loadTexture(face.getMaterial());

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        FloatBuffer v = face.getVertices();
        GLES20.glVertexAttribPointer(mPositionHandle, Model.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                Model.COORDS_PER_VERTEX * StreamUtils.BYTES_IN_FLOAT, v);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mNormalHandle, Model.COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                Model.COORDS_PER_VERTEX * StreamUtils.BYTES_IN_FLOAT, face.getNormals());

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, Model.TEXTURE_DATA_SIZE, GLES20.GL_FLOAT, false,
                Model.TEXTURE_DATA_SIZE * StreamUtils.BYTES_IN_FLOAT, face.getTextures());

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        Matrix.multiplyMM(mMVPMatrix, 0, camera.mViewMatrix(), 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, v.capacity() / 3);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordinateHandle);
    }

    private void drawLight() {
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");

        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        Matrix.multiplyMM(mMVPMatrix, 0, camera.mViewMatrix(), 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
    }

    private int compileShader(final int shaderType, final String shaderSource) {
        int shaderHandle = GLES20.glCreateShader(shaderType);
        if (shaderHandle != 0) {
            GLES20.glShaderSource(shaderHandle, shaderSource);
            GLES20.glCompileShader(shaderHandle);
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
                GLES20.glDeleteShader(shaderHandle);
                shaderHandle = 0;
            }
        }
        if (shaderHandle == 0) {
            throw new RuntimeException("Error creating shader.");
        }
        return shaderHandle;
    }

    private int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes) {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);
            if (attributes != null) {
                final int size = attributes.length;
                for (int i = 0; i < size; i++) {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }
            GLES20.glLinkProgram(programHandle);
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(programHandle));
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }
        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }
        return programHandle;
    }

    private int loadTexture(Model.Material material) {
        if (loadedMaterials.containsKey(material.name)) {
            return loadedMaterials.get(material.name);
        }
        final int[] textureHandle = new int[1];
        GLES20.glGenTextures(1, textureHandle, 0);
        if (textureHandle[0] != 0) {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, material.texture, 0);
        }

        if (textureHandle[0] == 0) {
            throw new RuntimeException("Error loading texture.");
        }
        loadedMaterials.put(material.name, textureHandle[0]);
        return textureHandle[0];
    }

    private String loadShader(int resId) {
        return StreamUtils.readTextFileFromRawResource(context, resId);
    }

    public void setWorld(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void onTouch(MotionEvent motionEvent) {
        camera.onTouchEvent(motionEvent);
    }

    public class FPSCounter {
        long startTime = System.nanoTime();
        int frames = 0;

        public void logFrame() {
            frames++;
            if (System.nanoTime() - startTime >= 1000000000) {
                Log.d("DebugLogs", "fps: " + frames);
                frames = 0;
                startTime = System.nanoTime();
            }
        }
    }
}