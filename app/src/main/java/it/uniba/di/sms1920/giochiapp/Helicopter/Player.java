package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Player extends GameObject {
    private static int XINITIALPOSITION = 100;
    private int score;
    private boolean up;
    private boolean playing;
    private Animation animation=new Animation();
    private long startTime;


    Player(Bitmap res, int w, int h, int numFrames){
        x=XINITIALPOSITION;
        //il giocatore è creato a metà schermo
        y=GamePanel.HEIGHT/2;
        dy=0;
        score=0;
        height=h;
        width=w;

        //creazione dell'immagine e dell'animazione
        Bitmap[] image=new Bitmap[numFrames];

        for (int i=0;i<image.length;i++){
            image[i]= Bitmap.createBitmap(res,i*width,0,width,height);
        }
        animation.setFrames(image);
        animation.setDelay(10);
        startTime= System.nanoTime();
    }


    public void setUp(boolean b){
        up=b;
    }


    public void update(){
        long elapsed=(System.nanoTime()-startTime)/1000000;
        if (elapsed>100){
            //si aumenta lo score in base al tempo in cui si è in gioco
            score++;
            startTime= System.nanoTime();
        }
        animation.update();
        //si controlla che l'elicottero non vada oltre i bordi sia dall'alto che dal basso
        if (up){
            dy-=1;
        }
        else {
            dy+=1;
        }
        if (dy>14)dy=14;
        if (dy<-14)dy=-14;

        y+=dy*2;

    }


    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(),x,y,null);
    }


    public int getScore(){return score;}


    boolean getPlaying(){return playing;}


    void setPlaying(boolean b){playing=b;}


    void resetDY(){dy=0;}


    void resetScore(){score=0;}
}
