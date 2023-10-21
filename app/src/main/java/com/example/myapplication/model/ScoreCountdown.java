package com.example.myapplication.model;

import android.os.CountDownTimer;

public class ScoreCountdown extends CountDownTimer {
    private Player player = Player.getInstance();
    private static ScoreCountdown instance;
    private OnScoreChangeListener onScoreChangeListener;

    private ScoreCountdown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public static ScoreCountdown getInstance(long millisInFuture, long countDownInterval) {
        if (instance == null) {
            instance = new ScoreCountdown(millisInFuture, countDownInterval);
        }
        return instance;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (player.getScore() > 0) {
            player.setScore(player.getScore() - 5);
        }
        if (onScoreChangeListener != null) {
            onScoreChangeListener.onScoreChange(player.getScore());
        }

    }

    @Override
    public void onFinish() {
        // Do something when countdown is over
    }

    public interface OnScoreChangeListener {
        void onScoreChange(int newScore);
    }

    public void setOnScoreChangeListener(OnScoreChangeListener listener) {
        this.onScoreChangeListener = listener;
    }
}