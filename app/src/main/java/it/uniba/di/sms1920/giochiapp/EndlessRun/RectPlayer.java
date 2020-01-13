package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

import it.uniba.di.sms1920.giochiapp.R;

public class RectPlayer implements GameObject {

    private Rect rectangle;

    private AnimationManager animManager;

    public Rect getRectangle() {
        return rectangle;
    }

    RectPlayer(Rect rectangle, int color) {
        this.rectangle = rectangle;

        //vengono create le bitmap del giocatore
        Bitmap idleImg = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue);
        Bitmap walk1 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk1);
        Bitmap walk2 = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.alienblue_walk2);

        //vengono create le animazioni per la stasi e per il movimento del giocatore
        Animation idle = new Animation(new Bitmap[]{idleImg}, 2, true);
        //vengono create le animazioni per il movimento verso destra e verso sinistra dell'utente
        Animation walkRight = new Animation(new Bitmap[]{walk1, walk2}, 0.5f, true);

        Matrix m = new Matrix();
        m.preScale(-1, 1);
        walk1 = Bitmap.createBitmap(walk1, 0, 0, walk1.getWidth(), walk1.getHeight(), m, false);
        walk2 = Bitmap.createBitmap(walk2, 0, 0, walk2.getWidth(), walk2.getHeight(), m, false);

        Animation walkLeft = new Animation(new Bitmap[]{walk1, walk2}, 0.5f, true);

        animManager = new AnimationManager(new Animation[]{idle, walkRight, walkLeft});

    }

    @Override
    public void draw(Canvas canvas) {
        animManager.draw(canvas, rectangle);
    }


    @Override
    public void update() {
        animManager.update();
    }

    public void update(Point point) {
        //metodo update che riceve il punto in cui si trova il giocatore
        float oldLeft = rectangle.left;

        //si ottiene la nuova posizione dell'utente
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2, point.x + rectangle.width()/2, point.y + rectangle.height()/2);

        int state = 0;
        if (rectangle.left - oldLeft > 5) {
            //l'utente è andato a destra
            state = 1;
        } else if (rectangle.left - oldLeft < -5) {
            //l'utente è andato a sinistra
            state = 2;
        }

        animManager.playAnim(state);
        animManager.update();
    }
}
