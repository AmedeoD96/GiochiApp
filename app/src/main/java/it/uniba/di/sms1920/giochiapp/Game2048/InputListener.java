package it.uniba.di.sms1920.giochiapp.Game2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;

import it.uniba.di.sms1920.giochiapp.R;

public class InputListener implements View.OnTouchListener {

    private static final int SWIPE_MIN_DISTANCE = 0;
    private static final int SWIPE_THRESHOLD_VELOCITY = 25;
    private static final int MOVE_THRESHOLD = 250;
    private static final int RESET_STARTING = 10;
    private final MainView mView;
    private float x;
    private float y;
    private float lastDx;
    private float lastDy;
    private float previousX;
    private float previousY;
    private float startingX;
    private float startingY;
    private int previousDirection = 1;
    private int veryLastDirection = 1;

    private final int MOVE_LEFT;
    // Se si è effettuato o meno una mossa
    private boolean hasMoved = false;
    // Disabilita gli swipe se l'utente inizia la pressione di un'icona
    private boolean beganOnIcon = false;

    InputListener(MainView view) {
        super();
        this.mView = view;
        MOVE_LEFT = 3;
    }

    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {


            case MotionEvent.ACTION_DOWN:
                //in caso di pressione da parte dell'utente
                //si ottengono le coordinate di pressione iniziali e finali dello swipe
                x = event.getX();
                y = event.getY();
                startingX = x;
                startingY = y;
                previousX = x;
                previousY = y;
                //per calcolare la distanza
                lastDx = 0;
                lastDy = 0;
                hasMoved = false;
                //se sono state premute le icone
                beganOnIcon = iconPressed(mView.sXNewGame, mView.sYIcons)
                        || iconPressed(mView.sXUndo, mView.sYIcons);
                return true;
            case MotionEvent.ACTION_MOVE:
                //in caso di movimento del dito sullo schermo
                //si otterrebbero le coordinate
                x = event.getX();
                y = event.getY();
                //se il gioco è attivo e non sono state premute icone
                if (mView.game.isActive() && !beganOnIcon) {
                    //si calcola la distanza tra nuovo punto e precedente
                    float dx = x - previousX;
                    //controlla che il dito non si sia fermato e stia facendo uno swipe orizzontale
                    boolean ControlChangingDirectionOrizzontal = Math.abs(lastDx + dx) < Math.abs(lastDx) + Math.abs(dx);
                    //controlla che la distanza percorsa non sia troppa
                    boolean ControlLenghtOrizzontal = Math.abs(dx) > RESET_STARTING;
                    //controlla se sia stato fatto un movimento abbastanza lungo
                    boolean ControlMinLenghtOrizzontal = Math.abs(x - startingX) > SWIPE_MIN_DISTANCE;
                    if (ControlChangingDirectionOrizzontal && ControlLenghtOrizzontal && ControlMinLenghtOrizzontal) {
                        //ottengo i valori sulla direzione
                        startingX = x;
                        startingY = y;
                        lastDx = dx;
                        previousDirection = veryLastDirection;
                    }
                    //in caso in cui non venga creato alcun percorso orizzontale
                    if (lastDx == 0) {
                        lastDx = dx;
                    }
                    //si setta la distanza verticale
                    float dy = y - previousY;
                    //controlla che il dito non si sia fermato e stia facendo uno swipe verticale
                    boolean ControlChangingDirectionVertical = Math.abs(lastDy + dy) < Math.abs(lastDy) + Math.abs(dy);
                    //controlla che la distanza percorsa non sia troppa
                    boolean ControlLenghtVertical = Math.abs(dy) > RESET_STARTING;
                    //controlla se sia stato fatto un movimento abbastanza lungo
                    boolean ControlMinLenghtVertical = Math.abs(y - startingY) > SWIPE_MIN_DISTANCE;
                    if ( ControlChangingDirectionVertical && ControlLenghtVertical
                            && ControlMinLenghtVertical) {
                        //ottengo i valori sulla direzione
                        startingX = x;
                        startingY = y;
                        lastDy = dy;
                        previousDirection = veryLastDirection;
                    }
                    //in caso in cui non venga creato alcun percorso verticale
                    if (lastDy == 0) {
                        lastDy = dy;
                    }
                    //se l'utente non ha ancora fatto uno swipe e ha percorso una distanza minima per fare uno swipe
                    if (pathMoved() > SWIPE_MIN_DISTANCE * SWIPE_MIN_DISTANCE && !hasMoved) {
                        boolean moved = false;
                        //Verticale
                        //controllo sulla direzione dello swipe
                        if (((dy >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dy) >= Math.abs(dx)) || y - startingY >= MOVE_THRESHOLD) && previousDirection % 2 != 0) {
                            moved = true;
                            //si assegna per conoscere l'ultima direzione effettuata
                            previousDirection = previousDirection * 2;
                            veryLastDirection = 2;
                            //movimento effettivo verso il basso
                            int MOVE_DOWN = 2;
                            mView.game.move(MOVE_DOWN);
                        } else if (((dy <= -SWIPE_THRESHOLD_VELOCITY && Math.abs(dy) >= Math.abs(dx)) || y - startingY <= -MOVE_THRESHOLD) && previousDirection % 3 != 0) {
                            moved = true;
                            //si assegna per conoscere l'ultima direzione effettuata
                            previousDirection = previousDirection * 3;
                            veryLastDirection = 3;
                            //movimento effettivo verso l'alto
                            int MOVE_UP = 0;
                            mView.game.move(MOVE_UP);
                        }
                        //Orizzontale
                        if (((dx >= SWIPE_THRESHOLD_VELOCITY && Math.abs(dx) >= Math.abs(dy)) || x - startingX >= MOVE_THRESHOLD) && previousDirection % 5 != 0) {
                            moved = true;
                            //si assegna per conoscere l'ultima direzione effettuata
                            previousDirection = previousDirection * 5;
                            veryLastDirection = 5;
                            //movimento effettivo verso destra
                            int MOVE_RIGHT = 1;
                            mView.game.move(MOVE_RIGHT);
                        } else if (((dx <= -SWIPE_THRESHOLD_VELOCITY && Math.abs(dx) >= Math.abs(dy)) || x - startingX <= -MOVE_THRESHOLD) && previousDirection % 7 != 0) {
                            moved = true;
                            //si assegna per conoscere l'ultima direzione effettuata
                            previousDirection = previousDirection * 7;
                            veryLastDirection = 7;
                            //movimento effettivo verso sinistra
                            mView.game.move(MOVE_LEFT);
                        }
                        if (moved) {
                            //ottengo le coordinate
                            hasMoved = true;
                            startingX = x;
                            startingY = y;
                        }
                    }
                }
                //coordinate precedenti
                previousX = x;
                previousY = y;
                return true;
            case MotionEvent.ACTION_UP:
                //in caso di rilascio della pressione
                x = event.getX();
                y = event.getY();
                previousDirection = 1;
                veryLastDirection = 1;
                //Input del menu
                if (!hasMoved) {
                    //se è stata premuta l'incona di nuovo gioco
                    if (iconPressed(mView.sXNewGame, mView.sYIcons)) {
                        //se si fosse in stato di game over
                        if (!mView.game.gameLost()) {
                            //si illuminerebbe il bottone di reset
                            new AlertDialog.Builder(mView.getContext())
                                    .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mView.game.newGame();
                                        }
                                    })
                                    .setNegativeButton(R.string.continue_game, null)
                                    .setTitle(R.string.reset_dialog_title)
                                    .setMessage(R.string.reset_dialog_message)
                                    .show();
                        } else {
                            //altrimenti lancia la nuova partita
                            mView.game.newGame();
                        }

                    } else if (iconPressed(mView.sXUndo, mView.sYIcons)) {
                        // in caso di pressione del tasto undo si ritorna alla mossa precedente
                        mView.game.revertUndoState();
                    } else if (isTap(2) && inRange(mView.startingX, x, mView.endingX)
                            && inRange(mView.startingY, x, mView.endingY) && mView.continueButtonEnabled) {
                        //se fosse presente il bottone di endless mode e si premesse in un range specifico
                        //si attiverebbe la modalità endless
                        mView.game.setEndlessMode();
                    }
                }
        }
        return true;
    }

    private float pathMoved() {
        //somma delle distanze al quadrato
        return (x - startingX) * (x - startingX) + (y - startingY) * (y - startingY);
    }

    //ritorna un booleano pari all'effettiva pressione di una icona
    private boolean iconPressed(int sx, int sy) {
        return isTap(1) && inRange(sx, x, sx + mView.iconSize)
                && inRange(sy, y, sy + mView.iconSize);
    }

    //controllo di distanze
    private boolean inRange(float starting, float check, float ending) {
        return (starting <= check && check <= ending);
    }

    private boolean isTap(int factor) {
        return pathMoved() <= mView.iconSize * mView.iconSize * factor;
    }
}
