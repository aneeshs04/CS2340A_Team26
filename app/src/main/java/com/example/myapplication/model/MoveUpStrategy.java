package com.example.myapplication.model;

import com.example.myapplication.model.MovementStrategy;
import com.example.myapplication.model.Player;

public class MoveUpStrategy implements MovementStrategy {

    @Override
    public void move(Player player) {
        player.setY(player.getY() - 50);
    }

}

