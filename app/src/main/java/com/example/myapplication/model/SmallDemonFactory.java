package com.example.myapplication.model;

public class SmallDemonFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(String type) {
        return new ChortDemon();
    }
}
