package stydying.algo.com.algostudying.ui.graphics;

import android.content.Context;
import android.graphics.Point;
import android.opengl.Matrix;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;

import stydying.algo.com.algostudying.ui.RotationGestureDetector;
import stydying.algo.com.algostudying.utils.vectors.Vector3f;

/**
 * Created by Anton on 10.02.2016.
 */
public class Camera {

    private final GestureDetector scrollGestureDetector;
    private final RotationGestureDetector rotationGestureDetector;
    private final ScaleGestureDetector scaleGestureDetector;

    private final int SCREEN_X_MULTIPLIER;
    private final int SCREEN_Y_MULTIPLIER;

    private float currentAngle = -45.0f;
    private float scale = 0;

    private int i = 0;
    private float[] mViewMatrix = new float[16];

    private float upZ = 1.0f;
    private float upY = 0.0f;
    private float upX = 0.0f;

    private float eyeX = 0.0f;
    private float eyeZ = 15f;
    private float eyeY = 10.0f;

    private float lookX = 0.0f;
    private float lookY = 0.0f;
    private float lookZ = 0.0f;

    private float eyeXdelta = 0;
    private float eyeYdelta = 0;

    private Vector3f gameWorldCenter;

    public Camera(Context context) {
        rotationGestureDetector = new RotationGestureDetector(new RotationGestureDetector.OnRotationGestureListener() {
            @Override
            synchronized public void OnRotation(RotationGestureDetector rotationDetector) {
                currentAngle = rotationDetector.getDeltaAngle();
            }
        });
        scrollGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                final float deltaX = (distanceX / SCREEN_X_MULTIPLIER) * 25;
                final float deltaY = (distanceY / SCREEN_Y_MULTIPLIER) * 25;
               /* eyeX = eyeX - deltaX;
                eyeY = eyeY + deltaY;
                lookX = lookX - deltaX;
                lookY = lookY + deltaY;*/
                eyeXdelta += deltaX;
                eyeYdelta += deltaY;
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scale = detector.getScaleFactor();
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {

            }
        });

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_X_MULTIPLIER = size.x;
        SCREEN_Y_MULTIPLIER = size.y;
    }

    public void init(Vector3f gameWorldCenter) {
        this.eyeY = -gameWorldCenter.x / 2;
        this.eyeX = -gameWorldCenter.y / 2;
        this.gameWorldCenter = gameWorldCenter;
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        try {
            if (!rotationGestureDetector.onTouchEvent(motionEvent)) {
                scrollGestureDetector.onTouchEvent(motionEvent);
            }
            scaleGestureDetector.onTouchEvent(motionEvent);
        } catch (Exception e) {
            // silently catch
        }
        if (i == 3) {
            Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
            i = 0;
        }
        i++;
    }

    public float[] mViewMatrix() {
        return mViewMatrix;
    }

    public void processModelMatrix(float[] mModelMatrix) {
        Matrix.rotateM(mModelMatrix, 0, currentAngle, 0, 0, 1);
        Matrix.translateM(mModelMatrix, 0, -gameWorldCenter.x, -gameWorldCenter.y, 0);
        Matrix.translateM(mModelMatrix, 0, eyeXdelta, eyeYdelta, 0);
    }
}
