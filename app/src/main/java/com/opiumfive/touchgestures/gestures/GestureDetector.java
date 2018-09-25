package com.opiumfive.touchgestures.gestures;

import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_UP;

public class GestureDetector {

    private static final float DEFAULT_SCALE_DEBOUNCE_DELTA = 0.1f;
    private static final float DEFAULT_ROTATION_DEBOUNCE_DELTA = 5f;

    @NonNull
    private GesturesListener mGesturesListener;
    private GestureDetectorCompat mSwipeDetector;
    private OnSwipeEventListener mOnSwipeEventListener;
    private ScaleGestureDetector mScaleDetector;
    private RotationGestureDetector mRotationDetector;
    private float mScaleDebounceDelta = DEFAULT_SCALE_DEBOUNCE_DELTA;
    private float mRotationDebounceDelta = DEFAULT_ROTATION_DEBOUNCE_DELTA;
    private long mDownTimeMillis;
    private float mCurrentScaleFactor = 1f;
    private float mDebouncedScaleFactor = 1f;
    private boolean mIsStartedTrackingThreeTap = false;

    public GestureDetector(@NonNull Context context, @NonNull GesturesListener listener) {
        mGesturesListener = listener;
        mOnSwipeEventListener = new OnSwipeEventListener(mGesturesListener);
        mSwipeDetector = new GestureDetectorCompat(context, mOnSwipeEventListener);
        mSwipeDetector.setIsLongpressEnabled(false);
        mRotationDetector = new RotationGestureDetector(mGesturesListener, mRotationDebounceDelta);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                mCurrentScaleFactor *= detector.getScaleFactor();

                if (Math.abs(mCurrentScaleFactor - mDebouncedScaleFactor) >= mScaleDebounceDelta) {
                    mDebouncedScaleFactor = mCurrentScaleFactor;
                    mGesturesListener.onScale(mDebouncedScaleFactor);
                }

                return true;
            }
        });
    }

    public void setScaleDebounceDelta(float scaleDebounceDelta) {
        mScaleDebounceDelta = scaleDebounceDelta;
    }

    public void setRotationDebounceDelta(float rotationDebounceDelta) {
        mRotationDebounceDelta = rotationDebounceDelta;
        mRotationDetector.setDebounceDelta(mRotationDebounceDelta);
    }

    public void setSwipeStepNextItemThreshold(float threshold) {
        mOnSwipeEventListener.setNextItemThreshold(threshold);
    }

    public void putEvent(int action, float x, float y) {
        if (action == ACTION_DOWN) {
            mDownTimeMillis = SystemClock.uptimeMillis();
        }
        MotionEvent motionEvent = MotionEvent.obtain(mDownTimeMillis, SystemClock.uptimeMillis(), action, x, y, 0);
        onTouchEvent(motionEvent);
    }

    public void putEvent(MotionEvent motionEvent) {
        onTouchEvent(motionEvent);
    }

    private void onTouchEvent(MotionEvent event) {
        if (!trackOtherCustomEvents(event)) {
            if (!mSwipeDetector.onTouchEvent(event)) {
                mScaleDetector.onTouchEvent(event);
                mRotationDetector.onTouchEvent(event);
            }
        }
    }

    private boolean trackOtherCustomEvents(MotionEvent event) {
        switch (event.getActionMasked()) {
            case ACTION_POINTER_DOWN:
                if (event.getPointerCount() >= 3) {
                    mIsStartedTrackingThreeTap = true;
                }
                break;
            case ACTION_MOVE:
                mIsStartedTrackingThreeTap = false;
                break;
            case ACTION_POINTER_UP:
                if (mIsStartedTrackingThreeTap && event.getPointerCount() >= 3) {
                    mGesturesListener.onThreeFingersTap();
                }
                mIsStartedTrackingThreeTap = false;
                break;
        }

        boolean isTrackingOtherEvents = mIsStartedTrackingThreeTap; // add more here

        return isTrackingOtherEvents;
    }
}
