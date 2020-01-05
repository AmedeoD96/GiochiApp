package it.uniba.di.sms1920.giochiapp.Frogger;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private GameView gameView;

    private boolean running;
    public void setRunning(boolean running) {
        this.running = running;
    }

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }


    @Override
    @SuppressLint("WrongCall")
    public void run() {
        Canvas canvas;
        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gameView.onDraw(canvas);
                }
            }
            finally {
                if (canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }


}
