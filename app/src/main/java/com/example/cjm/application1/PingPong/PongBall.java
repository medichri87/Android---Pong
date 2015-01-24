package com.example.cjm.application1.PingPong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

import com.example.cjm.application1.R;

import java.util.Random;

/**
 * Created by cj on 1/1/2015.
 */
class PongBall {
    float x, y, dx, dy, width;

    private RectF bound;
    private int color;
    private boolean centerScreen;
    private Random rand;
    private int direction;
    private boolean offScreen;
    private Paint paint;
    private float factor;

    //Necessary for SharedPreferences
    private Context context;

    //View the ball appears in
    private View view;

    PongBall(int width, PingPong v) {
        this.view = v;
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setTypeface(Typeface.SERIF);
        paint.setColor(Color.WHITE);

        this.bound = new RectF();
        this.rand = new Random();
        calcDirectionOutput();
        this.view = v;
        this.width = width;
        this.x = ((v.getMeasuredWidth() / 2) - (width / 2));
        this.y = ((v.getMeasuredHeight() / 2) - (width / 2));
        this.color = Color.YELLOW;
        this.centerScreen = true;
        this.offScreen = false;

        Context c = Settings.getSettingsContext();
        int id;
        if (Settings.isSettingsAltered())
            id = c.getSharedPreferences("SHARED", Context.MODE_MULTI_PROCESS).getInt("RG1", 1);
        else
            id = R.id.settings_ball_slow;

        factor = (float) (id == R.id.settings_ball_slow ? 0.6 : (id == R.id.settings_ball_moderate ? 0.8:
                1.1));

        this.dx = 6 * factor;
        this.dy = 5 * factor;
    }

    private void calcDirectionOutput() {
        direction = rand.nextInt(3);
        switch (direction) {
            case 0:
                this.dx = -this.dx;
                break;
            case 1:
                this.dy = -this.dy;
                break;
            case 2:
                this.dx = -this.dx;
                this.dy = -this.dy;
                break;
        }
    }

    public void setOffScreen(boolean offScreen) {
        this.offScreen = offScreen;
    }

    public boolean isOffScreen() {
        return offScreen;
    }

    public void checkBounds() {
        float screenWidth = view.getMeasuredWidth();
        float screenHeight = view.getMeasuredHeight();

        int xMin = 0, yMin = 0, xMax = (int) screenWidth, yMax = (int) screenHeight;

        //LEFT or RIGHT out of bounds
        if (this.x < xMin || this.x > xMax) {
            offScreen = true;
            setCenterScreen(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //TOP
        if (this.y - this.width < yMin) {
            this.y = yMin + this.width;
            this.dy = -this.dy;
            //BOTTOM
        } else if (this.y + this.width > yMax) {
            this.y = yMax - this.width;
            this.dy = -this.dy;
        }
    }

    public void setCenterScreen(boolean centerScreen) {
        this.centerScreen = centerScreen;
    }

    public boolean isCenterScreen() {
        return centerScreen;
    }

    public void update() {
        checkBounds();
        this.x += this.dx;
        this.y += this.dy;
        setBounds(x - width, y - width, x + width, y + width);
    }

    public void drawBall(Canvas c) {
        c.drawOval(bound, paint);
    }

    public void centerBall() {
        this.x = ((view.getMeasuredWidth() / 2) - (width / 2));
        this.y = ((view.getMeasuredHeight() / 2) - (width / 2));
        calcDirectionOutput();
        setCenterScreen(false);
    }

    public void setBounds(float left, float top, float right, float bottom) {
        this.bound.set(left, top, right, bottom);
    }

    public RectF getBounds() {
        return bound;
    }

    public int getColor() {
        return color;
    }

    public float getWidth() {
        return width;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
