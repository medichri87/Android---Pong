package com.example.cjm.application1.PingPong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.View;

import com.example.cjm.application1.R;

/**
 * Created by cj on 1/3/2015.
 */
public class Paddle {
    private RectF bounds;
    private int color;
    float x, y, dy;
    private PaddleController controller;
    private Bitmap bitmap;
    private View v;
    private boolean ai;
    private int upCount = 0, aiCount = 0;

    public Paddle(boolean ai, View view) {
        this.ai = ai;
        this.v = view;
        this.controller = new PaddleController(this);
        this.bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.paddle);
        this.bounds = new RectF();
        this.dy = (ai ? 4 : 12);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public RectF getBounds() {
        return bounds;
    }

    public void update(View v) {
        if (upCount == 0) {
            this.x = 0;
            this.y = ((v.getMeasuredHeight() / 2) - (bitmap.getHeight() / 2));
        }
        if (controller.isUp()) {
            if (y - dy > 0)
                y -= dy;
        } else if (controller.isDown()) {
            if (y + bitmap.getHeight() + dy < v.getMeasuredHeight())
                y += dy;
        }
        upCount++;
        bounds.set(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void aiUpdate(View v, float yPos, float xPos) {
        if (aiCount == 0) {
            this.x = (v.getMeasuredWidth() - this.getBitmap().getWidth());
            this.y = ((v.getMeasuredHeight() / 2) - (bitmap.getHeight() / 2));
        }
        //Move towards ball (keep paddle middle in line with ball)
        float midPaddle = this.y + (bitmap.getHeight() / 2);

        if (this.y < 0) {
            this.y += dy;
        } else if (this.y + bitmap.getHeight() > v.getMeasuredHeight()) {
            this.y -= dy;
        } else if (yPos > midPaddle) {
            this.y += dy;
        } else if (yPos < midPaddle) {
            this.y -= dy;
        }

        aiCount++;
        bounds.set(v.getMeasuredWidth() - bitmap.getWidth(), y, v.getMeasuredWidth(), y + bitmap.getHeight());
    }

    public void setBounds(RectF bounds) {
        this.bounds = bounds;
    }

    public void drawPaddle(Canvas c) {
        if (!ai)
            c.drawBitmap(bitmap, x, y, null);
        else
            c.drawBitmap(bitmap, v.getMeasuredWidth() - bitmap.getWidth(), y, null);
    }

    public void centerPaddle() {
        this.y = (v.getMeasuredHeight() / 2) - (bitmap.getHeight() / 2);
        if (ai) {
            bounds.set(0, y - (bitmap.getHeight() / 2), bitmap.getWidth(), y + (bitmap.getHeight() / 2));
        } else {
            bounds.set(v.getMeasuredWidth() - bitmap.getWidth(), y - (bitmap.getHeight() / 2),
                    v.getMeasuredWidth(), y + (bitmap.getHeight() / 2));
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public PaddleController getController() {
        return controller;
    }

}
