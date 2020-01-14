package it.uniba.di.sms1920.giochiapp.Frogger;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Heart {

    private int lives;
    private Bitmap bitmap;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private final float X;
    private final float Y;

    //metodo che ritorna il numero di vite
    public int getLives() {
        return lives;
    }

    //metodo che imposta il numero di vite
    public void setLives(int lives) {
        this.lives = lives;
    }

    //metodo che imposta ,tramite il parametro booleano, lo stato del cuore
    // se la vita esiste oppure è stata eliminata
    public void setDead(boolean dead) {

        this.dead = dead;
    }

    private boolean dead = false;

    /*Costruttore della classe Heart
    parameters: riceve in input un float che indica l'altezza dell'oggetti Heart e le immagini che rappresentano gli oggetti Heart
     */
    public Heart(float logHeight, Bitmap bitmap3, Bitmap bitmap2, Bitmap bitmap1){
        X = 15;
        lives = 3;
        this.bitmap = bitmap3;
        this.bitmap3 = bitmap3;
        this.bitmap2 = bitmap2;
        this.bitmap1 = bitmap1;
        Y = logHeight - bitmap.getHeight()+50;
    }

    /*Metodo che cambia il valore delle vite totali.
    parameters: riceve in input un intero che va ad aggiungere quel preciso numero di vite
    a quelle già esistenti
     */
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

    /*Metodo che disegna gli oggetti di tipo Heart
    parameters: riceve in input un Canvas
     */
    public void draw(Canvas canvas){
        if(lives != 0){
            canvas.drawBitmap(bitmap,X, Y, null);
        }
    }

    //metodo che ritorna lo stato del booleano dead
    public boolean getdead() {
        return dead;
    }


}
