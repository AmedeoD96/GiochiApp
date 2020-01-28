package it.uniba.di.sms1920.giochiapp.Frogger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Animation {

    private Bitmap[] frames;
    private int frameIndex;
    private boolean scale;

    private boolean isPlaying = false;

     boolean isPlaying() {

        return isPlaying;
    }

     void play() {
        isPlaying = true;
        frameIndex = 0;
        lastFrame = System.currentTimeMillis();
    }

     void stop() {

        isPlaying = false;
    }

    private float frameTime;

    private long lastFrame;


    //Costruttore della classe
    //Params
    //frames: vettore di Bitmap dal quale si ottengono le immagini delle animazioni
    //animTime: float indicante il tempo di durata prevista dell'animazione
    //scale: boolean indicante se l'animazione sia da scalare nelle proporzioni
    public Animation(Bitmap[] frames, float animTime, boolean scale) {
        this.frames = frames;
        frameIndex = 0;

        frameTime = animTime/frames.length;

        lastFrame = System.currentTimeMillis();

        this.scale = scale;
    }

    //la funzione disegna una Bitmap a partire da un Rect
    //se il boolean scale fosse attivo , l'immagine verrebbbe scalata dalla funzione scaleRect
    public void draw(Canvas canvas, Rect destination) {
        if(!isPlaying)
            return;

        if(scale == true) {
            scaleRect(destination);
        }

        canvas.drawBitmap(frames[frameIndex], null, destination, new Paint());
    }

    //Riceve in input un Rect e ne effettua uno scale
    //viene calcolato il risultato della divisione tra la larghezza dell'ultimo frame e l'altezza del primo
    //per poi usarlo allo scopo adattare la base o l'altezza del rettangolo in base alle misure già presenti
    private void scaleRect(Rect rect) {
        float whRatio = (float)(frames[frameIndex].getWidth())/frames[frameIndex].getHeight();
        if(rect.width() > rect.height())
            rect.left = rect.right - (int)(rect.height() * whRatio);
        else
            rect.top = rect.bottom - (int)(rect.width() * (1/whRatio));
    }

    //la funzione incrementa il frame index e aggiorna il lastFrane
    //ciò viene eseguito se il tempo attuale - l'ultimo frame sono maggiori del prodotto del frameTime*1000
    public void update() {
        if(!isPlaying)
            return;

        if(System.currentTimeMillis() - lastFrame > frameTime*1000) {
            frameIndex++;
            frameIndex = frameIndex >= frames.length ? 0 : frameIndex;
            lastFrame = System.currentTimeMillis();
        }
    }

}
