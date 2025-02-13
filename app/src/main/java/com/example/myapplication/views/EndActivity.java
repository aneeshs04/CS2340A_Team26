package com.example.myapplication.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.model.Leaderboard;
import com.example.myapplication.model.Player;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

public class EndActivity extends AppCompatActivity {
    private Player player = Player.getInstance();

    //opening the ending screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);
        TextView timeView = findViewById(R.id.textViewTime);
        timeView.setText("Time Spent: " + player.getTime() + " seconds");

        // changing end screen based on if player won or lost
        TextView statusTextView = findViewById(R.id.youwinText);
        if (player.getWon()) {
            statusTextView.setText("You Win!");
        } else {
            statusTextView.setText("You Lose!");
        }

        // leaderboard initializations
        TextView scoreView = findViewById(R.id.scoreRecentView);
        scoreView.setText(String.valueOf(player.getScore()));
        TextView nameView = findViewById(R.id.nameRecentView);
        nameView.setText(MainActivity.getName());

        Date currentTime = Calendar.getInstance().getTime();
        int month = currentTime.getMonth() + 1;
        int day = currentTime.getDate();
        String date = month + "/" + day;

        TextView dateView = findViewById(R.id.dateRecentView);
        dateView.setText(date);


        Leaderboard lb = Leaderboard.getInstance();
        lb.add(MainActivity.getName(), date, player.getScore());

        ArrayList<String> nameList = lb.getNameList();
        ArrayList<String> dateList = lb.getDateList();
        ArrayList<Integer> scoreList = lb.getScoreList();

        // modifying leaderboard visual to take in 5 different inputs
        if (scoreList.size() >= 1) {
            TextView leaderboardNameView1 = findViewById(R.id.leaderboardNameView1);
            leaderboardNameView1.setText(nameList.get(0));
            TextView leaderboardDateView1 = findViewById(R.id.leaderboardDateView1);
            leaderboardDateView1.setText(dateList.get(0));
            TextView leaderboardScoreView1 = findViewById(R.id.leaderboardScoreView1);
            leaderboardScoreView1.setText(scoreList.get(0).toString());
        }

        if (scoreList.size() >= 2) {
            TextView leaderboardNameView2 = findViewById(R.id.leaderboardNameView2);
            leaderboardNameView2.setText(nameList.get(1));
            TextView leaderboardDateView2 = findViewById(R.id.leaderboardDateView2);
            leaderboardDateView2.setText(dateList.get(1));
            TextView leaderboardScoreView2 = findViewById(R.id.leaderboardScoreView2);
            leaderboardScoreView2.setText(scoreList.get(1).toString());
        }

        if (scoreList.size() >= 3) {
            TextView leaderboardNameView3 = findViewById(R.id.leaderboardNameView3);
            leaderboardNameView3.setText(nameList.get(2));
            TextView leaderboardDateView3 = findViewById(R.id.leaderboardDateView3);
            leaderboardDateView3.setText(dateList.get(2));
            TextView leaderboardScoreView3 = findViewById(R.id.leaderboardScoreView3);
            leaderboardScoreView3.setText(scoreList.get(2).toString());
        }

        if (scoreList.size() >= 4) {
            TextView leaderboardNameView4 = findViewById(R.id.leaderboardNameView4);
            leaderboardNameView4.setText(nameList.get(3));
            TextView leaderboardDateView4 = findViewById(R.id.leaderboardDateView4);
            leaderboardDateView4.setText(dateList.get(3));
            TextView leaderboardScoreView4 = findViewById(R.id.leaderboardScoreView4);
            leaderboardScoreView4.setText(scoreList.get(3).toString());
        }

        if (scoreList.size() >= 5) {
            TextView leaderboardNameView5 = findViewById(R.id.leaderboardNameView5);
            leaderboardNameView5.setText(nameList.get(4));
            TextView leaderboardDateView5 = findViewById(R.id.leaderboardDateView5);
            leaderboardDateView5.setText(dateList.get(4));
            TextView leaderboardScoreView5 = findViewById(R.id.leaderboardScoreView5);
            leaderboardScoreView5.setText(scoreList.get(4).toString());
        }

        // restarts the game
        Button restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> {
            Intent restart = new Intent(EndActivity.this, StartActivity.class);
            startActivity(restart);
            finish();
        });
    }
}
