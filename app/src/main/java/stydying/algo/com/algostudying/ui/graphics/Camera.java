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

import static java.lang.Math.*;

/**
 * Created by Anton on 10.02.2016.
 */
public class Camera {

    private final GestureDetector scrollGestureDetector;
    private final RotationGestureDetector rotationGestureDetector;
    private final ScaleGestureDetector scaleGestureDetector;

    private final int SCREEN_X_MULTIPLIER;
    private final int SCREEN_Y_MULTIPLIER;

    private double deltaAngle = 0.0f;
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
        rotationGestureDetector = new RotationGestureDetector(new RotationGestureDetector.OnRotationGestureListener() {
            @Override
            public void OnRotation(RotationGestureDetector rotationDetector) {
                deltaAngle = toRadians(rotationDetector.getDeltaAngle());
                final double cos = cos(deltaAngle);
                final double sin = sin(deltaAngle);
                final float newX = (float) (eyeX * cos - eyeY * sin);
                final float newY = (float) (eyeX * sin + eyeY * cos);
                eyeX = newX;
                eyeY = newY;
            }
        });
        scrollGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                final float deltaX = (distanceX / SCREEN_X_MULTIPLIER) * 15;
                final float deltaY = (distanceY / SCREEN_Y_MULTIPLIER) * 15;
                final float resultDeltaX = (float) (deltaX * cos(deltaAngle) - deltaY * sin(deltaAngle));
                final float resultDeltaY = (float) (deltaX * sin(deltaAngle) + deltaY * cos(deltaAngle));
                eyeX = eyeX + getSightDirectionMultiplier(cos(deltaAngle)) * resultDeltaX;
                eyeY = eyeY + getSightDirectionMultiplier(sin(deltaAngle)) * resultDeltaY;
                lookX = lookX + getSightDirectionMultiplier(cos(deltaAngle)) * resultDeltaX;
                lookY = lookY + getSightDirectionMultiplier(sin(deltaAngle)) * resultDeltaY;
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                final float scale = 10;
                if (detector.getScaleFactor() > 0) {
                    eyeX += scale;
                    eyeY += scale;
                } else {
                    eyeX -= scale;
                    eyeY -= scale;
                }
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

    public void init() {
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        try {
            scrollGestureDetector.onTouchEvent(motionEvent);
            rotationGestureDetector.onTouchEvent(motionEvent);
            //scaleGestureDetector.onTouchEvent(motionEvent);
        } catch (Exception e) {
            // silently catch
        }
        if (i == 3) {
            Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
            i = 0;
        }
        i++;
    }

    private int getSightDirectionMultiplier(double value) {
        if (value == 0) {
            return 0;
        }
        if (value > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public float[] mViewMatrix() {
        return mViewMatrix;
    }
}
