package com.example.cjm.application1.PingPong;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by cj on 1/5/2015.
 */
public final class Sound {
    private MediaPlayer mp;

    public Sound(Context context, int resid) {
        this.mp = new MediaPlayer();
        this.mp = MediaPlayer.create(context, resid);
        this.mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void play() {
        this.mp.start();
    }

    public void release() {
        this.mp.release();
    }

    public void stop() {
        this.mp.stop();
    }

}
