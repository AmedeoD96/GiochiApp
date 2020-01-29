package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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

    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;

    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;

    //costruttore
    //crea il giocatore con una sua grandezza e con un suo colore di base
    //Genera dinamicamente il punto di partenza del giocatore in base alla grandezza dello schermo
    GameplayScene() {
        player = new RectPlayer(new Rect(100, 100, 200, 200), Color.rgb(255, 0, 0));
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2,3 * Constants.SCREEN_HEIGHT / 4);
        //il giocatore viene posto nel punto di partenza
        player.update(playerPoint);

        //crea l'èobstacleManager dando come parametri le misure degli ostacoli da generare il colore di base
        obstacleManager = new ObstacleManager(300, 550 , 150, Color.BLACK);

        //viene istanziato e registrato l'OrientationData.
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();

    }

    //genera il punto di inizio e un nuovo Obstacle Manager in caso di reset della partita dopo il primo utilizzo
    private void reset() {
        playerPoint = new Point(Constants.SCREEN_WIDTH / 2,3 * Constants.SCREEN_HEIGHT / 4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(300, 550, 150, Color.BLACK);
        movingPlayer = false;
    }

    //Usato per chiudere la sessione di gioco
    //una volta ottenuto l'utente utilizzatore, confronta lo score e l'highscore per un eventuale set
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
                //se la partita non fosse finita e se il giocatore fosse inserito correttamente sulla mappa, viene posto a true
                if(!gameOver && player.getRectangle().contains((int)event.getX(),(int)event.getY())) {
                    movingPlayer = true;
                }
                //se la partita fosse finita ma l'utente non ha interagito con lo schermo, la partita viene resettata automaticamente
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 2000) {
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //se la partita non fosse conclusa e l'utente fosse in movimento si settano i punti del piano in cui si trova il giocatore
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
        player.draw(canvas);
        obstacleManager.draw(canvas);

        //in caso di game over viene ottenuto l'utente corrente, settato l'highscore se effettivamente modificato
        if(gameOver) {
            User user = UsersManager.getInstance().getCurrentUser();
            if(user.scoreAlienrun < obstacleManager.getScore()) {
                user.setScoreAlienrun(obstacleManager.getScore());
            }

            //si ottiene il contesto
            Context context = GlobalApplicationContext.getAppContext();
            //viene importato il font
            Typeface customTypeface = ResourcesCompat.getFont(context, R.font.mariokartds);
            Paint paint = new Paint();
            paint.setTextSize(200);
            paint.setARGB(255,255, 143, 10);
            paint.setTypeface(customTypeface);
            paint.setTextAlign(Paint.Align.CENTER);
            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;
            canvas.drawText(context.getString(R.string.game_over), xPos, yPos, paint);
            canvas.drawText(context.getString(R.string.high_score_game), xPos, yPos + 200, paint);
            canvas.drawText(String.valueOf(user.scoreAlienrun), xPos, 400 + yPos, paint);

        }
    }

    @Override
    public void update() {
        if(!gameOver) {
            if(frameTime < Constants.INIT_TIME) {
                frameTime = Constants.INIT_TIME;
            }
            //viene settato il tempo trascorso dall'inizio della partita
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            //si aumenta la velocità del giocatore in base all'orientamento attuale e a quello iniziale
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1]- orientationData.getStartOrientation()[1];
                float roll =  orientationData.getOrientation()[2]- orientationData.getStartOrientation()[2];

                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / 1000f;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT / 1000f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y += Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }

            //evita che il punto in cui si trova l'utente non sia "sotto" lo schermo
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

            //se l'utente impatta un ostacolo allora viene settato lo stato di game over e ottenuto il tempo attuale
            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

}
