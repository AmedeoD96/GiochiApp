package it.uniba.di.sms1920.giochiapp.Frogger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

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
    //SharedPreferences mPref=null;
    Animation water_anim;
    AnimationManager animationManager;


    public void setWasRunning(boolean wasRunning) {
        this.wasRunning = wasRunning;
    }

    boolean wasRunning;


    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        logBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.log);

        //water=BitmapFactory.decodeResource(getResources(), R.drawable.water);
        waterAnim();

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
        //se cambio left da 0 a getWidth() la rana mi cammina sull'acqua
        //il tronco sostitutivo dovrà avere altezza 40 px, devi ridimensionare l'immagine con paint
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
        for (int i = 0; i < speeds.length; i+=2){ //le velocità pari vanno in un senso (moltiplicate per 1)
            speeds[i] *= 1;
        }
        for (int i = 1; i < speeds.length; i+=2){ //le velocità dispari vanno nell'altro senso (moltiplicate per -1)
            speeds[i] *= -1;
        }


        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            times[i]=ran.nextInt(2000)+1000;
        }
        t = new Timer(); //NON VIENE PIU' USATA


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

        if(frog.getBox().intersect(0,0,getWidth(),logBitmap.getHeight()*2))score(); //left top è il punto in alto a sinistra

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

        //todo fare in modo che Bulbasaur non cammini sull'acqua quando vado a dx o sx sul log
    }

    public  void hit(){
        heart.lifeChange(-1);
        frog.setxVel(0);
        frog.setyVel(0);
        frog.setY(frog.getyStart());
        frog.setX(frog.getxStart());
        inWater = false;
    }

    public void score(){
        points ++;
        if(points<0)points=0;

        //salva dati
       /* int highscore_stored_frogger;
        highscore_stored_frogger=loadScore(GlobalApplicationContext.getAppContext());
        */
        User user = UsersManager.getInstance().getCurrentUser();
        if(points>=user.scoreFrogger){
            user.setScoreFrogger(points);
        }

        //reset gioco, usare la funzione di reset
        speeds = new int[logRows.length];
        times = new long[speeds.length];
        lastMils = new long[times.length];
        frog.setY(frog.getyStart());
        frog.setX(frog.getxStart());
        frog.setxVel(0);
        for(long l : lastMils){ //inutile
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

    }//cancellare e usare metodo reset

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
                    frog.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frog));
                    frog.frogJump(0,-1);
                    frog.setxVel(0);
                    Log.d("Direction", "Up");
                }
                else if (angle > 135 && angle < 225){
                    frog.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frogsx));
                    frog.frogJump(-1,0);
                    frog.setxVel(0);
                    Log.d("Direction", "Left");
                }
                else if (angle > 45 && angle < 135){
                    frog.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frogdown));
                    frog.frogJump(0,1);
                    frog.setxVel(0);
                    Log.d("Direction", "Down");
                }
                else{
                    frog.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frogright));
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
        animationManager.playAnim(0);
        animationManager.update();
        animationManager.draw(canvas,waterBox);


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


        //qui cambia il colore nel caso di morte
        if(heart.getdead()==true){
            paint.setARGB(140, 70, 70, 70);
            canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
            paint.setARGB(225,99, 65, 65);
            canvas.drawText("score", canvas.getWidth()-logBitmap.getWidth(),0, paint);

        }

        collision();
    }

    public void waterAnim(){
        Bitmap water = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        Bitmap waterflip = BitmapFactory.decodeResource(getResources(), R.drawable.waterflip);
        Bitmap waterflop = BitmapFactory.decodeResource(getResources(), R.drawable.waterflop);

        water_anim=new Animation(new Bitmap[]{water,waterflip, waterflop}, 2, false);

        animationManager= new AnimationManager(new Animation[]{water_anim});
    }
}