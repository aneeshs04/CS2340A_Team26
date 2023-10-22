package com.example.myapplication.views;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;
import com.example.myapplication.R;

public class PlayerView extends View {
    private float x, y;
    private Bitmap bitmap;
    private boolean isFacingRight = true;

    // creates a player sprite based on which character was chosen
    public PlayerView(Context context, float x, float y) {
        super(context);
        this.x = x;
        this.y = y;

        switch (MainActivity.getCharacter()) {
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
        Rect destRect = new Rect((int) x, (int) y, (int) (x + width), (int) (y + height));

        // flipping character depending on movement direction
        Matrix matrix = new Matrix();
        int centerX = bitmap.getWidth() / 2;
        int centerY = bitmap.getHeight() / 2;
        if (!isFacingRight) {
            matrix.setScale(-1, 1, centerX, centerY);
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(newBitmap, null, destRect, null);
    }

    // updating the position of the character as it moves
    public void updatePosition(float newX, float newY) {
        x = newX;
        y = newY;
        invalidate();
    }

    // updating the animation of the character based on the character chosen
    public void updateAnimation(int animationCount) {
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

    // getters/setters for other classes to access/modify private variables
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setCharacterDirection(boolean isFacingRight) {
        this.isFacingRight = isFacingRight;
    }
}
