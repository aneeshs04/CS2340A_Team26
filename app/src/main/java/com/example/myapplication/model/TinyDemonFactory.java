package com.example.myapplication.model;

public class TinyDemonFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(String type) {
        return new ImpDemon();
    }
}
