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
import com.example.myapplication.model.Player;
import com.example.myapplication.views.MainActivity;

public class PlayerViewModel extends View {
    private Player player;
    private Bitmap bitmap;

    // creates a player sprite based on which character was chosen
    public PlayerViewModel(Context context, Player player) {
        super(context);
        this.player = player;

        switch (player.getCharacter()) {
            case "knight":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knight);
                break;
            case "elf":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elf);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lizard);
                break;
        }
    }

    // declaring size of bitmap
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // drawing bitmap of specific size
        int width = 100;
        int height = 175;
        float x = player.getX();
        float y = player.getY();
        Rect destRect = new Rect((int) x, (int) y, (int) (x + width), (int) (y + height));

        // flipping character depending on movement direction
        Matrix matrix = new Matrix();
        int centerX = bitmap.getWidth() / 2;
        int centerY = bitmap.getHeight() / 2;
        if (!player.isFacingRight()) {
            matrix.setScale(-1, 1, centerX, centerY);
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(newBitmap, null, destRect, null);
    }

    // updating the animation of the character based on the character chosen
    public void updateAnimation(int animationCount, boolean movement) {
        switch (animationCount) {
            case 0:
                switch (MainActivity.getCharacter()) {
                    case "knight":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knight_0);
                        break;
                    case "elf":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elf_0);
                        break;
                    default:
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lizard_0);
                        break;
                }
                break;
            case 1:
                switch (MainActivity.getCharacter()) {
                    case "knight":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knight_1);
                        break;
                    case "elf":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elf_1);
                        break;
                    default:
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lizard_1);
                        break;
                }
                break;
            case 2:
                switch (MainActivity.getCharacter()) {
                    case "knight":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knight_2);
                        break;
                    case "elf":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elf_2);
                        break;
                    default:
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lizard_2);
                        break;
                }
                break;
            case 3:
                switch (MainActivity.getCharacter()) {
                    case "knight":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knight_3);
                        break;
                    case "elf":
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.elf_3);
                        break;
                    default:
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lizard_3);
                        break;
                }
                break;
        }
        invalidate();
    }
}
