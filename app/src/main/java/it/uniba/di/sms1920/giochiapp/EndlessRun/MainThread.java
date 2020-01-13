package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Canvas;
import android.view.SurfaceHolder;


public class MainThread extends Thread {
    private static final int MAX_FPS = 30;
    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    void setRunning(boolean running) {
        this.running = running;
    }

    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        long frameCount = 0;
        long totalTIme = 0;
        long targetTime = 1000/MAX_FPS;

        while(running) {
            //otteniamo il tempo di avvio del gioco
            startTime = System.nanoTime();
            canvas = null;

            try {
                //si ottiene il canvas dal surfaceHolder e vengono eseguiti l'aggiornamento e il disegno del gamePanel
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime )/1000000;
            //il tempo di attesa è ottenuto dalla differenza tra il tempo scelto di default come target e il tempo di gioco in millisecondi
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    //se il tempo di attesa fosse maggiore di 0 il thread verrebbe posto in stato di sleep
                    sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //viene calcolato il tempo totale
            totalTIme += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == MAX_FPS) {
                //viene calcolata la media degli FPS
                double averageFPS = 1000 / ((totalTIme / frameCount) / 1000000);
                frameCount = 0;
                totalTIme = 0;
            }
        }
    }
}
