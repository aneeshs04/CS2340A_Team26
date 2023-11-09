package com.example.myapplication.model;

public abstract class EnemyFactory {
//    public Enemy generateEnemy(EnemyType type) {
//        switch (type) {
//            case ENEMY_ONE:
//                return new Necromancer();
//            case ENEMY_TWO:
//                return new BigDemon();
//            case ENEMY_THREE:
//                return new SmallDemon();
//            case ENEMY_FOUR:
//                return new TinyDemon();
//            default:
//                return null;
//        }
//    }
    public abstract Enemy createEnemy(String type);
}
