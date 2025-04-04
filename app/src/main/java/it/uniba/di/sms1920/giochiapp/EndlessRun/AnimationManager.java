package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Canvas;
import android.graphics.Rect;


public class AnimationManager {
    private Animation[] animations;
    private int animationIndex = 0;

    AnimationManager(Animation[] animations) {
        this.animations = animations;
    }

    //riceve in input un indice e, applicandosi ad un vettore di Animation avvia la funzione play per l'animazione posta
    //nella posizione specificatta dall'indice. Vengono poste in stato di stop tutti gli altri elementi del vettore
    void playAnim(int index) {
        for(int i = 0; i<animations.length; i++) {
            if (i == index) {
                if (!animations[index].isPlaying()) {
                    animations[i].play();
                }
            } else {
                animations[i].stop();
            }
        }
        animationIndex = index;
    }


    //la funzione disegna una Animation a partire da un Rect
    //viene disegnata solamente la animazione in esecuzione
    public void draw(Canvas canvas, Rect rect) {
        if (animations[animationIndex].isPlaying()) {
            animations[animationIndex].draw(canvas, rect);
        }
    }

    public void update() {
        animations[animationIndex].update();
    }
}
