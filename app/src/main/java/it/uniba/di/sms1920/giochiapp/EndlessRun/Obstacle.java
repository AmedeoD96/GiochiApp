package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import it.uniba.di.sms1920.giochiapp.R;

public class Obstacle implements GameObject {

    private Rect rectangle;
    private Rect rectangle2;
    private int color;

    Bitmap obstacleGreenImg;
    Bitmap obstacleOrangeImg;
    Bitmap obstacleGreenFlipped;
    Bitmap obstacleOrangeFlipped;

    Animation orangeMovement;
    Animation greenMovement;
    AnimationManager animationManagerGreen;
    AnimationManager animationManagerOrange;

    public Obstacle(int rectHeight, int color, int startX,int startY, int playerGap) {
        this.color = color;
        rectangle = new Rect(0, startY, startX, startY+rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY+rectHeight);

        obstacleGreenImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakeslime);
        obstacleOrangeImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakelava);

        obstacleGreenFlipped = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.greenflipped);
        obstacleOrangeFlipped = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.orangeflipped);

        Matrix m = new Matrix();
        Matrix m2 = new Matrix();
        m.preScale(-1, 1);
        m2.preScale(-1,1);
        obstacleGreenImg = Bitmap.createBitmap(obstacleGreenImg, 0, 0, obstacleGreenImg.getWidth(), obstacleGreenImg.getHeight(), m, false);
        obstacleOrangeImg = Bitmap.createBitmap(obstacleOrangeImg, 0, 0, obstacleOrangeImg.getWidth(), obstacleOrangeImg.getHeight(), m2, false);

        obstacleGreenFlipped = Bitmap.createBitmap(obstacleGreenFlipped, 0, 0, obstacleGreenFlipped.getWidth(), obstacleGreenFlipped.getHeight(), m, false);
        obstacleOrangeFlipped = Bitmap.createBitmap(obstacleOrangeFlipped, 0, 0, obstacleOrangeFlipped.getWidth(), obstacleOrangeFlipped.getHeight(), m2, false);

        Log.i("1","1");
        greenMovement = new Animation(new Bitmap[]{ obstacleGreenImg, obstacleGreenFlipped },0.5f, false);
        orangeMovement = new Animation(new Bitmap[] {obstacleOrangeImg, obstacleOrangeFlipped}, 0.5f, false);

        Log.i("2","2");
        animationManagerGreen = new AnimationManager(new Animation[] {greenMovement});
        animationManagerOrange = new AnimationManager(new Animation[] {orangeMovement});
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
        animationManagerGreen.draw(canvas,rectangle);
        animationManagerOrange.draw(canvas,rectangle2);
    }

    @Override
    public void update() {
        animationManagerOrange.update();
        animationManagerGreen.update();

        animationManagerOrange.playAnim(0);
        animationManagerGreen.playAnim(0);
        animationManagerGreen.update();
        animationManagerOrange.update();
    }
}
