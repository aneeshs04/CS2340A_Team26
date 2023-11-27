package com.example.myapplication.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.R;
import com.example.myapplication.model.Weapon;
import com.example.myapplication.views.MainActivity;

import java.util.logging.Handler;

public class WeaponViewModel extends View {
    private Weapon weapon;
    private Bitmap bitmap;


    // creates a player sprite based on which character was chosen
    public WeaponViewModel(Context context, Weapon weapon) {
        super(context);
        this.weapon = weapon;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frame_1_delay_0_1s);
    }

    // declaring size of bitmap
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // drawing bitmap of specific size
        int width = 150;
        int height = 300;
        float x = weapon.getX();
        float y = weapon.getY();
        Rect destRect = weapon.getDestRect();

        // flipping character depending on movement direction
        Matrix matrix = new Matrix();

        if (weapon.getWeaponSwingDirection() == "left") {
            destRect = weapon.getDestRect();
            int centerX = bitmap.getWidth() / 2;
            int centerY = bitmap.getHeight() / 2;
            matrix.setScale(-1, 1, centerX, centerY);
        }
        if (weapon.getWeaponSwingDirection() == "up") {
            destRect = weapon.getDestRect();
            matrix.setRotate(-90, x, y + 20);
        }
        if (weapon.getWeaponSwingDirection() == "down") {
            destRect = weapon.getDestRect();
            matrix.setRotate(90, x, y + 20);
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(newBitmap, null, destRect, null);

    }

    public void updateAnimation(int x) {
        switch (x) {
            case 0:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frame_1_delay_0_1s);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frame_2_delay_0_05s);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frame_3_delay_0_05s);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frame_4_delay_0_05s);
                break;
            case 4:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frame_5_delay_0_05s);
                break;
            case 5:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frame_6_delay_0_1s);
                break;
        }
        invalidate();
    }


}