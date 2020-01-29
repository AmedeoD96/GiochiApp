package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    private Bitmap image;
    private int x;
    private int dx;

    Background(Bitmap res){
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    //se il background superasse la grandezza dell'immagine allora quest'ultima ripartirebbe
    public void update() {
        x+=dx;
        if(x < -GamePanel.WIDTH){
            x = 0;
        }
    }
    //funzione di creazione effettiva del background
    public void draw(Canvas canvas) {
        int y = 0;
        canvas.drawBitmap(image, x, y, null);
        if (x < 0){
            canvas.drawBitmap(image, x + GamePanel.WIDTH, y, null);
        }
    }
}
