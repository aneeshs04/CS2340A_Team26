package com.example.myapplication.model;

import android.os.CountDownTimer;

public class TimeCountdown extends CountDownTimer {

    private Player player = Player.getInstance();
    private static TimeCountdown instance;
    private OnTimeChangeListener onTimeChangeListener;

    private TimeCountdown(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public static TimeCountdown getInstance(long millisInFuture, long countDownInterval) {
        if (instance == null) {
            instance = new TimeCountdown(millisInFuture, countDownInterval);
        }
        return instance;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        player.setTime(player.getTime() +  1);
        if (onTimeChangeListener != null) {
            onTimeChangeListener.onTimeChange(player.getTime());
        }
    }
    @Override
    public void onFinish() {
        // do something when countdown is over
    }

    public interface OnTimeChangeListener {
        void onTimeChange(int newTime);
    }

    public void setOnTimeChangeListener(OnTimeChangeListener listener) {
        this.onTimeChangeListener = listener;
    }
}
