package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    void update();
    void draw(Canvas canvas);
    void terminate();
    void receiveTouch(MotionEvent event);
}
