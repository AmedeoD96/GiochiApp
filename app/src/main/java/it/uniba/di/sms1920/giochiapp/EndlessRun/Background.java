package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import it.uniba.di.sms1920.giochiapp.EndlessRun.GamePanel;

public class Background {

    private Bitmap image;
    private int x,y,dx;

    public Background(Bitmap res){
        image=res;
    }

    public void update() {

    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image,x,y,null);
    }
}
