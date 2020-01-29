package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    private final long SECONDS_IN_MILLISECONDS = 1000;
    private final long MILLISECONDS_POW_2 = 1000000;

    private final SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder=surfaceHolder;
        this.gamePanel=gamePanel;
    }
    @Override
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime=0;
        int frameCount=0;
        int FPS = 30;
        long targetTime = SECONDS_IN_MILLISECONDS / FPS;
        while (running){
            startTime= System.nanoTime();
            canvas=null;

            try {
                //Inizia a modificare i pixels presenti sulla superficie
                canvas=this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }catch (Exception ignored){
            }
            finally {
                if(canvas!=null){
                    try {
                        //smette di modificare i pixels presenti sulla superficie
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception ignored){

                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime) / MILLISECONDS_POW_2;
            waitTime = targetTime - timeMillis;
            try {
                //viene fermato il tempo di attesa nel thread
                sleep(waitTime);
            }catch (Exception ignored){

            }
            //vengono aggiornati il counter dei frame e il tempo trascorso
            totalTime+= System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == FPS){
                double averageFPS = SECONDS_IN_MILLISECONDS / ((totalTime / frameCount) / MILLISECONDS_POW_2);
                frameCount=0;
                totalTime=0;
                System.out.println(averageFPS);
            }
        }
    }
    void setRunning(boolean b){
        running=b;
    }
}
