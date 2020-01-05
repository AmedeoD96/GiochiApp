package it.uniba.di.sms1920.giochiapp.Frogger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.util.Log;


public class Frog {

    private Bitmap bitmap;
    private float x;
    private float y;
    private float yVel;
    private float xStart;
    private float yStart;
    public float jumpH;
    Rect box;


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
        box = new Rect((int)getX(), (int)getY(),(int)(getX()+getBitmap().getWidth()), (int)(getY()+getBitmap().getHeight()));
        x += xVel;
    }
}
