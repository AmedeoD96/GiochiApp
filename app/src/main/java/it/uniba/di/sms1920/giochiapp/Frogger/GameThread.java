package it.uniba.di.sms1920.giochiapp.Frogger;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;

    /*Questo setter, chiamato in GameView e nella MainActivity, riceve in input una variabile booleana
    che funge da flag e comunica al thread di gioco lo stato corrente, settando lo stato del thread
    in base al parametro booleano
    */
    void setRunning(boolean running) {
        this.running = running;
    }


    /*Costruttore della classe GameThread
    parameters: SurfaceHolder e GameView
    Viene usato per creare un thread di gioco.
     */
    GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();

        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }


    @Override
    @SuppressLint("WrongCall")

    /*Metodo che tramite lockCanvas crea una superficie scrivibile, che non potrà essere modificata
      a meno che non venga invocato unlockCanvasAndPost */
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
