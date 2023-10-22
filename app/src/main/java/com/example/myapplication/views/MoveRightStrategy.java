package com.example.myapplication.views;

import com.example.myapplication.model.Player;

public class MoveRightStrategy implements MovementStrategy {
    @Override
    public void move(Player player) {
        player.setX(player.getX() + 50);
    }
}

