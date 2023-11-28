package com.example.myapplication.model;

import com.example.myapplication.model.MovementStrategy;
import com.example.myapplication.model.Player;

public class MoveRightStrategy implements MovementStrategy {

    @Override
    public void move(Player player) {
        player.setX((float)(player.getX() + (50 * player.getSpeedMultiplier())));
    }


}

