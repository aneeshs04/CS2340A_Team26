package com.example.myapplication;

import com.example.myapplication.model.Leaderboard;
import com.example.myapplication.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExampleUnitTest {

    @Test
    public void testStartingScoreIs100() {
        Player player = Player.getInstance();
        assertEquals(100, player.getScore());
    }

    @Test
    public void testSetScore() {
        Player player = Player.getInstance();
        player.setScore(200);
        assertEquals(200, player.getScore());
    }

    @Test
    public void testSetHealth() {
        Player player = Player.getInstance();
        player.setHealth(75);
        assertEquals(75, player.getHealth());
    }


    @Test
    public void testPlayerIsSingleton() {
        Player player1 = Player.getInstance();
        Player player2 = Player.getInstance();
        assertEquals(player1, player2);
    }

    @Test
    public void testLeaderboardIsSingleton() {
        Leaderboard leaderboard1 = Leaderboard.getInstance();
        Leaderboard leaderboard2 = Leaderboard.getInstance();
        assertEquals(leaderboard1, leaderboard2);
    }

    @Test
    public void testLeaderboardImplementationName() {
        Leaderboard leaderboard = Leaderboard.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        int month = currentTime.getMonth() + 1;
        int day = currentTime.getDate();
        String date = month + "/" + day;
        leaderboard.add("Aneesh", date, 85);
        leaderboard.add("Danny", date, 75);
        leaderboard.add("Rishit", date, 25);
        ArrayList exampleNameList = new ArrayList<>();
        exampleNameList.add("Aneesh");
        exampleNameList.add("Danny");
        exampleNameList.add("Rishit");
        assertEquals(exampleNameList, leaderboard.getNameList());
    }

    @Test
    public void testLeaderboardImplementationScore() {
        Leaderboard leaderboard = Leaderboard.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        int month = currentTime.getMonth() + 1;
        int day = currentTime.getDate();
        String date = month + "/" + day;
        ArrayList exampleScoreList = new ArrayList<>();
        exampleScoreList.add(85);
        exampleScoreList.add(75);
        exampleScoreList.add(25);
        assertEquals(exampleScoreList, leaderboard.getScoreList());
    }
    @Test
    public void testSetDifficulty() {
        Player player = Player.getInstance();
        player.setDifficulty("medium");
        assertEquals("medium", player.getDifficulty());

        player.setDifficulty("hard");
        assertEquals("hard", player.getDifficulty());
    }

    @Test
    public void testDefaultDifficultyIsEasy() {
        Player player = Player.getInstance();
        assertEquals("easy", player.getDifficulty());
    }

    @Test
    public void testHealthHardDifficulty() {
        Player player = Player.getInstance();
        player.setDifficulty("hard");
        assertEquals(50, player.getHealth());
    }

    @Test
    public void testHealthMediumDifficulty() {
        Player player = Player.getInstance();
        player.setDifficulty("medium");
        assertEquals(100, player.getHealth());
    }

    @Test
    public void testHealthEasyDifficulty() {
        Player player = Player.getInstance();
        player.setDifficulty("easy");
        assertEquals(150, player.getHealth());
    }

}