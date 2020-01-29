package it.uniba.di.sms1920.giochiapp.Frogger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class LogObj {


     private Bitmap bitmap;
     private float x;
     private float y;
     private float xVel;


    Rect getBox() {
        return box;
    }

    private Rect box;

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

     float getxVel() {
         return xVel;
     }


     /*Costruttore della classe LogObj
     parameters: riceve in input la Bitmap del log, la velocità del log e la posizione rispetto agli assi
      */
      LogObj(Bitmap bitmap, int speed, float y, float x) {
         this.bitmap = bitmap;
         this.x = x;
         this.y = y;
         xVel = speed;
     }

    /*funzione che disegna il rettangolo in cui il log è contenuto, avendo in input un Canvas.
     Viene creato un rettangolo prendendo in input i parametri della Bitmap di riferimento e le posizioni del log rispetto ai vari assi.
     */
     public void draw(Canvas canvas) {
         box = new Rect((int)getX(), (int)getY(),(int)(getX()+getBitmap().getWidth()), (int)(getY()+getBitmap().getHeight()));
         canvas.drawBitmap(bitmap, x, y, null);
         x += xVel;



     }
 }
