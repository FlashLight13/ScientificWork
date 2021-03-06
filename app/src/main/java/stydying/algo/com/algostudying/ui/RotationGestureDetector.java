package stydying.algo.com.algostudying.ui;

import android.view.MotionEvent;

/**
 * Created by Anton on 10.02.2016.
 */
public class RotationGestureDetector {
    private static final int INVALID_POINTER_ID = -1;
    private float fX, fY, sX, sY;
    private int ptrID1, ptrID2;

    private float angle = 0;
    private float storedAngle = 0;
    private int rotationSensitiveLevel = 1;

    private OnRotationGestureListener mListener;

    public float getDeltaAngle() {
        return (storedAngle + angle) % 360;
    }

    public RotationGestureDetector setRotationSensitiveLevel(int rotationSensitiveLevel) {
        this.rotationSensitiveLevel = rotationSensitiveLevel;
        return this;
    }

    public RotationGestureDetector(OnRotationGestureListener listener) {
        mListener = listener;
        ptrID1 = INVALID_POINTER_ID;
        ptrID2 = INVALID_POINTER_ID;
    }

    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    ptrID1 = event.getPointerId(event.getActionIndex());
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    ptrID2 = event.getPointerId(event.getActionIndex());
                    if (ptrID2 != INVALID_POINTER_ID && ptrID1 != INVALID_POINTER_ID) {
                        sX = event.getX(event.findPointerIndex(ptrID1));
                        sY = event.getY(event.findPointerIndex(ptrID1));
                        fX = event.getX(event.findPointerIndex(ptrID2));
                        fY = event.getY(event.findPointerIndex(ptrID2));
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (ptrID1 != INVALID_POINTER_ID && ptrID2 != INVALID_POINTER_ID) {
                        float nfX, nfY, nsX, nsY;
                        nsX = event.getX(event.findPointerIndex(ptrID1));
                        nsY = event.getY(event.findPointerIndex(ptrID1));
                        nfX = event.getX(event.findPointerIndex(ptrID2));
                        nfY = event.getY(event.findPointerIndex(ptrID2));

                        angle = angleBetweenLines(fX, fY, sX, sY, nfX, nfY, nsX, nsY) / rotationSensitiveLevel;

                        if (mListener != null) {
                            mListener.OnRotation(this);
                            return true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    ptrID1 = INVALID_POINTER_ID;
                    storedAngle += angle;
                    angle = 0;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    storedAngle += angle;
                    angle = 0;
                    ptrID2 = INVALID_POINTER_ID;
                    break;
                case MotionEvent.ACTION_CANCEL:
                    ptrID1 = INVALID_POINTER_ID;
                    ptrID2 = INVALID_POINTER_ID;
                    storedAngle += angle;
                    angle = 0;
                    break;
            }
        } catch (Exception e) {
            // silently catch exception
        }
        return event.getPointerCount() == 2;
    }

    private float angleBetweenLines(float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float angle1 = (float) Math.atan2((fY - sY), (fX - sX));
        float angle2 = (float) Math.atan2((nfY - nsY), (nfX - nsX));

        float angle = ((float) Math.toDegrees(angle1 - angle2)) % 360;
        if (angle < -180.f) angle += 360.0f;
        if (angle > 180.f) angle -= 360.0f;
        return angle;
    }

    public interface OnRotationGestureListener {
        void OnRotation(RotationGestureDetector rotationDetector);
    }
}
