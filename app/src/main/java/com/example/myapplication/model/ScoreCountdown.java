package com.example.myapplication.model;
import android.os.CountDownTimer;

public class ScoreCountdown extends CountDownTimer {
    private int score;
    private OnTickListener listener;

    public ScoreCountdown(long millisInFuture, long countDownInterval, int initialScore, OnTickListener listener) {
        super(millisInFuture, countDownInterval);
        this.score = initialScore;
        this.listener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        score -= 5;
        listener.onScoreUpdate(score);
    }

    @Override
    public void onFinish() {
        // Handle countdown finished if needed.
    }

    public interface OnTickListener {
        void onScoreUpdate(int score);
    }
}
