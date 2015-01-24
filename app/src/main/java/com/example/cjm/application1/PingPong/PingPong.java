package com.example.cjm.application1.PingPong;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.cjm.application1.R;

/**
 * Created by cj on 1/3/2015.
 */
public class PingPong extends View {
    private PongBall ball;
    private PaddleController control;
    private Paint info, border;
    private Player p1, p2;
    private Sound hitSound, gameOverSound;
    private boolean isPaused;
    private int gameCount;
    private AlertDialog.Builder dialog;

    public PingPong(Context context) {
        super(context);

        this.p1 = new Player("Chris", new Paddle(false, this));
        this.p2 = new Player("A.I.", new Paddle(true, this));
        this.hitSound = new Sound(context, R.raw.pong);
        this.gameOverSound = new Sound(context, R.raw.t1_be_back);
        this.ball = new PongBall(10, this);
        this.ball.setCenterScreen(true);
        this.control = p1.getPaddle().getController();
        this.isPaused = false;

        this.info = new Paint();
        info.setDither(true);
        info.setAntiAlias(true);
        info.setTextSize(18f);
        info.setColor(Color.WHITE);
        info.setHinting(Paint.HINTING_ON);
        info.setAlpha(100);
        info.setStyle(Paint.Style.FILL_AND_STROKE);

        this.border = new Paint();
        this.border.setDither(true);
        this.border.setColor(Color.WHITE);
        this.border.setAlpha(100);
        this.border.setStyle(Paint.Style.FILL_AND_STROKE);
        this.border.setStrokeWidth(5.0f);

        setBackgroundColor(Color.BLACK);
        setFocusable(true);
        requestFocus();
        setOnTouchListener(control);

        int id;
        Context c = Settings.getSettingsContext();
        if (Settings.isSettingsAltered()) {
            id = c.getSharedPreferences("SHARED", Context.MODE_MULTI_PROCESS).getInt("RG2", 1);
        } else {
            id = R.id.settings_gameover_5;
        }

        this.gameCount = (id == R.id.settings_gameover_5 ? 5 : (id == R.id.settings_gameover_10 ? 10 : 25));

        //End-Game Dialog options
        this.dialog = new AlertDialog.Builder(context);
        this.dialog.setTitle("Play again?");
        this.dialog.setMessage("Do you want to play again? (Y/N)");
        this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                postInvalidate();
            }
        });
        this.dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                resetGame();
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    private void drawBorder(Canvas c) {
        info.setStrokeWidth(2.5f);
        //TOP
        c.drawLine(0, 0, getMeasuredWidth(), 0, border);

        //LEFT
        c.drawLine(0, 0, 0, getMeasuredHeight(), border);

        //RIGHT
        c.drawLine(getMeasuredWidth() - border.getStrokeWidth(), 0, getMeasuredWidth(),
                getMeasuredHeight(), border);

        //BOTTOM
        c.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), border);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String l1 = String.format("%d", p1.getScore());
        String l2 = String.format("%d", p2.getScore());
        int txtMeasure_l1 = (int) info.measureText(l1);
        int txtMeasure_l2 = (int) info.measureText(l2);

        int halfX = getMeasuredWidth() / 2;
        int halfY = getMeasuredHeight() / 2;

        int leftFourth = halfX - (halfX / 2);
        int rightFourth = halfX + (halfX / 2);

        info.setColor(Color.WHITE);
        info.setStrokeWidth(2f);

        info.setTextSize(14f);
        canvas.drawText(String.format("Ball Speed: %.2f/ %.2f, NTW: %d", ball.dx, ball.dy, gameCount), 50,
                100, info);

        info.setTextSize(32f);
        canvas.drawLine(halfX, 0, halfX, this.getMeasuredHeight(), info);
        canvas.drawText(l1, leftFourth - (txtMeasure_l1 / 2), 75, info);
        canvas.drawText(l2, rightFourth - (txtMeasure_l2 / 2), 75, info);
        drawBorder(canvas);

        //Handle game over
        if (gameOver()) {
            gameOverSound.play();
            dialog.show();
            Toast.makeText(getContext(), getWinner().getTitle() + " wins!", Toast.LENGTH_SHORT).show();
        } else
            invalidate();

        ball.drawBall(canvas);
        p1.getPaddle().drawPaddle(canvas);
        p2.getPaddle().drawPaddle(canvas);

        //Center ball to screen
        if (isPaused) {
            drawPauseScreen(canvas);
        } else {
            if (ball.isCenterScreen()) {
                if (ball.isOffScreen()) {
                    if (ball.x < 0) {
                        p2.setScore(p2.getScore() + 1);
                    } else if (ball.x + ball.getWidth() > this.getMeasuredWidth()) {
                        p1.setScore(p1.getScore() + 1);
                    }
                    centerProperties();
                    ball.setCenterScreen(false);
                    ball.setOffScreen(false);
                }
            }

            //Collision of ball with Paddle, play sound
            if (ball.getBounds().intersect(p1.getPaddle().getBounds()) || ball.getBounds().intersect(p2.getPaddle().getBounds()))
                hitSound.play();
            if (ball.getBounds().intersect(p1.getPaddle().getBounds())) {
                ball.x = (p1.getPaddle().x + p1.getPaddle().getBitmap().getWidth()) + ball.getWidth();
                ball.dx = -ball.dx;
            }

            //AI detection
            if (ball.getBounds().intersect(p2.getPaddle().getBounds())) {
                ball.x = (this.getMeasuredWidth() - p2.getPaddle().getBitmap().getWidth() - ball.getWidth());
                ball.dx = -ball.dx;
            }

            ball.update();
            p2.getPaddle().aiUpdate(this, ball.y, ball.x);
            p1.getPaddle().update(this);
        }

    }

    private void drawPauseScreen(Canvas canvas) {
        //Clear screen and print PAUSE in center of screen
        info.setColor(Color.RED);
        info.setTextSize(85);
        String s = "PAUSED";
        int txtMeasure = (int) info.measureText(s);
        int halfX = getMeasuredWidth() / 2;
        int halfY = getMeasuredHeight() / 2;

        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR);
        canvas.drawText(s, halfX - (txtMeasure / 2), halfY, info);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        control.onKeyDown(keyCode, event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_P:
                isPaused = !isPaused;
                break;
            case KeyEvent.KEYCODE_C:
                if (!isPaused)
                    centerProperties();
                break;
            case KeyEvent.KEYCODE_Q:
                System.exit(0);
                break;
            case KeyEvent.KEYCODE_R:
                resetGame();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        control.onKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    public void centerProperties() {
        p1.getPaddle().centerPaddle();
        p2.getPaddle().centerPaddle();
        ball.centerBall();
    }

    private void resetGame() {
        isPaused = false;
        centerProperties();
        p1.setScore(0);
        p2.setScore(0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean gameOver() {
        return (p1.getScore() == gameCount || p2.getScore() == gameCount);
    }

    private Player getWinner() {
        return (p1.getScore() > p2.getScore() ? p1 : p2);
    }

}
