package it.uniba.di.sms1920.giochiapp.Frogger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class LogObj {


     private Bitmap bitmap;
     private float x;
     private float y;
     private float yVel;
     private float xVel;

    public Rect getBox() {
        return box;
    }

    public void setBox(Rect box) {
        this.box = box;
    }

    private Rect box;

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    int direction;

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


     public LogObj(Bitmap bitmap, int speed, float y, float x) {
         this.bitmap = bitmap;
         this.x = x;
         this.y = y;
         xVel = speed;
     }


     public void draw(Canvas canvas) {
         canvas.drawBitmap(bitmap, x, y, null);
         x += xVel;
         box = new Rect((int)getX(), (int)getY(),(int)(getX()+getBitmap().getWidth()), (int)(getY()+getBitmap().getHeight()));
     }
 }
