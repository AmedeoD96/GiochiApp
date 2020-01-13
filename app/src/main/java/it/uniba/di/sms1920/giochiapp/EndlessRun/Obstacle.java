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

    private AnimationManager animationManagerGreen;
    private AnimationManager animationManagerOrange;

    Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        //vengono create le bitmap
        rectangle = new Rect(0, startY, startX, startY+rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY+rectHeight);

        Bitmap obstacleGreenImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakeslime);
        Bitmap obstacleOrangeImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.snakelava);

        Bitmap obstacleGreenFlipped = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.greenflipped);
        Bitmap obstacleOrangeFlipped = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.orangeflipped);

        Matrix m = new Matrix();
        Matrix m2 = new Matrix();
        m.preScale(-1, 1);
        m2.preScale(-1,1);
        obstacleGreenImg = Bitmap.createBitmap(obstacleGreenImg, 0, 0, obstacleGreenImg.getWidth(), obstacleGreenImg.getHeight(), m, false);
        obstacleOrangeImg = Bitmap.createBitmap(obstacleOrangeImg, 0, 0, obstacleOrangeImg.getWidth(), obstacleOrangeImg.getHeight(), m2, false);

        obstacleGreenFlipped = Bitmap.createBitmap(obstacleGreenFlipped, 0, 0, obstacleGreenFlipped.getWidth(), obstacleGreenFlipped.getHeight(), m, false);
        obstacleOrangeFlipped = Bitmap.createBitmap(obstacleOrangeFlipped, 0, 0, obstacleOrangeFlipped.getWidth(), obstacleOrangeFlipped.getHeight(), m2, false);

        //vengono create le animation
        Animation greenMovement = new Animation(new Bitmap[]{obstacleGreenImg, obstacleGreenFlipped}, 0.5f, false);
        Animation orangeMovement = new Animation(new Bitmap[]{obstacleOrangeImg, obstacleOrangeFlipped}, 0.5f, false);

        animationManagerGreen = new AnimationManager(new Animation[] {greenMovement});
        animationManagerOrange = new AnimationManager(new Animation[] {orangeMovement});
    }

    public Rect getRectangle() {
        return rectangle;
    }

    void incrementY(float y) {
        //si incrementano i lati del rettangolo di un float
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }

    boolean playerCollide(RectPlayer player) {
        //ritorna il boolean relativo all'intersezione del rettangolo utente con i rettangoli relativi agli ostacoli
        return Rect.intersects(rectangle,player.getRectangle()) || Rect.intersects(rectangle2,player.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        //si disegna l'animazione del canvas sui rettangoli degli ostacoli
        Paint paint = new Paint();
        paint.setColor(color);
        animationManagerGreen.draw(canvas,rectangle);
        animationManagerOrange.draw(canvas,rectangle2);
    }

    @Override
    public void update() {
        animationManagerOrange.update();
        animationManagerGreen.update();

        //viene attivata l'animazione
        animationManagerOrange.playAnim(0);
        animationManagerGreen.playAnim(0);
        animationManagerGreen.update();
        animationManagerOrange.update();
    }
}
