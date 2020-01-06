package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import it.uniba.di.sms1920.giochiapp.R;

public class Obstacle implements GameObject {

    private Rect rectangle;
    private Rect rectangle2;
    private int color;

    Bitmap obstacleGreenImg;
    Bitmap obstacleOrangeImg;

    public Obstacle(int rectHeight, int color, int startX,int startY, int playerGap) {
        this.color = color;
        rectangle = new Rect(0, startY, startX, startY+rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY+rectHeight);

        obstacleGreenImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakeslime);
        obstacleOrangeImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakelava);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        obstacleGreenImg = Bitmap.createBitmap(obstacleGreenImg, 0, 0, obstacleGreenImg.getWidth(), obstacleGreenImg.getHeight(), m, false);
        obstacleOrangeImg = Bitmap.createBitmap(obstacleOrangeImg, 0, 0, obstacleOrangeImg.getWidth(), obstacleOrangeImg.getHeight(), m, false);

    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y) {
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    public boolean playerCollide(RectPlayer player) {
        return Rect.intersects(rectangle,player.getRectangle()) || Rect.intersects(rectangle2,player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawBitmap(obstacleGreenImg,null,rectangle, new Paint());
        canvas.drawBitmap(obstacleOrangeImg,null, rectangle2, new Paint());
    }

    @Override
    public void update() {

    }
}
