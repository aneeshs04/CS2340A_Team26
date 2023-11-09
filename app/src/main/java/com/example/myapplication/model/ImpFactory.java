package com.example.myapplication.model;

public class ImpFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(int x, int y) {
        return new ImpDemon(x, y);
    }
}
