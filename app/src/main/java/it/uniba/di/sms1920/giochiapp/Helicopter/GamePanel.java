package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    public static final int WIDTH=856;
    public static final int HEIGHT=480;
    public static final int MOVESPEED= -5;
    public static final int NUMBEROFSURFACEDESTROYED = 1000;
    public static final int HEIGHTPLAYER = 25;
    public static final int WIDTHPLAYER = 65;
    public static final int NUMBERFRAMESPLAYER = 3;
    public static final int HEIGHTMISSILE = 15;
    public static final int WIDTHMISSILE = 45 ;
    public static final int FRAMESMISSILE = 13;
    public static final int HEIGHTEXPLOSION = 100;
    public static final int WIDTHEXPLOSION = 100;
    public static final int FRAMESEXPLOSION = 24;
    private long SmokeStartTime;
    private long missileStartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<Border> topBorders;
    private ArrayList<Border> bottomBorders;
    private Random rand=new Random();
    private int maxBorderHeight;
    private int minBorderHeight;
    private boolean newGamecreated;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;

    private int numberMissilesSurpassed = 0;
    private int lastNumberOfMissilesSurpassed = 0;

    public int getLastNumberOfMissilesSurpassed() {
        return lastNumberOfMissilesSurpassed;
    }

    public void setLastNumberOfMissilesSurpassed(int lastNumberOfMissilesSurpassed) {
        this.lastNumberOfMissilesSurpassed = lastNumberOfMissilesSurpassed;
    }



    BitmapFactory.Options o = new BitmapFactory.Options();

    public GamePanel(Context context){
        super(context);
        o.inScaled = false;

        //si aggiunge una callback al surfaceViewHolder per ottenere l'evento in atto
        getHolder().addCallback(this);
        setFocusable(true);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry=true;
        int counter=0;
        while (retry && counter<NUMBEROFSURFACEDESTROYED){
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
    public void surfaceCreated(SurfaceHolder holder){
        bg = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.grassbg1, o));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter, o),WIDTHPLAYER,HEIGHTPLAYER,NUMBERFRAMESPLAYER);
        smoke = new ArrayList<>();
        missiles = new ArrayList<>();
        topBorders = new ArrayList<>();
        bottomBorders = new ArrayList<>();
        missileStartTime = System.nanoTime();
        SmokeStartTime = System.nanoTime();
        thread = new MainThread(getHolder(),this);
        //così facendo si inizializza in modo sicuro il loop di gioco
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()== MotionEvent.ACTION_DOWN){
            if(!player.getPlaying() && newGamecreated && reset){
                //in caso di partita creata ma di non interazione dell'utente con lo schermo
                player.setPlaying(true);
                Animation animation = explosion.getAnimation();
                animation.deleteAnimation();
                player.setUp(true);
            }
            if(player.getPlaying()) {
                //se l'utente stesse interagendo
                if(!started)started=true;
                reset=false;
                player.setUp(true);
            }
            return true;
        }
        if(event.getAction()== MotionEvent.ACTION_UP){
            //si ottiene la nuova posizione nell'asse y dell'elicottero
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){
        if(player.getPlaying()) {
            //se non venissero creati i bordi la partita finirebbe
            if(bottomBorders.isEmpty()){
                player.setPlaying(false);
                return;
            }
            if(topBorders.isEmpty()){
                player.setPlaying(false);
                return;
            }

            bg.update();
            player.update();
            //calcola la soglia di altezza che il bordo può avere in base al punteggio
            // aumentare per rallentare l'aumentare della difficoltà, diminuire per rallentare l'aumentare della difficoltà
            //indica quanto debbano essere grandi i muri, al massimo metà dello schermo
            int progressDenom = 20;
            maxBorderHeight=30+ player.getScore()/ progressDenom;
            if(maxBorderHeight>HEIGHT/4)maxBorderHeight=HEIGHT/4;
            minBorderHeight=5+player.getScore()/ progressDenom;

            User user = UsersManager.getInstance().getCurrentUser();

            //controlla la collisione con il bordo inferiore
            for(int i=0;i<bottomBorders.size();i++){
                if(collision(bottomBorders.get(i),player)){
                    player.setPlaying(false);

                    int highScore = user.scoreHelicopter;

                    //si controlla l'highscore
                    if(highScore < player.getScore()) {
                        user.setScoreHelicopter(player.getScore());
                    }
                }
            }
            //controlla la collisione con il bordo superiore
            for(int i=0;i<topBorders.size();i++){
                if(collision(topBorders.get(i),player)){
                    player.setPlaying(false);

                    int highScore = user.scoreHelicopter;

                    //si controlla l'highscore
                    if(highScore < player.getScore()) {
                        user.setScoreHelicopter(player.getScore());
                    }
                }
            }

            //update del bordo superiore
            this.updateTopborder();
            //update del bordo inferiore
            this.updateBottomborder();
            //si aggiungono i missili in base al tempo
            long missilesElapsed= (System.nanoTime()-missileStartTime)/1000000;
            if(missilesElapsed>(2000-player.getScore()/4)){
                //il primo missile è sempre al centro
                if(missiles.size()==0){
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.
                            missile, o),WIDTH+10,HEIGHT/2,WIDTHMISSILE,HEIGHTMISSILE,player.getScore(), FRAMESMISSILE));
                }
                else {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile, o),
                            WIDTH+10,(int)(rand.nextDouble()*(HEIGHT-(maxBorderHeight*2)+maxBorderHeight)), WIDTHMISSILE, HEIGHTMISSILE,player.getScore(),FRAMESMISSILE));
                }
                //resetta il timer
                missileStartTime= System.nanoTime();
            }
            //scorre attraverso ogni missile e controlla che la collisione sia avvenuta
            for(int i=0;i<missiles.size();i++){
                //aggiorna i missili
                missiles.get(i).update();
                if(collision(missiles.get(i),player)){
                    missiles.remove(i);
                    player.setPlaying(false);

                    //si controlla l'highscore
                    int highScore = user.scoreHelicopter;

                    if(highScore<player.getScore()) {
                        user.setScoreHelicopter(player.getScore());
                    }
                    break;
                }
                //i missili fuori dallo schermo vengono rimossi
                if(missiles.get(i).getX()<-100){
                    missiles.remove(i);
                    numberMissilesSurpassed++;
                    break;
                }

            }

            //viene aggiunto il fumo e si crea una nuova immagine in base al tempo trascorso
            long elapsed=(System.nanoTime()-SmokeStartTime)/1000000;
            if(elapsed>120){
                smoke.add(new Smokepuff(player.getX(),player.getY()+10));
                SmokeStartTime= System.nanoTime();
            }
            //se l'immagine va fuori dallo schermo viene rimossa
            for (int i=0;i<smoke.size();i++){
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10){
                    smoke.remove(i);
                }
            }
        }
        //se l'utente non stesse giocando
        else {
            player.resetDY();
            //se non fosse ancora iniziata la fase di reset, viene inizializzata e viene mostrata l'esplosione dell'elicottero
            if(!reset){
                newGamecreated =false;
                startReset = System.nanoTime();
                reset = true;
                disappear = true;

                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion, o),player.getX(),player.getY()-30,WIDTHEXPLOSION,HEIGHTEXPLOSION,FRAMESEXPLOSION);
            }
            long resetElapsed=(System.nanoTime()-startReset)/1000000;
            explosion.update();

            if(resetElapsed<2500 && !newGamecreated){
                newGame();
            }
        }
    }

    public void updateBottomborder() {
        //update del bordo inferiore
        for (int i=0;i<bottomBorders.size();i++){
            bottomBorders.get(i).update();
            //se il bordo esce dallo schermo, viene rimosso
            if(bottomBorders.get(i).getX()<-20){
                bottomBorders.remove(i);
                bottomBorders.add(new Border(BitmapFactory.decodeResource(getResources(), R.drawable.brick, o),bottomBorders.get(bottomBorders.size()-1).getX()+20,HEIGHT-minBorderHeight,200));
            }
        }
    }

    public void updateTopborder() {
        for(int i=0;i<topBorders.size();i++){
            topBorders.get(i).update();
            if(topBorders.get(i).getX()<-20){
                topBorders.remove(i);
                //rimuove gli elementi di un arraylist, ne aggiunge di nuovi
                topBorders.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.brick, o),
                        topBorders.get(topBorders.size()-1).getX()+20,0,10));
            }
        }
    }

    public boolean collision(GameObject a, GameObject b){
        //collision detection
        if(Rect.intersects(a.getRectangle(),b.getRectangle())){
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //viene scalata la dimensione dell'immagine
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);
        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if (!disappear) {
                //viene disegnato il giocatore
                player.draw(canvas);
            }
            //viene disegnato il fumo
            for (Smokepuff sp : smoke) {
                sp.draw(canvas);
            }
            //vengono disegnati i missili
            for (Missile m : missiles) {
                m.draw(canvas);
            }

            //viene disegnato il bordo superiore
            for (Border tb : topBorders) {
                tb.draw(canvas);
            }
            //viene disegnato il bordo inferiore
            for (Border bb : bottomBorders) {
                bb.draw(canvas);
            }
            //viene disegnata l'esplosione
            if (started) {
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }

    }

    public void drawText(Canvas canvas) {
        User user = UsersManager.getInstance().getCurrentUser();
        int best = user.scoreHelicopter;

        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        //viene importato il font
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        //viene attuato un draw del testo
        canvas.drawText(getContext().getString(R.string.distance)+ player.getScore(),10,HEIGHT-10,paint);
        canvas.drawText(getContext().getString(R.string.best)+ best,10,HEIGHT-430,paint);

        if(!player.getPlaying()&&newGamecreated&&reset){
            //nella schermata di start
            Paint paint1=new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText(getContext().getString(R.string.pressToStart), WIDTH / 2 - 50, HEIGHT / 2, paint1);
            paint1.setTextSize(20);
            canvas.drawText(getContext().getString(R.string.goUp),WIDTH/2-50,HEIGHT/2+20,paint1);
            canvas.drawText(getContext().getString(R.string.goDown),WIDTH/2-50,HEIGHT/2+40,paint1);
            if(getLastNumberOfMissilesSurpassed() != 0) {
                Resources res = getResources();
                String numberMissiles = res.getQuantityString(R.plurals.numberOfMissilesSurpassed, getLastNumberOfMissilesSurpassed(), getLastNumberOfMissilesSurpassed());
                canvas.drawText(numberMissiles, WIDTH/2-50,HEIGHT/2+60,paint1);
            }
        }

    }

    public void newGame(){
        //vengono azzerati tutti i valori degli oggetti presenti sullo schermo
        disappear=false;
        bottomBorders.clear();
        topBorders.clear();
        missiles.clear();
        smoke.clear();

        //altezza minima e massima del bordo
        minBorderHeight=5;
        maxBorderHeight=30;

        //posizione di partenza del giocatore
        player.setY(HEIGHT / 2);
        player.resetDY();
        //viene salvato il punteggio massimo
        User user = UsersManager.getInstance().getCurrentUser();
        int highScore = user.scoreHelicopter;
        setLastNumberOfMissilesSurpassed(numberMissilesSurpassed);
        numberMissilesSurpassed = 0;

        if(highScore<player.getScore()) {
            user.setScoreHelicopter(player.getScore());
        }

        player.resetScore();

        //creazione dei bordi iniziali
        //bordo superiore
        for (int i=0;i*20<WIDTH+40;i++){
            //creato primo bordo superiore
            if(i==0){
                topBorders.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.brick, o),i*20,0,10));
            }
            //aggiunti bordi finchè lo schermo iniziale è pieno
            else {
                topBorders.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.brick, o),i*20,0,10));
            }
        }
        //bordo inferiore
        for (int i=0;i*20<WIDTH+40;i++){
            //primo bordo inferiore creato
            if(i==0){
                bottomBorders.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.brick, o),i*20,HEIGHT-minBorderHeight,200));
            }
            //aggiunti bordi finchè lo schermo iniziale è pieno
            else {
                bottomBorders.add(new Border(BitmapFactory.decodeResource(getResources(),R.drawable.brick, o),i*20,HEIGHT-minBorderHeight,200));
            }
        }
        newGamecreated=true;
    }
}
