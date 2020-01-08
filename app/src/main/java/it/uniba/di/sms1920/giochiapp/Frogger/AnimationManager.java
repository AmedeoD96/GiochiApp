package it.uniba.di.sms1920.giochiapp.Frogger;

import android.graphics.Canvas;
import android.graphics.Rect;

import it.uniba.di.sms1920.giochiapp.EndlessRun.Animation;

public class AnimationManager {
    private it.uniba.di.sms1920.giochiapp.EndlessRun.Animation[] animations;
    private int animationIndex = 0;

    public AnimationManager(Animation[] animations) {
        this.animations = animations;
    }

    public void playAnim(int index) {
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


    public void draw(Canvas canvas, Rect rect) {
        if (animations[animationIndex].isPlaying()) {
            animations[animationIndex].draw(canvas, rect);
        }
    }

    public void update() {

        animations[animationIndex].update();
    }





}
