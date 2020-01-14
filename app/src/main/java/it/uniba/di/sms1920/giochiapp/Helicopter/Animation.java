package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Bitmap;

public class Animation {
    private Bitmap[] frames;
    private int currentFrames;
    private long startTimes;
    private long delay;
    private boolean playedOnce;

    //riceve in input un array di Bitmap
    //Predispone l'utilizzo dell'animation
    void setFrames(Bitmap[] frames){
        this.frames=frames;
        currentFrames=0;
        startTimes= System.nanoTime();
    }
    void setDelay(long d){delay=d;}

    public void update(){
        //aggiorna il tempo trascorso dall'inizio del gioco
        long elapsed=(System.nanoTime()-startTimes)/1000000;
        //si passa al frame succesivo quando il tempo trascorso supera il delay
        if(elapsed>delay){
            currentFrames++;
            startTimes= System.nanoTime();
        }
        //se ci trovassimo alla fine del vettore si ripartirebbe dal primo frame
        if (currentFrames==frames.length){
            currentFrames=0;
            playedOnce=true;
        }
    }
    //fa ripartire l'animazione
    void deleteAnimation(){
        currentFrames=0;
        playedOnce=true;
    }


    public Bitmap getImage(){
        return frames[currentFrames];
    }

    boolean playedOnce(){return playedOnce;}
}
