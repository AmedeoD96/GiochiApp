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

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.sfondoendless));
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry=true;
        int counter=0;
        while (retry && counter<1000){
            try {
                thread.setRunning(false);
                counter++;
                thread.join();
                retry=false;
                thread=null;
            }catch (Exception e){e.printStackTrace();}

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        manager.receiveTouch(event);

        return true;
        //return super.onTouchEvent(event);
    }

    public void update() {
        bg.update();
        manager.update();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        bg.draw(canvas);
        manager.draw(canvas);
    }


    public void close() {
        manager.terminate();
    }

}
