package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Missile extends GameObject {
    private int speed;
    private Animation animation=new Animation();

    Missile(Bitmap res, int x, int y, int w, int h, int s, int numFrames){
        super.x=x;
        super.y=y;
        width=w;
        height=h;
        Random rand = new Random();
        speed=7+(int)(rand.nextDouble()* s /30);

        //si ottiene la  velocità dei missili
        if(speed>40)speed=40;
        Bitmap[] image=new Bitmap[numFrames];
        //si crea la bitmap per ogni frame
        for (int i=0;i<image.length;i++){
            image[i]= Bitmap.createBitmap(res,0,i*height,width,height);
        }
        animation.setFrames(image);
        animation.setDelay(100-speed);
    }
    public void update(){
        x-=speed;
        animation.update();
    }
    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(animation.getImage(),x,y,null);
        }catch (Exception ignored){}
    }
    @Override
    public int getWidth(){
        //il missile sbanda per una esperienza più realistica
        return width-10;
    }
}
