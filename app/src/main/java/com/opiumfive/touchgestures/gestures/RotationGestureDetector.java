package com.opiumfive.touchgestures.gestures;

import android.view.MotionEvent;

public class RotationGestureDetector {

    private static final int INVALID_POINTER_ID = -1;
    private static final float MAX_DEGREES_VALUE = 360f;
    private static final float HALF_MAX_DEGREES_VALUE = MAX_DEGREES_VALUE / 2;

    private float mFirstPointerX, mFirstPointerY;
    private float mSecondPointerX, mSecondPointerY;
    private int mPointerId1, mPointerId2;
    private float mAngle;
    private float mDebounceDelta;

    private GesturesListener mListener;

    public RotationGestureDetector(GesturesListener listener, float debounceDelta) {
        mListener = listener;
        mDebounceDelta = debounceDelta;
        mPointerId1 = INVALID_POINTER_ID;
        mPointerId2 = INVALID_POINTER_ID;
    }

    public float getAngle() {
        return mAngle;
    }

    public void setDebounceDelta(float debounceDelta) {
        mDebounceDelta = debounceDelta;
    }

    public void onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mPointerId1 = event.getPointerId(event.getActionIndex());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointerId2 = event.getPointerId(event.getActionIndex());
                mSecondPointerX = event.getX(event.findPointerIndex(mPointerId1));
                mSecondPointerY = event.getY(event.findPointerIndex(mPointerId1));
                mFirstPointerX = event.getX(event.findPointerIndex(mPointerId2));
                mFirstPointerY = event.getY(event.findPointerIndex(mPointerId2));
                break;
            case MotionEvent.ACTION_MOVE:
                if (mPointerId1 != INVALID_POINTER_ID && mPointerId2 != INVALID_POINTER_ID) {
                    float nfX, nfY, nsX, nsY;
                    nsX = event.getX(event.findPointerIndex(mPointerId1));
                    nsY = event.getY(event.findPointerIndex(mPointerId1));
                    nfX = event.getX(event.findPointerIndex(mPointerId2));
                    nfY = event.getY(event.findPointerIndex(mPointerId2));

                    float newAngle = angleBetweenLines(mFirstPointerX, mFirstPointerY, mSecondPointerX, mSecondPointerY, nfX, nfY, nsX, nsY);
                    if (Math.abs(mAngle - newAngle) >= mDebounceDelta) {
                        mAngle = newAngle;
                        if (mListener != null) {
                            mListener.onRotation(mAngle);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mPointerId1 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mPointerId2 = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
                mPointerId1 = INVALID_POINTER_ID;
                mPointerId2 = INVALID_POINTER_ID;
                break;
        }
    }

    private float angleBetweenLines(float fX, float fY, float sX, float sY, float nfX, float nfY, float nsX, float nsY) {
        float angle1 = (float) Math.atan2((fY - sY), (fX - sX));
        float angle2 = (float) Math.atan2((nfY - nsY), (nfX - nsX));

        float angle = ((float) Math.toDegrees(angle1 - angle2)) % MAX_DEGREES_VALUE;
        if (angle < -HALF_MAX_DEGREES_VALUE) angle += MAX_DEGREES_VALUE;
        if (angle > HALF_MAX_DEGREES_VALUE) angle -= MAX_DEGREES_VALUE;
        return angle;
    }
}
