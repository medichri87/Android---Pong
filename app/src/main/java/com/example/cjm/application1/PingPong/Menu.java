package com.example.cjm.application1.PingPong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cjm.application1.R;

/**
 * Created by cj on 1/7/2015.
 */
public class Menu extends Activity {
    private final Context context = this;
    private Button start, settings, instructions, exit;
    private ImageView img;
    private AnimationDrawable anim;
    private int viewWidth, viewHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.img = (ImageView) findViewById(R.id.img_view);
        this.img.setBackgroundResource(R.drawable.f1);

        //this.img.setBackgroundResource(R.drawable.animation);
        //this.anim = (AnimationDrawable) img.getBackground();

        this.start = (Button) findViewById(R.id.start);
        this.settings = (Button) findViewById(R.id.settings);
        this.instructions = (Button) findViewById(R.id.instructions);
        this.exit = (Button) findViewById(R.id.exit);

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RunMovingBall.class);
                startActivity(intent);
            }
        });

        this.settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Settings.class);
                startActivity(intent);
            }
        });

        this.instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Instructions.class);
                startActivity(intent);
            }
        });

        this.exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        //anim.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        this.viewHeight = getWindow().getDecorView().getMeasuredHeight();
        this.viewWidth = getWindow().getDecorView().getMeasuredHeight();
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }
}
