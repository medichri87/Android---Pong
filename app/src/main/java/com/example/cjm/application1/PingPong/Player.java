package com.example.cjm.application1.PingPong;

/**
 * Created by cj on 1/3/2015.
 */
public class Player {
    private String title;
    private int score;
    private Paddle paddle;

    public Player(String title, Paddle p) {
        this.title = title;
        this.score = 0;
        this.paddle = p;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void resetScore() {
        this.score = 0;
    }
}
