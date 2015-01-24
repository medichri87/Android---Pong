package com.example.cjm.application1.PingPong;

import android.graphics.RectF;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by cj on 1/3/2015.
 */
public class PaddleController implements View.OnTouchListener {
    private boolean isUp;
    private boolean isDown;
    private Paddle paddle;

    public PaddleController(Paddle paddle) {
        this.paddle = paddle;
        this.isUp = false;
        this.isDown = false;
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                setUp(true);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                setDown(true);
                break;
        }
    }

    public void onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                setUp(false);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                setDown(false);
                break;
        }
    }

    public void setDown(boolean isDown) {
        this.isDown = isDown;
    }

    public void setUp(boolean isUp) {
        this.isUp = isUp;
    }

    public boolean isUp() {
        return isUp;
    }

    public boolean isDown() {
        return isDown;
    }

    @Override
    //FIX ISSUE OF OUT OF BOUNDS WITH TOUCH MOTION!!!! -----------------------------
    public boolean onTouch(View v, MotionEvent event) {
        RectF temp = new RectF(0, paddle.getBitmap().getHeight() - paddle.dy,
                paddle.getBitmap().getWidth(), paddle.getBitmap().getHeight() + paddle.dy);
        int action = event.getAction();
        float eX = event.getRawX();
        float eY = event.getRawY();

        if (action == MotionEvent.ACTION_MOVE) {
            this.paddle.y = event.getRawY();
        }
        return true;
    }
}
