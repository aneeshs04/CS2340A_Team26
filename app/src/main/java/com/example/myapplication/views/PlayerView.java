package com.example.myapplication.views;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import com.example.myapplication.R;

public class PlayerView extends View {
    private float x, y;
    private final Bitmap bitmap;

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // drawing bitmap of specific size
        int width = 100;
        int height = 175;
        Rect destRect = new Rect((int) x, (int) y, (int) (x + width), (int) (y + height));
        canvas.drawBitmap(bitmap, null, destRect, null);
    }

    public void updatePosition(float newX, float newY) {
        x = newX;
        y = newY;
        invalidate();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
