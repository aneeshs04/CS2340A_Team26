package com.example.myapplication.model;

public class NecromancerFactory extends EnemyFactory {
    @Override
    public Enemy createEnemy(String type) {
        return new NecromancerDemon();
    }
}
