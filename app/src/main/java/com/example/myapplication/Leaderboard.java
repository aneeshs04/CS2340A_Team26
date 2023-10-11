package com.example.myapplication;

import java.util.ArrayList;

public class Leaderboard {
    private volatile static Leaderboard single_instance = null;
    final private int scoreCount;
    final private ArrayList<Integer> scoreList;
    final private ArrayList<String> dateList;
    final private ArrayList<String> nameList;
    private Leaderboard() {
        scoreCount = 5;
        scoreList = new ArrayList<>();
        dateList = new ArrayList<>();
        nameList = new ArrayList<>();
    }

    public static synchronized Leaderboard getInstance() {
        if (single_instance == null) {
            single_instance = new Leaderboard();
        }
        return single_instance;
    }

    public void add(String name, String date, int score) {
        if (scoreList.size() == 0) {
            scoreList.add(score);
            dateList.add(date);
            nameList.add(name);
            return;
        }
        if (scoreList.size() < scoreCount) {
            for (int i = 0; i < scoreList.size(); i++) {
                if (scoreList.get(i) <= score) {
                    scoreList.add(i, score);
                    dateList.add(i, date);
                    nameList.add(i, name);
                    return;
                }
            }
            scoreList.add(score);
            dateList.add(date);
            nameList.add(name);
        } else {
            if (score >= scoreList.get(scoreCount - 1)) {
                scoreList.remove(scoreCount - 1);
                dateList.remove(scoreCount - 1);
                nameList.remove(scoreCount - 1);
                for (int i = 0; i < scoreList.size(); i++) {
                    if (scoreList.get(i) <= score) {
                        scoreList.add(i, score);
                        dateList.add(i, date);
                        nameList.add(i, name);
                        return;
                    }
                }
            }
        }
    }

    public ArrayList<Integer> getScoreList() {
        return scoreList;
    }
    public ArrayList<String> getNameList() {
        return nameList;
    }
    public ArrayList<String> getDateList() {
        return dateList;
    }
}
