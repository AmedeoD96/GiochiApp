package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class ObstacleManager {
    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;
    private int color;

    private long startTime;
    private long initTime;

    private int score = 0;

    private Animation greenObstacle;
    private Animation orangeObstacle;
    private AnimationManager animManager;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight, int color) {
        this.playerGap = playerGap;
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        this.color = color;

        startTime = initTime = System.currentTimeMillis();

        obstacles = new ArrayList<>();


        animManager = new AnimationManager(new Animation[]{ greenObstacle, orangeObstacle });

        populateObstacles();
    }

    public boolean playerCollide(RectPlayer player) {
        for(Obstacle ob: obstacles) {
            if(ob.playerCollide(player)) {
                return true;
            }
        }
        return false;
    }

    private void populateObstacles() {
        int currY = -5*Constants.SCREEN_HEIGHT/4;
        while (currY < 0) {
            int xstart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, color, xstart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;

        }
    }

/*
    Context context = GlobalApplicationContext.getAppContext();
    SharedPreferences pref = context.getSharedPreferences("info", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = pref.edit();

 */


    public void update() {
        if (startTime < Constants.INIT_TIME) {
            startTime = Constants.INIT_TIME;
        }
        int elapsedTime = (int)(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        float speed =(float)(Math.sqrt(1 + (startTime - initTime)/(2000.0)))*Constants.SCREEN_HEIGHT/(10000.0f);
        for(Obstacle ob : obstacles) {
            ob.incrementY((speed * elapsedTime)/2);
            ob.update();
        }
        if(obstacles.get(obstacles.size()-1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
            int xstart = (int)(Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(0, new Obstacle(obstacleHeight, color, xstart, obstacles.get(0).getRectangle().top-obstacleHeight-obstacleGap,playerGap));
            obstacles.remove(obstacles.size()-1);
            score++;

            //TODO verificare il funzionamento chiudendo in vari modi il gioco
            //Salvataggio dati endlss
            User user = UsersManager.getInstance().getCurrentUser();
            if(user.scoreAlienrun < score){
                user.setScoreAlienrun(score);
            }

            //int highScore = pref.getInt("TopScore", 0);

            /*if(highScore<score) {
                editor.putInt("TopScoreEndless", score);
                editor.apply();
            }
             */
        }

    }

    public void draw(Canvas canvas) {
        for(Obstacle ob : obstacles) {
            ob.draw(canvas);
        }
        Paint paint =  new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.GREEN);
        canvas.drawText("" + score, 50, 50 + paint.descent() - paint.ascent(), paint );
    }
}
