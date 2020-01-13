package it.uniba.di.sms1920.giochiapp.Frogger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Frog {

    private Bitmap bitmap;
    private float x;
    private float y;
    private float yVel;
    private float xStart;
    private float yStart;
    public float jumpH;
    Rect box;
    
    float widthPercentage = 0.3f;


    public Rect getBox() {
        return box;
    }

    public void setBox(Rect box) {
        this.box = box;
    }
    public float getxStart() {
        return xStart;
    }

    public float getyStart() {
        return yStart;
    }


    public void setStart(float x, float y){
        this.xStart = x;
        this.yStart = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getyVel() {
        return yVel;
    }

    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    public float getxVel() {
        return xVel;
    }

    public void setxVel(float xVel) {
        this.xVel = xVel;
    }

    private float xVel;

    public Frog(Bitmap bitmap, float jumpH){
        this.jumpH = jumpH;
        this.bitmap = bitmap;
        x = 50;
    }

    public void frogJump(float x, float y){
        this.x += jumpH * x;
        this.y += jumpH * y;
    }



    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
        Rect startFrogRect = new Rect((int)getX(), (int)getY(),(int)(getX()+getBitmap().getWidth()), (int)(getY()+getBitmap().getHeight()));
        int boxWidth = Math.round(startFrogRect.width() * widthPercentage);

        box = new Rect(startFrogRect.left + boxWidth, startFrogRect.top, startFrogRect.right - boxWidth, startFrogRect.bottom);
        x += xVel;
    }
}
