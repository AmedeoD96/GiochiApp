package it.uniba.di.sms1920.giochiapp.Frogger;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;


public class Frog {

    private Bitmap bitmap;
    private float x;
    private float y;
    private float xVel;
    private float yVel;
    private float xStart;
    private float yStart;
    public float step;
    Rect box;
    
    float widthPercentage = 0.3f;


    //ritorna il rettangolo in cui è contenuta la rana
    public Rect getBox() {
        return box;
    }

    //ritorna la posizione di start rispetto all'asse x
    public float getxStart() {
        return xStart;
    }

    //ritorna la posizione di start rispetto all'asse y
    public float getyStart() {
        return yStart;
    }


    //imposta la posizione di start
    public void setStart(float x, float y){
        this.xStart = x;
        this.yStart = y;
    }

    //ritorna l'immagine bitmap
    public Bitmap getBitmap() {
        return bitmap;
    }

    //imposta l'immagine bitmap
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    //ritorna la posizione rispetto all'asse x
    public float getX() {
        return x;
    }

    //imposta la posizione della rana rispetto all'asse x
    public void setX(float x) {
        this.x = x;
    }

    //ritorna la posizione rispetto all'asse y
    public float getY() {
        return y;
    }

    //imposta la posizione della rana rispetto all'asse y
    public void setY(float y) {
        this.y = y;
    }

    //imposta la velocità sull'asse y, viene usato quando la rana viene colpita per fermarla
    public void setyVel(float yVel) {
        this.yVel = yVel;
    }

    //imposta la velocità sull'asse x, viene usato quando la rana viene colpita per fermarla
    public void setxVel(float xVel) {
        this.xVel = xVel;
    }



    //Costruttore della classe Frog, che prende in input l'immagine della rana e la grandezza di ogni passo o salto.
    //Setta la posizione della rana rispetto all'asse x all'inizio della partita, dando un valore di 50
    public Frog(Bitmap bitmap, float step){
        this.step = step;
        this.bitmap = bitmap;
        x = 50;
    }

    /*funzione che imposta di quanto deve spostarsi la rana sui due assi quando effettua un passo o salto.
    Viene chiamata quando la rana si muove sulle 4 direzioni, ricevendo in input:
    x=1 se ci si deve spostare verso destra
    x=-1 se si deve andare verso sinistra
    y=-1 se si deve andare avanti
    y=1 se si deve andare indietro

     */
    public void frogJump(float x, float y){
        this.x += step * x;
        this.y += step * y;
    }



    /*funzione che disegna il rettangolo in cui la rana è contenuta, avendo in input un Canvas.
      Viene creato un rettangolo prendendo in input i parametri della Bitmap di riferimento e le posizioni della rana rispetto ai vari assi,
      dopodichè la width del rettangolo viene modificata per motivi di coerenza tra immagine e contesto di gioco, tramite l'intero boxWidth.

    * */
    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap, x, y, null);
        Rect startFrogRect = new Rect((int)getX(), (int)getY(),(int)(getX()+getBitmap().getWidth()), (int)(getY()+getBitmap().getHeight()));
        int boxWidth = Math.round(startFrogRect.width() * widthPercentage);

        box = new Rect(startFrogRect.left + boxWidth, startFrogRect.top, startFrogRect.right - boxWidth, startFrogRect.bottom);
        x += xVel;
    }
}
