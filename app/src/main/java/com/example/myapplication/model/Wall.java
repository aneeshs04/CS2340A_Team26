package com.example.myapplication.model;

public class Wall {
    int left, top, right, bottom;

    public Wall(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public boolean collidesWith(int x, int y) {
        return x > left && x < right && y > top && y < bottom;
    }
}

