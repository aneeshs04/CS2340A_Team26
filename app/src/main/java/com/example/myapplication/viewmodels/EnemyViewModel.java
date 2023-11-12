package com.example.myapplication.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.model.Enemy;
import com.example.myapplication.views.MainActivity;

public class EnemyViewModel extends View {
    private Enemy enemy;
    private Bitmap enemyBitmap;

    // Initialize the ViewModel with the bitmap of the enemy.
    public EnemyViewModel(Context context, Enemy enemy) {
        super(context);
        this.enemy = enemy;

        switch (enemy.getName()) {
            case "necromancer demon":
                this.enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.necromancer_demon);
                break;
            case "chort demon":
                this.enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chort_demon);
                break;
            case "imp demon":
                this.enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imp_demon);
                break;
            default:
                this.enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_demon);
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width;
        int height;

        switch (enemy.getName()) {
            case "necromancer demon":
                width = 100;
                height = 175;
                break;
            case "chort demon":
                width = 125;
                height = 175;
                break;
            case "imp demon":
                width = 100;
                height = 120;
                break;
            default:
                width = 200;
                height = 250;
                break;
        }

        // drawing bitmap of specific size

        float x = enemy.getX();
        float y = enemy.getY();
        Rect destRect = new Rect((int) x, (int) y, (int) (x + width), (int) (y + height));

        // flipping character depending on movement direction
        Matrix matrix = new Matrix();
        int centerX = enemyBitmap.getWidth() / 2;
        int centerY = enemyBitmap.getHeight() / 2;
        if (enemy.getDirection().equals("left")) {
            matrix.setScale(-1, 1, centerX, centerY);
        }
        Bitmap newBitmap = Bitmap.createBitmap(enemyBitmap, 0, 0, enemyBitmap.getWidth(), enemyBitmap.getHeight(), matrix, true);
        canvas.drawBitmap(newBitmap, null, destRect, null);
    }

    // updating the animation of the character based on the character chosen
    public void updateAnimation(int animationCount) {
        switch (animationCount) {
            case 0:
                switch (enemy.getName()) {
                    case "necromancer demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.necromancer_anim_f0);
                        break;
                    case "chort demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chort_run_anim_f0);
                        break;
                    case "imp demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imp_run_anim_f0);
                        break;
                    default:
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_demon_run_anim_f0);
                        break;
                }
                break;
            case 1:
                switch (enemy.getName()) {
                    case "necromancer demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.necromancer_anim_f1);
                        break;
                    case "chort demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chort_run_anim_f1);
                        break;
                    case "imp demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imp_run_anim_f1);
                        break;
                    default:
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_demon_run_anim_f1);
                        break;
                }
                break;
            case 2:
                switch (enemy.getName()) {
                    case "necromancer demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.necromancer_anim_f2);
                        break;
                    case "chort demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chort_run_anim_f2);
                        break;
                    case "imp demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imp_run_anim_f2);
                        break;
                    default:
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_demon_run_anim_f2);
                        break;
                }
            case 3:
                switch (enemy.getName()) {
                    case "necromancer demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.necromancer_anim_f3);
                        break;
                    case "chort demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chort_run_anim_f3);
                        break;
                    case "imp demon":
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imp_run_anim_f3);
                        break;
                    default:
                        enemyBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_demon_run_anim_f3);
                        break;
                }
                break;
        }
        invalidate();
    }
}
