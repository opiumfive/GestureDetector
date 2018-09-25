package com.opiumfive.touchgestures.gestures;

import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class OnSwipeEventListener extends GestureDetector.SimpleOnGestureListener {

    private static final float NEXT_ITEM_THRESHOLD_DEFAULT = 100.0f;

    private float mScrollStartX = 0f;
    private float mScrollStartY = 0f;
    private int mCurrentItemXNumber = 0;
    private int mCurrentItemYNumber = 0;
    private float mNextItemThreshold = NEXT_ITEM_THRESHOLD_DEFAULT;

    @NonNull
    private GesturesListener mGestureListener;

    public OnSwipeEventListener(@NonNull GesturesListener gestureListener) {
        mGestureListener = gestureListener;
    }

    public OnSwipeEventListener(float nextItemThreshold, @NonNull GesturesListener gestureListener) {
        mGestureListener = gestureListener;
        mNextItemThreshold = nextItemThreshold;
    }

    public void setNextItemThreshold(float nextItemThreshold) {
        mNextItemThreshold = nextItemThreshold;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mScrollStartX = e.getX();
        mScrollStartY = e.getY();
        mCurrentItemXNumber = 0;
        mCurrentItemYNumber = 0;
        return super.onDown(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceX) > Math.abs(distanceY) && e2.getPointerCount() == 1) {
            int diffX = (int) Math.floor((e2.getX() - mScrollStartX) / mNextItemThreshold);

            if (diffX > mCurrentItemXNumber) {
                mGestureListener.onRightSwipeStep();
            } else if (diffX < mCurrentItemXNumber) {
                mGestureListener.onLeftSwipeStep();
            }

            mCurrentItemXNumber = diffX;
        } else if (Math.abs(distanceX) < Math.abs(distanceY) && e2.getPointerCount() == 1) {
            int diffY = (int) Math.floor((e2.getY() - mScrollStartY) / mNextItemThreshold);

            if (diffY > mCurrentItemYNumber) {
                mGestureListener.onBottomSwipeStep();
            } else if (diffY < mCurrentItemYNumber) {
                mGestureListener.onTopSwipeStep();
            }

            mCurrentItemYNumber = diffY;
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityX) > Math.abs(velocityY)) {
            if (e1.getX() < e2.getX()) {
                mGestureListener.onSwipeRight();
            } else if (e1.getX() > e2.getX()) {
                mGestureListener.onSwipeLeft();
            }
        } else {
            if (e1.getY() < e2.getY()) {
                mGestureListener.onSwipeDown();
            } else if (e1.getY() > e2.getY()) {
                mGestureListener.onSwipeUp();
            }
        }

        return true;
    }
}
