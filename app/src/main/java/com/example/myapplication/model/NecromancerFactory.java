package com.example.myapplication.model;

public class NecromancerFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(int x, int y) {
        return new NecromancerDemon(x, y);
    }
}
