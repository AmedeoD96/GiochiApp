package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class BottomBorder extends GameObject {

    Bitmap image;
    public BottomBorder(Bitmap res, int x, int y){
        height=200;
        width=20;

        this.x=x;
        this.y=y;
        dx=GamePanel.MOVESPEED;
        image= Bitmap.createBitmap(res,0,0,width,height);
    }
    public void update(){
        x+=dx;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(image,x,y,null);
    }
}
