package com.example.myapplication.model;

public class Wall {
    int left, top, right, bottom;

    public Wall(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }
    public float getBottom() {
        return bottom;
    }

    public void setLeft(int left) {
        this.left = left;
    }
    public void setRight(int right) {
        this.right = right;
    }
    public void setTop(int top) {
        this.top = top;
    }
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }


    public boolean collidesWith(int x, int y) {
        return x > left && x < right && y > top && y < bottom;
    }
}

