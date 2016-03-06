package stydying.algo.com.algostudying.ui.graphics;

import android.content.Context;
import android.graphics.Point;
import android.opengl.Matrix;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

import stydying.algo.com.algostudying.ui.RotationGestureDetector;

/**
 * Created by Anton on 10.02.2016.
 */
public class Camera {

    private final GestureDetector.SimpleOnGestureListener scrollListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            eyeX += (distanceX / SCREEN_X_MULTIPLIER) * 15;
            eyeY -= (distanceY / SCREEN_Y_MULTIPLIER) * 15;
            lookX += (distanceX / SCREEN_X_MULTIPLIER) * 15;
            lookY -= (distanceY / SCREEN_Y_MULTIPLIER) * 15;
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };
    private final RotationGestureDetector.OnRotationGestureListener rotationGestureListener = new RotationGestureDetector.OnRotationGestureListener() {
        @Override
        public void OnRotation(RotationGestureDetector rotationDetector) {
            angle = rotationDetector.getAngle();
            eyeX = (float) (eyeZ * Math.cos(Math.toRadians(angle))) + lookX;
            eyeY = (float) (eyeZ * Math.sin(Math.toRadians(angle))) + lookY;
        }
    };

    private final GestureDetector scrollGestureDetector;
    private final RotationGestureDetector rotationGestureDetector;

    private final int SCREEN_X_MULTIPLIER;
    private final int SCREEN_Y_MULTIPLIER;

    private float angle = 0.0f;
    private int i = 0;
    private float[] mViewMatrix = new float[16];

    private float upZ = 1.0f;
    private float upY = 0.0f;
    private float upX = 0.0f;

    private float eyeX = 12.0f;
    private float eyeZ = 15f;
    private float eyeY = -8.0f;

    private float lookX = 0.0f;
    private float lookY = 0.0f;
    private float lookZ = 0.0f;

    public Camera(Context context) {
        rotationGestureDetector = new RotationGestureDetector(rotationGestureListener);
        scrollGestureDetector = new GestureDetector(context, scrollListener);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        SCREEN_X_MULTIPLIER = size.x;
        SCREEN_Y_MULTIPLIER = size.y;
    }

    public void init() {
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        scrollGestureDetector.onTouchEvent(motionEvent);
        rotationGestureDetector.onTouchEvent(motionEvent);
        if (i == 3) {
            Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
            i = 0;
        }
        i++;
    }

    public float[] mViewMatrix() {
        return mViewMatrix;
    }
}
