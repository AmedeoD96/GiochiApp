package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import it.uniba.di.sms1920.giochiapp.R;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;

    private SceneManager manager;

    private Background bg;


    //Costruttore della clsse
    //Riceve il contesto corrente e lo setta come costante
    //aggiunge una Callback, crea il MainThread e il SceneManager
    public GamePanel(Context context) {
        super(context);

        getHolder().addCallback(this);

        Constants.CURRENT_CONTEXT = context;

        thread = new MainThread(getHolder(), this);

        manager = new SceneManager();

        setFocusable(true);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //riceve in input un SurfaceHolder. Crea il background e da il comando start al thread
    //inizializza la costante relativa al tempo di avvio dell'applicazione
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.sfondoendless));
        thread.setRunning(true);
        thread.start();
    }

    //riceve in input il SurfaceHolder. Viene posto a null il thread
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000){
            try {
                thread.setRunning(false);
                counter++;
                thread.join();
                retry = false;
                thread = null;
            }catch (Exception e){e.printStackTrace();}

        }
    }

    //alla ricezione di un evento attua il receiveTouch dello SceneManager
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        manager.receiveTouch(event);

        return true;
        //return super.onTouchEvent(event);
    }

    //update del Background e dello SceneManager
    public void update() {
        bg.update();
        manager.update();
    }

    //disegna il Background e lo SceneManager
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        bg.draw(canvas);
        manager.draw(canvas);
    }

    //termina lo SceneManager
    public void close() {
        manager.terminate();
    }

}
