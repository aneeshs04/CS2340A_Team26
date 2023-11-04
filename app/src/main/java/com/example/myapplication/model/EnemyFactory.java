package com.example.myapplication.model;

public abstract class EnemyFactory {
    public Enemy generateEnemy(EnemyType type) {
        switch (type) {
            case ENEMY_ONE:
                return new Enemy1();
            case ENEMY_TWO:
                return new Enemy2();
            case ENEMY_THREE:
                return new Enemy3();
            case ENEMY_FOUR:
                return new Enemy4();
            default:
                return null;
        }
    }
}
