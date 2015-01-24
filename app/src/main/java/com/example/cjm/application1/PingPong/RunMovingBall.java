package com.example.cjm.application1.PingPong;

import android.app.Activity;
import android.os.Bundle;

import com.example.cjm.application1.PingPong.PingPong;

/**
 * Created by cj on 12/31/2014.
 */
public class RunMovingBall extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new PingPong(this));
    }
}
