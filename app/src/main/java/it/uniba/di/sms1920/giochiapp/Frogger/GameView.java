package it.uniba.di.sms1920.giochiapp.Frogger;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
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
    final int ROWS_WITHOUT_LOGS=4;
    final int BOTTOM_BOUNDS=1900;
    final int LEFT_BOUNDS=55;
    final int RIGHT_BOUNDS=900;
    final int SPEED_LEVELS=7;
    final int STARTING_SPEED_LEVEL=2;

    int points;
    long lastMils[];
    Rect waterBox, upperGrass, lowerGrass;
    Timer t;
    boolean haveMoved = false;
    GameThread thread;
    Bitmap logBitmap;
    ArrayList<LogObj> logRows[];
    ArrayList<Integer> remove[];
    Heart heart;
    boolean started;
    Animation water_anim, grass_anim;
    AnimationManager animationManagerWater, animationManagerGrass;
    SoundPool soundPool_hit, soundPool_walking;
    int hitId,walkId;



    public void setWasRunning(boolean wasRunning) {
        this.wasRunning = wasRunning;
    }

    boolean wasRunning;

/*Costruttore della classe GameView, accetta come parametro il contesto dell'activity in cui viene chiamato.
  Vengono inizializzate le immagini di sfondo, le animazioni e gli audio di gioco
*/
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        logBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.log);

        waterAnim();
        grassAnim();

        frog = new Frog(BitmapFactory.decodeResource(getResources(), R.drawable.frog),logBitmap.getHeight() );
        thread = new GameThread(getHolder(), this);
        heart = new Heart(logBitmap.getHeight(),
                BitmapFactory.decodeResource(getResources(), R.drawable.heart3),
                BitmapFactory.decodeResource(getResources(), R.drawable.heart2),
                BitmapFactory.decodeResource(getResources(), R.drawable.heart));


        //audio hit
        AudioAttributes attributes_hit= new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
        soundPool_hit = new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(attributes_hit).build();
        hitId=soundPool_hit.load(getContext(), R.raw.flooding, 1);

        //audio walk
        AudioAttributes attributes_walk= new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();
        soundPool_walking = new SoundPool.Builder().setMaxStreams(10).setAudioAttributes(attributes_walk).build();
        walkId=soundPool_walking.load(getContext(), R.raw.walking, 1);

    }


    /*Override del metodo surfaceCreated, imposta lo scenario di gioco: i tronchi, le velocità, il numero di tentativi.
    parameters: SurfaceHolder

     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);

        //LOGSTRIP indica il numero di array di tronchi che andranno generati, uno per ogni "fila", sono le caselle da sove si trova la rana fino alla fine dello scenario
        LOGSTRIP = (int)Math.ceil(getHeight()/logBitmap.getHeight());
        logRows = new ArrayList[LOGSTRIP-ROWS_WITHOUT_LOGS];
        remove = new ArrayList[LOGSTRIP-ROWS_WITHOUT_LOGS];
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

        //Generazione randomica delle velocità per ogni singola fila di tronchi. Infatti può anche capitare che la prima fila sia la più veloce.
        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            speeds[i]=ran.nextInt(SPEED_LEVELS)+STARTING_SPEED_LEVEL;
        }
        for (int i = 0; i < speeds.length; i+=2){ //le velocità pari vanno in un senso (moltiplicate per 1)
            speeds[i] *= 1;
        }
        for (int i = 1; i < speeds.length; i+=2){ //le velocità dispari vanno nell'altro senso (moltiplicate per -1)
            speeds[i] *= -1;
        }


        for (int i = 0; i < speeds.length; i++){
            Random ran = new Random();
            times[i]=ran.nextInt(2000)+1000;//2000+1000
        }

        if(!wasRunning)
            thread.start();
        started = true;
    }

    /*Questo metodo disegna i tronchi sulla superficie di gioco, creando degli oggetti di tipo LogObj in base alle necessità.
      Times indica un'array di tempi generati randomicamente.
      parameters: un long e un Canvas.
     */
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

                //se il tempo attuale meno l'ultimo tempo registrato è maggiore di un tempo in millisecondi generato randomicamente (nell'array di tempi generati randomicamente), allora...

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

    /*E' un metodo che analizza cosa succede quando la scatola della rana interseca una certa posizione.
     */
    public void collision(){

        //se la rana non interseca l'erba, viene colpita
        if(!frog.getBox().intersect(0,0,getWidth(),getHeight())){
            hit();
        }

        //se la rana raggiunge l'estremo superiore dello scenario, vengono aggiunti i punti
        if(frog.getBox().intersect(0,0,getWidth(),logBitmap.getHeight()*2)){
            score();
        }

        //se la rana interseca il box dell'acqua...
        if(frog.getBox().intersect(waterBox)){
            inWater = true;

            for (int i = 0; i < logRows.length; i++) {
                for (int j = 0; j < logRows[i].size(); j++) {
                    if(getFrogIntersect(frog.getBox(), logRows[i].get(j).getBox())){
                        frog.setxVel(speeds[i]);
                        inWater = false;
                    }
                }
            }
            if(inWater){
                hit();
            }
        }

    }

    boolean getFrogIntersect(Rect frog, Rect log) {
        return frog.intersect(log);
    }


    /*E' il metodo che descrive cosa succede quando la rana viene colpita.
    */
    public  void hit(){

        heart.lifeChange(-1);
        frog.setxVel(0);
        frog.setyVel(0);
        frog.setY(frog.getyStart());
        frog.setX(frog.getxStart());
        inWater = false;

        soundPool_hit.play(hitId,1,1,1,0,1);
    }

    /*
     E' il metodo che determina l'aumento del punteggio quando si completa un livello. Viene chiamato quando la rana arriva alla superficie superiore di erba
     */
    public void score(){
        points++;
        if(points<0)points=0;

        User user = UsersManager.getInstance().getCurrentUser();
        if(points>=user.scoreFrogger){
            user.setScoreFrogger(points);
        }
        reset();

    }

    /*
    Questo metodo reimposta il gioco dopo il Game Over, reimpostando tutti i valori al valore iniziale
     */

    public void reset(){
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
            speeds[i]=ran.nextInt(SPEED_LEVELS)+STARTING_SPEED_LEVEL;
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
            times[i]=ran.nextInt(2000)+1000;//2000+1000
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }


    //calcola la distanza tra due punti
    public float distance(float[] s, float[] e){
        return (float)Math.sqrt(Math.pow(e[0]-s[0],2)+Math.pow(e[1]-s[1],2));
    }

    /*Questo metodo descrive cosa succede quando si clicca sulla rana per farle compiere dei movimenti.
      Sono descritti i vari movimenti possibili che essa può compiere e cosa succede lungo gli assi, come conseguenza del movimento.
      parameters: Riceve in input un evento di tipo MotionEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent event){

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if(heart.getdead()){
                points=0;
                reset();
            }
            start[0] = event.getX();
            start[1] = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            end[0] = event.getX();
            end[1] = event.getY();

            //se la rana non si è ancora mossa e la distanza tra i due punti è maggiore di un certo parametro, il movimento avviene
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

                    soundPool_walking.play(walkId,1,1,1,0,1);

                    Log.d("Direction", "Up");
                }
                else if ((angle > 135 && angle < 225)&& frog.getX()>LEFT_BOUNDS){
                    frog.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frogsx));
                    frog.frogJump(-1,0);
                    frog.setxVel(0);

                    soundPool_walking.play(walkId,1,1,1,0,1);
                    Log.d("Direction", "Left");
                }
                else if ((angle > 45 && angle < 135)&& frog.getY()<BOTTOM_BOUNDS){
                    frog.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frogdown));
                    frog.frogJump(0,1);
                    frog.setxVel(0);

                    soundPool_walking.play(walkId,1,1,1,0,1);
                    Log.d("Direction", "Down");
                }
                else if(frog.getX()<RIGHT_BOUNDS){
                    frog.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.frogright));
                    frog.frogJump(1,0);
                    frog.setxVel(0);

                    soundPool_walking.play(walkId,1,1,1,0,1);
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

    /*Questo metodo è chiamato immediatamente prima che una superficie venga distrutta.
    parameters: prende in input una variabile di tipo SurfaceHolder.
    */
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


    /*Questo metodo disegna e rimuove i logs dallo schermo, in base a determinate condizioni.
    Aggiunge i log da togliere alla lista all'array remove[]
    parameters: riceve in input un Canvas
     */
    public void drawLogs(Canvas canvas){
        for (int i = 0; i < logRows.length; i++){
            for (int ii = 0; ii < logRows[i].size(); ii++){
                logRows[i].get(ii).draw(canvas);

                //se un log va a velocità -1 ed è fuori dallo schermo(verso un estremo), rimuovi il log dalla lista
                if ((Math.signum(logRows[i].get(ii).getxVel())==-1)&&((logRows[i].get(ii).getX()<0-logBitmap.getWidth()))){
                    remove[i].add(ii);
                }

                //se un log va a velocità 1 ed è fuori dallo schermo (dall'altro estremo), rimuovi il log dalla lista
                else if ((Math.signum(logRows[i].get(ii).getxVel())==1)&&((logRows[i].get(ii).getX()>canvas.getWidth()))){
                    remove[i].add(ii);
                }
            }

            //pulisce l'array remove
            for(int ii = 0; ii < remove[i].size(); ii++){
                try {
                logRows[i].remove((int)remove[i].get(ii));
                }
                finally {}
            }
            remove[i].clear();

        }
    }


    /*Questo metodo imposta le animazioni, gli sfondi, gli scenari, gli elementi, i colori e dimensioni dei testi.
    parameters: prende in input un Canvas
    */
    @Override
    public void onDraw(Canvas canvas){

        if (!started) {
            return;
        }
        animationManagerWater.playAnim(0);
        animationManagerGrass.playAnim(0);
        animationManagerWater.update();
        animationManagerGrass.update();
        animationManagerWater.draw(canvas,waterBox);


        spawnLogs(System.currentTimeMillis(), canvas);
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        upperGrass = new Rect(0,(LOGSTRIP-2)*logBitmap.getHeight(),canvas.getWidth(),canvas.getHeight());
        lowerGrass = new Rect(0,0,canvas.getWidth(),logBitmap.getHeight()*2);
        animationManagerGrass.draw(canvas,upperGrass);
        animationManagerGrass.draw(canvas,lowerGrass);
        drawLogs(canvas);
        frog.draw(canvas);
        heart.draw(canvas);

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(150);

        canvas.drawText(String.valueOf(points), canvas.getWidth()-logBitmap.getWidth(), logBitmap.getHeight()+50, textPaint);


        //qui cambia il colore nel caso di morte e imposta il game over
        if(heart.getdead()==true){

            Context context = GlobalApplicationContext.getAppContext();
            Typeface customTypeface = ResourcesCompat.getFont(context, R.font.mariokartds);

            //imposta i parametri per il posizionamento della scritta Game Over

            Paint paintDeath = new Paint();
            paintDeath.setTextSize(200);
            paintDeath.setARGB(255,0, 1, 80);
            paintDeath.setTextAlign(Paint.Align.CENTER);
            paintDeath.setTypeface(customTypeface);
            User user = UsersManager.getInstance().getCurrentUser();
            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((paintDeath.descent() + paintDeath.ascent()) / 2)) ;

            canvas.drawText(getContext().getString(R.string.game_over), xPos, yPos, paintDeath);
            canvas.drawText("high score",xPos,yPos+200,paintDeath);
            canvas.drawText(String.valueOf(user.scoreFrogger), xPos, 400+ yPos, paintDeath);


        }

        collision();
    }

    //Questo metodo inizializza le Bitmap per le animazioni dell'acqua, l'animazione stessa e l'animation manager che la gestisce.

    public void waterAnim(){
        Bitmap water = BitmapFactory.decodeResource(getResources(), R.drawable.water);
        Bitmap waterflip = BitmapFactory.decodeResource(getResources(), R.drawable.waterflip);
        Bitmap waterflop = BitmapFactory.decodeResource(getResources(), R.drawable.waterflop);

        water_anim=new Animation(new Bitmap[]{water,waterflip, waterflop}, 2, false);

        animationManagerWater= new AnimationManager(new Animation[]{water_anim});
    }

    //Questo metodo inizializza le Bitmap per le animazioni dell'acqua, l'animazione stessa e l'animation manager che la gestisce.
    public void grassAnim(){
        Bitmap grass = BitmapFactory.decodeResource(getResources(), R.drawable.grass);
        Bitmap grassflip = BitmapFactory.decodeResource(getResources(), R.drawable.grassflip);
        Bitmap grassflop = BitmapFactory.decodeResource(getResources(), R.drawable.grassflop);

        grass_anim=new Animation(new Bitmap[]{grass, grassflip,grassflop}, 2,false);

        animationManagerGrass= new AnimationManager(new Animation[]{grass_anim});
    }
}