package com.example.myapplication.model;

public class BigDemonFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(String type) {
        return new BigDemon();
    }
}
