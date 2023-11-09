package com.example.myapplication.model;

public class BigDemonFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(int x, int y) {
        return new BigDemon(x, y);
    }
}
