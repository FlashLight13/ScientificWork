package stydying.algo.com.algostudying.ui.graphics.modules;

import stydying.algo.com.algostudying.utils.vectors.Vector3f;

/**
 * Created by anton on 26.07.15.
 */
public class CameraModule {
    private static final Vector3f CAMERA_POSITION = new Vector3f(0f, 20f, -30f);
    private static final Vector3f EYE_POSITION = new Vector3f(0f, 0f, 0f);
    private static final Vector3f UP_POSITION = new Vector3f(0f, 1f, 0f);

//    public static void look(GameRenderer.MatrixHolder matrixHolder, int width, int height) {
//
//    }

    public static void positionCamera(float x, float y, float z) {
        CAMERA_POSITION.x = x;
        CAMERA_POSITION.y = y;
        CAMERA_POSITION.z = z;
    }

    public static void positionCamera(Vector3f position) {
        CAMERA_POSITION.x = position.x;
        CAMERA_POSITION.y = position.y;
        CAMERA_POSITION.z = position.z;
    }

    public static void positionEye(float x, float y, float z) {
        EYE_POSITION.x = x;
        EYE_POSITION.y = y;
        EYE_POSITION.z = z;
    }

    public static void positionEye(Vector3f position) {
        EYE_POSITION.x = position.x;
        EYE_POSITION.y = position.y;
        EYE_POSITION.z = position.z;
    }

    public static void positionUp(float x, float y, float z) {
        UP_POSITION.x = x;
        UP_POSITION.y = y;
        UP_POSITION.z = z;
    }

    public static void positionUp(Vector3f position) {
        UP_POSITION.x = position.x;
        UP_POSITION.y = position.y;
        UP_POSITION.z = position.z;
    }
}
