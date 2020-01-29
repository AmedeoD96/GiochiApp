package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Border extends GameObject {
    private Bitmap image;

    //classe usata per la creazione dei bordi superiore e inferiore
    Border(Bitmap res, int x, int y, int h){
        height = h;
        //la width è settata di default a 20 per motivi grafici
        width = 20;
        this.x = x;
        this.y = y;

        //si ottiene la velocità di scorrimento dell'immagine
        dx = GamePanel.MOVESPEED;
        //creazione effettiva della bitmap dei bordi
        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    //si aggiorna la distanza percorsa
    public void update(){
        x += dx;
    }

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(image, x, y, null);
        }catch (Exception ignored){

        }
    }
}
