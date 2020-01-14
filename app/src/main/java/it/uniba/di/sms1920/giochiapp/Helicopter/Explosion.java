package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Explosion {
    private int x;
    private int y;
    private int height;
    private int row;
    private Animation animation=new Animation();

    Explosion(Bitmap res, int x, int y, int w, int h, int numFrames){
        this.x=x;
        this.y=y;
        this.height=h;

        //si ottengono i frame che comporrano l'animazione
        Bitmap[] image= new Bitmap[numFrames];
        //viene sezionata l'immagine contenente i frame dell'esplosione per ottenenre il singolo frame
        for (int i=0;i<image.length;i++){
            if (i%5==0&&i>0)row++;
            //viene creata una bitmap per ogni frame
            image[i]= Bitmap.createBitmap(res,(i-(5*row))* w,row*height, w,height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
    }
    public void draw(Canvas canvas){
        if (!animation.playedOnce()){
            //si disegna la bitmap dell'esplosione
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }
    }
    public void update(){
        if(!animation.playedOnce()){
            //si passa al frame successivo
            animation.update();
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public int getHeight(){
        return height;
    }
}
