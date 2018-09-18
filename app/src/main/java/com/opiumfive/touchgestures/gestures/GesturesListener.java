package com.opiumfive.touchgestures.gestures;

public interface GesturesListener {
    void onSwipeLeft();
    void onSwipeRight();
    void onSwipeUp();
    void onSwipeDown();
    void onRightSwipeStep();
    void onLeftSwipeStep();
    void onThreeFingersTap();
    void onRotation(float angleDegrees);
    void onScale(float scaleFactor);
}
