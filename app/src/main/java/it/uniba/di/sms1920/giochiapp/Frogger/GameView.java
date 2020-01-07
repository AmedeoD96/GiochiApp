package it.uniba.di.sms1920.giochiapp.Frogger;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    Frog frog;
    int speeds[];
    float start[] = new float[2];
    float end[] = new float[2];
    final float dis = 200;
    long times[];
    boolean inWater = false;
    int LOGSTRIP;
    int points;
    long lastMils[];
    Rect waterBox;
    Timer t;
    boolean haveMoved = false;
    GameThread thread;
    Bitmap logBitmap;
    ArrayList<LogObj> logRows[];
    ArrayList<Integer> remove[];
    Heart heart;
    boolean started;

    public void setWasRunning(boolean wasRunning) {
        this.wasRunning = wasRunning;
    }

    boolean wasRunning;

    Context context = GlobalApplicationContext.getAppContext();
    SharedPreferences pref = context.getSharedPreferences("info", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        logBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.log);

        frog = new Frog(BitmapFactory.decodeResource(getResources(), R.drawable.frog), logBitmap.getHeight());
        thread = new GameThread(getHolder(), this);
        heart = new Heart(logBitmap.getHeight(),
                BitmapFactory.decodeResource(getResources(), R.drawable.heart3),
                BitmapFactory.decodeResource(getResources(), R.drawable.heart2),
                BitmapFactory.decodeResource(getResources(), R.drawable.heart));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);


        LOGSTRIP = (int)Math.ceil(getHeight()/logBitmap.getHeight()); //LOGSTRIP=15 perchè 15 sono le "caselle" da dove si trova la rana fino all fine dello scenario
        logRows = new ArrayList[LOGSTRIP-4]; //4 sono le caselle dove i log non devono essere generati
        remove = new ArrayList[LOGSTRIP-4];
        for (int i = 0; i < remove.length; i++){
            remove[i]=new ArrayList<Integer>();
        }
        for (int i = 0; i < logRows.length; i++){
            logRows[i]=new ArrayList<LogObj>();
        }
        waterBox = new Rect (0, logBitmap.getHeight()*2, getWidth(), ((LOGSTRIP-2)*logBitmap.getHeight())); //getWidth() dà 1080
     //   se cambio left da 0 a getWidth() la rana mi cammina sull'acqua
        frog.setY((LOGSTRIP-1)*logBitmap.getHeight());
        frog.setStart(frog.getX(), frog.getY());
        speeds = new int[logRows.length];
        times = new long[speeds.length];
        lastMils = new long[times.length];
        for(long l : lastMils){
            l = System.currentTimeMillis();
        }

        //Made by Lillo: la velocità dei pezzi di legno è generata randomicamente.
        //Quindi può capitare che il primo "rigo" sia quello più veloce (nello scorrimento)
        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            speeds[i]=ran.nextInt(10)+3;
            //speeds[i] *= Math.signum(ran.nextFloat()-0.5);
        }
        for (int i = 0; i < speeds.length; i+=2){
            speeds[i] *= 1;
        }
        for (int i = 1; i < speeds.length; i+=2){
            speeds[i] *= -1;
        }


        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            times[i]=ran.nextInt(2000)+1000;
        }
        t = new Timer();


        if(!wasRunning)
            thread.start();
        started = true;
    }

    public void spawnLogs(long now, Canvas canvas){
        if(times != null) {
            for (int i = 0; i < times.length; i++) {
                if(logRows[i].size() < 1){
                    if (speeds[i] < 0) {
                        logRows[i].add(new LogObj(logBitmap, speeds[i], (i + 2) * logBitmap.getHeight(), canvas.getWidth()));
                    } else {
                        logRows[i].add(new LogObj(logBitmap, speeds[i], (i + 2) * logBitmap.getHeight(), 0 - logBitmap.getWidth()));
                    }
                }
                else if (now - lastMils[i] > times[i]) {
                    Random ran = new Random();
                    if (speeds[i] < 0) {
                        logRows[i].add(new LogObj(logBitmap, speeds[i], (i + 2) * logBitmap.getHeight(), canvas.getWidth()));
                    } else {
                        logRows[i].add(new LogObj(logBitmap, speeds[i], (i + 2) * logBitmap.getHeight(), 0 - logBitmap.getWidth()));
                    }
                    times[i] = ran.nextInt(7000) + 3000;

                    lastMils[i] = now;
                }
            }
        }
    }

    public void collision(){
        if(!frog.getBox().intersect(0,0,getWidth(),getHeight()))hit();

        if(frog.getBox().intersect(0,0,getWidth(),logBitmap.getHeight()*2))score();

        if(frog.getBox().intersect(waterBox)){
            inWater = true;

            for (int i = 0; i < logRows.length; i++) {
                for (int ii = 0; ii < logRows[i].size(); ii++) {
                    if(frog.getBox().intersect(logRows[i].get(ii).getBox())){
                        frog.setxVel(speeds[i]);
                        inWater = false;
                    }
                }
            }
            if(inWater)hit();
        }
    }

    public  void hit(){
        heart.lifeChange(-1);
        frog.setxVel(0);
        frog.setyVel(0);
        frog.setY(frog.getyStart());
        frog.setX(frog.getxStart());
        inWater = false;

        int highScore = pref.getInt("TopScore", 0);

        if(highScore<points) {
            editor.putInt("TopScoreFrogger", points);
            editor.apply();
        }
    }

    public void score(){
        points ++;
        if(points<0)points=0;
        speeds = new int[logRows.length];
        times = new long[speeds.length];
        lastMils = new long[times.length];
        frog.setY(frog.getyStart());
        frog.setX(frog.getxStart());
        frog.setxVel(0);
        for(long l : lastMils){
            l = System.currentTimeMillis();
        }

        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            speeds[i]=ran.nextInt(10)+3;
            //speeds[i] *= Math.signum(ran.nextFloat()-0.5);
        }
        for (int i = 0; i < speeds.length; i+=2){
            speeds[i] *= 1;
        }
        for (int i = 1; i < speeds.length; i+=2){
            speeds[i] *= -1;
        }


        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            times[i]=ran.nextInt(2000)+1000;
        }

    }

    public void reset(){
        points=0;
        speeds = new int[logRows.length];
        times = new long[speeds.length];
        lastMils = new long[times.length];
        frog.setY(frog.getyStart());
        frog.setX(frog.getxStart());
        frog.setxVel(0);
        for(long l : lastMils){
            l = System.currentTimeMillis();
        }
        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            speeds[i]=ran.nextInt(10)+3;
        }
        for (int i = 0; i < speeds.length; i+=2){
            speeds[i] *= 1;
        }
        for (int i = 1; i < speeds.length; i+=2){
            speeds[i] *= -1;
        }
        heart.setDead(false);
        heart.lifeChange(3);


        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            times[i]=ran.nextInt(2000)+1000;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public float distance(float[] s, float[]e){
        return (float)Math.sqrt(Math.pow(e[0]-s[0],2)+Math.pow(e[1]-s[1],2));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if(heart.getdead())reset();
            start[0] = event.getX();
            start[1] = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            end[0] = event.getX();
            end[1] = event.getY();
            if(!haveMoved && distance(start, end) >= dis){
                // move
                double angle = Math.toDegrees(Math.atan2((double)(end[1]-start[1]),(double)(end[0]-start[0])));
                if (angle < 0) {
                    angle += 360;
                }
                if(angle > 225 && angle < 315){
                    frog.frogJump(0,-1);
                    frog.setxVel(0);
                    Log.d("Direction", "Up");
                }
                else if (angle > 135 && angle < 225){
                    frog.frogJump(-1,0);
                    frog.setxVel(0);
                    Log.d("Direction", "Left");
                }
                else if (angle > 45 && angle < 135){
                    frog.frogJump(0,1);
                    frog.setxVel(0);
                    Log.d("Direction", "Down");
                }
                else{
                    frog.frogJump(1,0);
                    frog.setxVel(0);
                    Log.d("Direction", "Right");
                }


                haveMoved = true;

            }
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            haveMoved = false;
            start = new float[2];
            end = new float[2];
        }
        return true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void drawLogs(Canvas canvas){
        for (int i = 0; i < logRows.length; i++){
            for (int ii = 0; ii < logRows[i].size(); ii++){
                logRows[i].get(ii).draw(canvas);
                if ((Math.signum(logRows[i].get(ii).getxVel())==-1)&&((logRows[i].get(ii).getX()<0-logBitmap.getWidth()))){
                    remove[i].add(ii);
                }
                else if ((Math.signum(logRows[i].get(ii).getxVel())==1)&&((logRows[i].get(ii).getX()>canvas.getWidth()))){
                    remove[i].add(ii);
                }
            }
            for(int ii = 0; ii < remove[i].size(); ii++){
                try {
                logRows[i].remove((int)remove[i].get(ii));
                }
                finally {}
            }
            remove[i].clear();

        }
    }

    @Override
    public void onDraw(Canvas canvas){
        if (!started) {
            return;
        }
        canvas.drawColor(Color.BLUE);
        spawnLogs(System.currentTimeMillis(), canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(0,(LOGSTRIP-2)*logBitmap.getHeight(),canvas.getWidth(),canvas.getHeight(),paint);
        canvas.drawRect(0,0,canvas.getWidth(),logBitmap.getHeight()*2,paint);
        drawLogs(canvas);
        frog.draw(canvas);
        heart.draw(canvas);

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(150);

        canvas.drawText(points + "", canvas.getWidth()-logBitmap.getWidth(), logBitmap.getHeight()+50, textPaint);


        if(heart.getdead()==true){
            paint.setARGB(140, 70, 70, 70);
            canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
            paint.setARGB(225,99, 65, 65);
            canvas.drawText("score", canvas.getWidth()-logBitmap.getWidth(),0, paint);

            int highScore = pref.getInt("TopScoreFrogger", 0);

            if(highScore<points) {
                editor.putInt("TopScoreFrogger", points);
                editor.apply();
            }
        }

        collision();
    }
}