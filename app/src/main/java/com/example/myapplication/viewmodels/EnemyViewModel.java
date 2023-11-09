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

        // drawing bitmap of specific size
        int width = 100;
        int height = 175;
        float x = enemy.getX();
        float y = enemy.getY();
        Matrix matrix = new Matrix();
        Rect destRect = new Rect((int) x, (int) y, (int) (x + width), (int) (y + height));

        // flipping character depending on movement direction
        Bitmap newBitmap = Bitmap.createBitmap(enemyBitmap, 0, 0, enemyBitmap.getWidth(), enemyBitmap.getHeight(), matrix, true);
        canvas.drawBitmap(newBitmap, null, destRect, null);
    }
}
