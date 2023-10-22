package com.example.myapplication.views;

import com.example.myapplication.model.Player;

public class MoveDownStrategy implements MovementStrategy {
    @Override
    public void move(Player player) {
        player.setY(player.getY() + 50);
    }
}
