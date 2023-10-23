package com.example.myapplication.model;

import com.example.myapplication.model.MovementStrategy;
import com.example.myapplication.model.Player;

public class MoveLeftStrategy implements MovementStrategy {
    @Override
    public void move(Player player) {
        player.setX(player.getX() - 50);
    }
}
