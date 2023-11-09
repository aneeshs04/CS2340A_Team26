package com.example.myapplication.model;

public class ChortFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(int x, int y) {
        return new ChortDemon(x, y);
    }
}
