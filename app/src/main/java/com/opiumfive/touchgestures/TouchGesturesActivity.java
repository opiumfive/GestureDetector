package com.opiumfive.touchgestures;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.harman.psatouchgestures.R;
import com.opiumfive.touchgestures.gestures.GesturesListener;
import com.opiumfive.touchgestures.gestures.GestureDetector;

public class TouchGesturesActivity extends AppCompatActivity {

    private TextView mGestureText;
    private GestureDetector mGestureDetector;

    private GesturesListener mGesturesListener = new GesturesListener() {
        @Override
        public void onSwipeLeft() {
            addGestureText("onSwipeLeft");
        }

        @Override
        public void onSwipeRight() {
            addGestureText("onSwipeRight");
        }

        @Override
        public void onSwipeUp() {
            addGestureText("onSwipeUp");
        }

        @Override
        public void onSwipeDown() {
            addGestureText("onSwipeDown");
        }

        @Override
        public void onRightSwipeStep() {
            addGestureText("onRightSwipeStep");
        }

        @Override
        public void onLeftSwipeStep() {
            addGestureText("onLeftSwipeStep");
        }

        @Override
        public void onTopSwipeStep() {
            addGestureText("onTopSwipeStep");
        }

        @Override
        public void onBottomSwipeStep() {
            addGestureText("onBottomSwipeStep");
        }

        @Override
        public void onThreeFingersTap() {
            addGestureText("onThreeFingersTap");
        }

        @Override
        public void onRotation(float angleDegrees) {
            addGestureText("onRotation: " + angleDegrees);
        }

        @Override
        public void onScale(float scaleFactor) {
            addGestureText("onScale: " + scaleFactor);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_gestures);

        mGestureText = findViewById(R.id.gesture);
        mGestureDetector = new GestureDetector(this, mGesturesListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mGestureDetector.putEvent(event);
        return super.onTouchEvent(event);
    }

    private void addGestureText(String text) {
        if (mGestureText.getText().toString().split(System.getProperty("line.separator")).length > 13) {
            mGestureText.setText("");
        }
        mGestureText.append("\n" + text);
    }
}
