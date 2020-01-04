package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import it.uniba.di.sms1920.giochiapp.EndlessRun.GamePanel;

public class MainThread extends Thread {
    private static final int MAX_FPS = 30;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public void setRunning(boolean running) {
        this.running = running;
    }

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        long frameCount = 0;
        long totalTIme = 0;
        long targetTime = 1000/MAX_FPS;

        while(running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
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
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            totalTIme += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == MAX_FPS) {
                averageFPS = 1000/((totalTIme/frameCount)/1000000);
                frameCount = 0;
                totalTIme = 0;
                System.out.println(averageFPS);
            }
        }
    }
}
