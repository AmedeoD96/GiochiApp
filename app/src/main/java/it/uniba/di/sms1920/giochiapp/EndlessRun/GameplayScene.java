package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import androidx.core.content.res.ResourcesCompat;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class GameplayScene implements Scene {

    private Rect r = new Rect();

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;
    //Context context = GlobalApplicationContext.getAppContext();

    public GameplayScene() {
        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,0,0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(300,550 ,150, Color.BLACK);


        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();

    }

    public void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(300,550,150, Color.BLACK);
        movingPlayer = false;
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

        if(obstacleManager != null) {
            User user = UsersManager.getInstance().getCurrentUser();
            if(user.scoreAlienrun < obstacleManager.getScore()) {
                user.setScoreAlienrun(obstacleManager.getScore());
            }
        }
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(),(int)event.getY())) {
                    movingPlayer = true;
                }
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer) {
                    playerPoint.set((int)event.getX(),(int)event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawColor(Color.WHITE);

        player.draw(canvas);
        obstacleManager.draw(canvas);

        if(gameOver) {
            User user = UsersManager.getInstance().getCurrentUser();
            if(user.scoreAlienrun < obstacleManager.getScore()) {
                user.setScoreAlienrun(obstacleManager.getScore());
            }

            //SharedPreferences endless = context.getSharedPreferences("info", MODE_PRIVATE);
            //int highScore = endless.getInt("TopScoreEndless", 0);
            Context context = GlobalApplicationContext.getAppContext();
            Typeface customTypeface = ResourcesCompat.getFont(context, R.font.fippsregular);
            Paint paint = new Paint();
            paint.setTextSize(100);
            paint.setColor(Color.rgb(255,143,10));
            paint.setTypeface(customTypeface);
            Bitmap gameOverImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.gameover);
            Bitmap highScoreImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.highscore);
            Matrix m = new Matrix();
            int centreX = (canvas.getWidth()  - gameOverImg.getWidth()) /2;
            int centreY = (canvas.getHeight() - gameOverImg.getHeight()) /2;
            gameOverImg = Bitmap.createBitmap(gameOverImg, 0, 0, gameOverImg.getWidth(), gameOverImg.getHeight(), m, false);
            highScoreImg = Bitmap.createBitmap(highScoreImg,0,0, highScoreImg.getWidth(),highScoreImg.getHeight(),m,false);
            //drawCenterText(canvas, paint, "Game Over\nHigh score: " + highScore); Il \n non funziona quando si disegna un canvas.
            paint.setTextAlign(Paint.Align.CENTER);
            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
            //canvas.drawText("Game Over", xPos, yPos, paint);
            canvas.drawBitmap(gameOverImg,centreX,centreY,paint);
            canvas.drawBitmap(highScoreImg,centreX+150,centreY+170,paint);
            canvas.drawText(String.valueOf(user.scoreAlienrun), xPos, 290+ yPos, paint);

        }
    }

    @Override
    public void update() {
        if(!gameOver) {
            if(frameTime < Constants.INIT_TIME) {
                frameTime = Constants.INIT_TIME;
            }
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1]- orientationData.getStartOrientation()[1];
                float roll =  orientationData.getOrientation()[2]- orientationData.getStartOrientation()[2];

                float xSpeed = 2*roll* Constants.SCREEN_WIDTH/1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT/1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y += Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }

            if(playerPoint.x < 0) {
                playerPoint.x = 0;
            } else if (playerPoint.x > Constants.SCREEN_WIDTH) {
                playerPoint.x = Constants.SCREEN_WIDTH;
            }
            if(playerPoint.y < 0) {
                playerPoint.y = 0;
            } else if (playerPoint.y > Constants.SCREEN_HEIGHT) {
                playerPoint.y = Constants.SCREEN_HEIGHT;
            }

            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    /*
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }

     */
}
