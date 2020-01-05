package it.uniba.di.sms1920.giochiapp.Frogger;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Heart {
    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    private int lives;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private final float X;
    private final float Y;

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    private boolean dead = false;

    public Heart(float logHeight, Bitmap bitmap3, Bitmap bitmap2, Bitmap bitmap1){
        X = 0;
        lives = 3;
        this.bitmap = bitmap3;
        this.bitmap3 = bitmap3;
        this.bitmap2 = bitmap2;
        this.bitmap1 = bitmap1;
        Y = logHeight - bitmap.getHeight();
    }

    public void lifeChange(int i){
        lives += i;
        switch (lives){
            case 3:
                bitmap = bitmap3;
                break;
            case 2:
                bitmap = bitmap2;
                break;
            case 1:
                bitmap = bitmap1;
                break;
            case 0:
                dead = true;
            default:
                break;
        }
        Log.d("Lives: ", lives+"");
    }

    public void draw(Canvas canvas){
        if(lives != 0){
            canvas.drawBitmap(bitmap,X, Y, null);
        }
    }

    public boolean getdead() {
        return dead;
    }


}
