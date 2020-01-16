package it.uniba.di.sms1920.giochiapp.Game2048;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import it.uniba.di.sms1920.giochiapp.R;

public class MainView extends View {

    //Internal Constants
    static final int BASE_ANIMATION_TIME = 100000000;
    private static final String TAG = MainView.class.getSimpleName();
    private static final float MERGING_ACCELERATION = (float) -0.5;
    private static final float INITIAL_VELOCITY = (1 - MERGING_ACCELERATION) / 4;
    public final int numCellTypes = 21;
    private final BitmapDrawable[] bitmapCell = new BitmapDrawable[numCellTypes];
    public final MainGame game;
    //Internal variables
    private final Paint paint = new Paint();
    public boolean hasSaveState = false;
    public boolean continueButtonEnabled = false;
    public int startingX;
    public int startingY;
    public int endingX;
    public int endingY;
    //Icon variables
    public int sYIcons;
    public int sXNewGame;
    public int sXUndo;
    public int iconSize;
    //Misc
    boolean refreshLastTime = true;
    boolean showHelp;
    //Timing
    private long lastFPSTime = System.nanoTime();
    //Text
    private float titleTextSize;
    private float bodyTextSize;
    private float headerTextSize;
    private float instructionsTextSize;
    private float gameOverTextSize;
    //Layout variables
    private int cellSize = 0;
    private float textSize = 0;
    private float cellTextSize = 0;
    private int gridWidth = 0;
    private int textPaddingSize;
    private int iconPaddingSize;
    //Assets
    private Drawable backgroundRectangle;
    private Drawable lightUpRectangle;
    private Drawable fadeRectangle;
    private Bitmap background = null;
    private BitmapDrawable loseGameOverlay;
    private BitmapDrawable winGameContinueOverlay;
    private BitmapDrawable winGameFinalOverlay;
    //Text variables
    private int sYAll;
    private int titleStartYAll;
    private int bodyStartYAll;
    private int eYAll;
    private int titleWidthHighScore;
    private int titleWidthScore;

    public MainView(Context context) {
        super(context);

        Resources resources = context.getResources();
        //caricamento risorse
        game = new MainGame(context, this);
        try {
            //ottenimento degli assets
            backgroundRectangle = resources.getDrawable(R.drawable.background_rectangle);
            lightUpRectangle = resources.getDrawable(R.drawable.light_up_rectangle);
            fadeRectangle = resources.getDrawable(R.drawable.fade_rectangle);
            this.setBackgroundColor(resources.getColor(R.color.background));
            //si utilizzano gli assets
            paint.setAntiAlias(true);
        } catch (Exception e) {
            Log.e(TAG, "Error getting assets?", e);
        }
        //si inizializza l'inputListener
        setOnTouchListener(new InputListener(this));
        game.newGame();
    }

    private static int log2(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //attua un reset della trasparenza dello schermo
        canvas.drawBitmap(background, 0, 0, paint);

        //mostra il testo dello score
        drawScoreText(canvas);

        //illumina il tasto di reset a fine partita
        if (!game.isActive() && !game.aGrid.isAnimationActive()) {
            drawNewGameButton(canvas, true);
        }

        //mostra le celle
        drawCells(canvas);

        //testo di game over
        if (!game.isActive()) {
            drawEndGameState(canvas);
        }

        //testo di modalità infinita
        if (!game.canContinue()) {
            drawEndlessText(canvas);
        }

        //attuerebbe un refresh dello schermo se ci fosse ancora un animation in stato di run
        if (game.aGrid.isAnimationActive()) {
            invalidate(startingX, startingY, endingX, endingY);
            tick();

         //attuerebbe un refresh un'ultima volta in caso di fine del gioco
        } else if (!game.isActive() && refreshLastTime) {
            invalidate();
            refreshLastTime = false;
        }
    }

    //cambia la taglia dello schermo, crea il background, crea le celle
    @Override
    protected void onSizeChanged(int width, int height, int oldW, int oldH) {
        super.onSizeChanged(width, height, oldW, oldH);
        getLayout(width, height);
        createBitmapCells();
        createBackgroundBitmap(width, height);
        createOverlays();
    }

    //draw dei bordi
    private void drawDrawable(Canvas canvas, Drawable draw, int startingX, int startingY, int endingX, int endingY) {
        draw.setBounds(startingX, startingY, endingX, endingY);
        draw.draw(canvas);
    }

    //draw delle caselle di testo
    private void drawCellText(Canvas canvas, int value) {
        int textShiftY = centerText();
        if (value >= 8) {
            paint.setColor(getResources().getColor(R.color.text_white));
        } else {
            paint.setColor(getResources().getColor(R.color.text_black));
        }
        canvas.drawText("" + value, cellSize / 2, cellSize / 2 - textShiftY, paint);
    }

    private void drawScoreText(Canvas canvas) {
        //Draw del testo del punteggio
        paint.setTextSize(bodyTextSize);
        paint.setTextAlign(Paint.Align.CENTER);

        int bodyWidthHighScore = (int) (paint.measureText("" + game.highScore));
        int bodyWidthScore = (int) (paint.measureText("" + game.score));

        int textWidthHighScore = Math.max(titleWidthHighScore, bodyWidthHighScore) + textPaddingSize * 2;
        int textWidthScore = Math.max(titleWidthScore, bodyWidthScore) + textPaddingSize * 2;

        int textMiddleHighScore = textWidthHighScore / 2;
        int textMiddleScore = textWidthScore / 2;

        int eXHighScore = endingX;
        int sXHighScore = eXHighScore - textWidthHighScore;

        int eXScore = sXHighScore - textPaddingSize;
        int sXScore = eXScore - textWidthScore;

        //Output della casella legata all'highscore
        //set dei bordi
        backgroundRectangle.setBounds(sXHighScore, sYAll, eXHighScore, eYAll);
        backgroundRectangle.draw(canvas);
        paint.setTextSize(titleTextSize);
        paint.setColor(getResources().getColor(R.color.text_brown));
        canvas.drawText(getResources().getString(R.string.high_score), sXHighScore + textMiddleHighScore, titleStartYAll, paint);
        paint.setTextSize(bodyTextSize);
        paint.setColor(getResources().getColor(R.color.text_white));
        canvas.drawText(String.valueOf(game.highScore), sXHighScore + textMiddleHighScore, bodyStartYAll, paint);


        //Output della casella di punteggio
        backgroundRectangle.setBounds(sXScore, sYAll, eXScore, eYAll);
        backgroundRectangle.draw(canvas);
        paint.setTextSize(titleTextSize);
        paint.setColor(getResources().getColor(R.color.text_brown));
        canvas.drawText(getResources().getString(R.string.score), sXScore + textMiddleScore, titleStartYAll, paint);
        paint.setTextSize(bodyTextSize);
        paint.setColor(getResources().getColor(R.color.text_white));
        canvas.drawText(String.valueOf(game.score), sXScore + textMiddleScore, bodyStartYAll, paint);
    }

    private void drawNewGameButton(Canvas canvas, boolean lightUp) {

        if (lightUp) {
            drawDrawable(canvas,
                    lightUpRectangle,
                    sXNewGame,
                    sYIcons,
                    sXNewGame + iconSize,
                    sYIcons + iconSize
            );
        } else {
            drawDrawable(canvas,
                    backgroundRectangle,
                    sXNewGame,
                    sYIcons, sXNewGame + iconSize,
                    sYIcons + iconSize
            );
        }

        drawDrawable(canvas,
                getResources().getDrawable(R.drawable.ic_action_refresh),
                sXNewGame + iconPaddingSize,
                sYIcons + iconPaddingSize,
                sXNewGame + iconSize - iconPaddingSize,
                sYIcons + iconSize - iconPaddingSize
        );
    }

    private void drawUndoButton(Canvas canvas) {

        drawDrawable(canvas,
                backgroundRectangle,
                sXUndo,
                sYIcons, sXUndo + iconSize,
                sYIcons + iconSize
        );

        drawDrawable(canvas,
                getResources().getDrawable(R.drawable.ic_action_undo),
                sXUndo + iconPaddingSize,
                sYIcons + iconPaddingSize,
                sXUndo + iconSize - iconPaddingSize,
                sYIcons + iconSize - iconPaddingSize
        );
    }

    //draw dell'header dell'app
    private void drawHeader(Canvas canvas) {
        paint.setTextSize(headerTextSize);
        paint.setColor(getResources().getColor(R.color.text_black));
        paint.setTextAlign(Paint.Align.LEFT);
        int textShiftY = centerText() * 2;
        int headerStartY = sYAll - textShiftY;
        canvas.drawText(getResources().getString(R.string.header), startingX, headerStartY, paint);
    }

    //draw delle istruzioni durante il primo utilizzo dell'app
    private void drawInstructions(Canvas canvas) {
        paint.setTextSize(instructionsTextSize);
        paint.setTextAlign(Paint.Align.LEFT);
        int textShiftY = centerText() * 2;
        canvas.drawText(getResources().getString(R.string.instructions),
                startingX, endingY - textShiftY + textPaddingSize, paint);
    }

    //draw del background
    private void drawBackground(Canvas canvas) {
        drawDrawable(canvas, backgroundRectangle, startingX, startingY, endingX, endingY);
    }

    //ottiene il drawable delle 16 celle del backgorund
    private void drawBackgroundGrid(Canvas canvas) {
        Resources resources = getResources();
        Drawable backgroundCell = resources.getDrawable(R.drawable.cell_rectangle);
        // Mostra la griglia di gioco
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                drawDrawable(canvas, backgroundCell, sX, sY, eX, eY);
            }
        }
    }

    private void drawCells(Canvas canvas) {
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        // Mostra le celle singole
        for (int xx = 0; xx < game.numSquaresX; xx++) {
            for (int yy = 0; yy < game.numSquaresY; yy++) {
                int sX = startingX + gridWidth + (cellSize + gridWidth) * xx;
                int eX = sX + cellSize;
                int sY = startingY + gridWidth + (cellSize + gridWidth) * yy;
                int eY = sY + cellSize;

                Tile currentTile = game.grid.getCellContent(xx, yy);
                if (currentTile != null) {
                    //Ottiene e rappresenta il valore della casella corrente
                    int value = currentTile.getValue();
                    int index = log2(value);

                    //Controlla se ci siano animazioni attive
                    ArrayList<AnimationCell> aArray = game.aGrid.getAnimationCell(xx, yy);
                    boolean animated = false;
                    for (int i = aArray.size() - 1; i >= 0; i--) {
                        AnimationCell aCell = aArray.get(i);
                        //se l'animazione non fosse attiva verrebbe saltata
                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) {
                            animated = true;
                        }
                        if (!aCell.isActive()) {
                            continue;
                        }

                        //le animazioni vengono mostrate
                        if (aCell.getAnimationType() == MainGame.SPAWN_ANIMATION) {
                            //viene effettuato uno scale del testo
                            double percentDone = aCell.getPercentageDone();
                            float textScaleSize = (float) (percentDone);
                            paint.setTextSize(textSize * textScaleSize);

                            //viene effettuato un resize delle celle
                            float cellScaleSize = (cellSize / 2) * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                            //Animazioni di merge
                        } else if (aCell.getAnimationType() == MainGame.MERGE_ANIMATION) {
                            double percentDone = aCell.getPercentageDone();
                            // la grandezza del testo varia in base alla percentuale dell'animazione
                            //all'inizio diminuisce e poi aumenta
                            float textScaleSize = (float) (1 + INITIAL_VELOCITY * percentDone
                                    + MERGING_ACCELERATION * percentDone * percentDone / 2);
                            paint.setTextSize(textSize * textScaleSize);

                            float cellScaleSize = cellSize / 2 * (1 - textScaleSize);
                            bitmapCell[index].setBounds((int) (sX + cellScaleSize), (int) (sY + cellScaleSize), (int) (eX - cellScaleSize), (int) (eY - cellScaleSize));
                            bitmapCell[index].draw(canvas);
                            //animazioni di movimento
                        } else if (aCell.getAnimationType() == MainGame.MOVE_ANIMATION) {
                            double percentDone = aCell.getPercentageDone();
                            int tempIndex = index;
                            if (aArray.size() >= 2) {
                                tempIndex = tempIndex - 1;
                            }
                            //si ottengono le coordinate delle celle coinvolte nell'animazione
                            int previousX = aCell.extras[0];
                            int previousY = aCell.extras[1];
                            int currentX = currentTile.getX();
                            int currentY = currentTile.getY();
                            //distanza percorsa
                            int dX = (int) ((currentX - previousX) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            int dY = (int) ((currentY - previousY) * (cellSize + gridWidth) * (percentDone - 1) * 1.0);
                            //si settano i bordi e si attua un draw
                            bitmapCell[tempIndex].setBounds(sX + dX, sY + dY, eX + dX, eY + dY);
                            bitmapCell[tempIndex].draw(canvas);
                        }
                        animated = true;
                    }

                    //Se nono ci fossero animazioni attive si attuerebbe solo un draw della cella
                    if (!animated) {
                        bitmapCell[index].setBounds(sX, sY, eX, eY);
                        bitmapCell[index].draw(canvas);
                    }
                }
            }
        }
    }

    //mostra lo stato di fine partita
    private void drawEndGameState(Canvas canvas) {
        double alphaChange = 1;
        //viene abilitato il bottone di continue
        continueButtonEnabled = false;
        //per ogni animazione controlla le percentuali delle animazioni di fading tipiche delle schermate di vittoria o game over
        for (AnimationCell animation : game.aGrid.globalAnimation) {
            if (animation.getAnimationType() == MainGame.FADE_GLOBAL_ANIMATION) {
                alphaChange = animation.getPercentageDone();
            }
        }
        BitmapDrawable displayOverlay = null;
        if (game.gameWon()) {
            if (game.canContinue()) {
                continueButtonEnabled = true;
                //si mostra la schermata di vittoria e di possibilità di continuare
                displayOverlay = winGameContinueOverlay;
            } else {
                //si mostra la schermata di vittoria definitiva
                displayOverlay = winGameFinalOverlay;
            }
        } else if (game.gameLost()) {
            //si mostra la schermata di game over
            displayOverlay = loseGameOverlay;
        }

        if (displayOverlay != null) {
            //vengono settate le misure delle schermate
            displayOverlay.setBounds(startingX, startingY, endingX, endingY);
            displayOverlay.setAlpha((int) (255 * alphaChange));
            displayOverlay.draw(canvas);
        }
    }

    private void drawEndlessText(Canvas canvas) {
        //si mostra il testo della modalità senza fine
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(bodyTextSize);
        paint.setColor(getResources().getColor(R.color.text_black));
        canvas.drawText(getResources().getString(R.string.endless), startingX, sYIcons - centerText() * 2, paint);
    }

    private void createEndGameStates(Canvas canvas, boolean win, boolean showButton) {
        //crea la schermata di fine gioco, sia in caso di completamento che di game over
        int width = endingX - startingX;
        int length = endingY - startingY;
        int middleX = width / 2;
        int middleY = length / 2;
        if (win) {
            lightUpRectangle.setAlpha(127);
            drawDrawable(canvas, lightUpRectangle, 0, 0, width, length);
            lightUpRectangle.setAlpha(255);
            paint.setColor(getResources().getColor(R.color.text_white));
            paint.setAlpha(255);
            paint.setTextSize(gameOverTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            int textBottom = middleY - centerText();
            canvas.drawText(getResources().getString(R.string.you_win), middleX, textBottom, paint);
            paint.setTextSize(bodyTextSize);
            String text = showButton ? getResources().getString(R.string.go_on) :
                    getResources().getString(R.string.for_now);
            canvas.drawText(text, middleX, textBottom + textPaddingSize * 2 - centerText() * 2, paint);
        } else {
            fadeRectangle.setAlpha(127);
            drawDrawable(canvas, fadeRectangle, 0, 0, width, length);
            fadeRectangle.setAlpha(255);
            paint.setColor(getResources().getColor(R.color.text_black));
            paint.setAlpha(255);
            paint.setTextSize(gameOverTextSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getResources().getString(R.string.game_over), middleX, middleY - centerText(), paint);
        }
    }

    //si crea la bitmap del background
    private void createBackgroundBitmap(int width, int height) {
        background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        drawHeader(canvas);
        drawNewGameButton(canvas, false);
        drawUndoButton(canvas);
        drawBackground(canvas);
        drawBackgroundGrid(canvas);
        if (showHelp) drawInstructions(canvas);

    }

    //si crea la bitmap delle celle
    private void createBitmapCells() {
        Resources resources = getResources();
        int[] cellRectangleIds = getCellRectangleIds();
        paint.setTextAlign(Paint.Align.CENTER);
        //per ogni cella vengono settati i parametri
        for (int xx = 1; xx < bitmapCell.length; xx++) {
            int value = (int) Math.pow(2, xx);
            paint.setTextSize(cellTextSize);
            float tempTextSize = cellTextSize * cellSize * 0.9f / Math.max(cellSize * 0.9f, paint.measureText(String.valueOf(value)));
            paint.setTextSize(tempTextSize);
            Bitmap bitmap = Bitmap.createBitmap(cellSize, cellSize, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            //si attua il draw
            drawDrawable(canvas, resources.getDrawable(cellRectangleIds[xx]), 0, 0, cellSize, cellSize);
            drawCellText(canvas, value);
            bitmapCell[xx] = new BitmapDrawable(resources, bitmap);
        }
    }

    //si ottengono i colori delle celle
    private int[] getCellRectangleIds() {
        int[] cellRectangleIds = new int[numCellTypes];
        cellRectangleIds[0] = R.drawable.cell_rectangle;
        cellRectangleIds[1] = R.drawable.cell_rectangle_2;
        cellRectangleIds[2] = R.drawable.cell_rectangle_4;
        cellRectangleIds[3] = R.drawable.cell_rectangle_8;
        cellRectangleIds[4] = R.drawable.cell_rectangle_16;
        cellRectangleIds[5] = R.drawable.cell_rectangle_32;
        cellRectangleIds[6] = R.drawable.cell_rectangle_64;
        cellRectangleIds[7] = R.drawable.cell_rectangle_128;
        cellRectangleIds[8] = R.drawable.cell_rectangle_256;
        cellRectangleIds[9] = R.drawable.cell_rectangle_512;
        cellRectangleIds[10] = R.drawable.cell_rectangle_1024;
        cellRectangleIds[11] = R.drawable.cell_rectangle_2048;
        for (int xx = 12; xx < cellRectangleIds.length; xx++) {
            cellRectangleIds[xx] = R.drawable.cell_rectangle_4096;
        }
        return cellRectangleIds;
    }

    private void createOverlays() {
        Resources resources = getResources();
        //Inizializza l'overlay in caso di vittoria continua, vittoria definitiva, sconfitta
        //Vengono creati gli stati
        Bitmap bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        createEndGameStates(canvas, true, true);
        winGameContinueOverlay = new BitmapDrawable(resources, bitmap);
        bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        createEndGameStates(canvas, true, false);
        winGameFinalOverlay = new BitmapDrawable(resources, bitmap);
        bitmap = Bitmap.createBitmap(endingX - startingX, endingY - startingY, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        createEndGameStates(canvas, false, false);
        loseGameOverlay = new BitmapDrawable(resources, bitmap);
    }

    private void tick() {
        //si attua un tick di tutte le animazioni per passare all'animazione successiva
        long currentTime = System.nanoTime();
        game.aGrid.tickAll(currentTime - lastFPSTime);
        lastFPSTime = currentTime;
    }

    public void resyncTime() {
        lastFPSTime = System.nanoTime();
    }

    private void getLayout(int width, int height) {
        cellSize = Math.min(width / (game.numSquaresX + 1), height / (game.numSquaresY + 3));
        gridWidth = cellSize / 7;
        int screenMiddleX = width / 2;
        int screenMiddleY = height / 2;
        int boardMiddleY = screenMiddleY + cellSize / 2;
        iconSize = cellSize / 2;

        //Dimensione della griglia
        double halfNumSquaresX = game.numSquaresX / 2d;
        double halfNumSquaresY = game.numSquaresY / 2d;
        startingX = (int) (screenMiddleX - (cellSize + gridWidth) * halfNumSquaresX - gridWidth / 2);
        endingX = (int) (screenMiddleX + (cellSize + gridWidth) * halfNumSquaresX + gridWidth / 2);
        startingY = (int) (boardMiddleY - (cellSize + gridWidth) * halfNumSquaresY - gridWidth / 2);
        endingY = (int) (boardMiddleY + (cellSize + gridWidth) * halfNumSquaresY + gridWidth / 2);

        float widthWithPadding = endingX - startingX;

        // Dimensione del testo
        paint.setTextSize(cellSize);
        textSize = cellSize * cellSize / Math.max(cellSize, paint.measureText("0000"));

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(1000);
        instructionsTextSize = Math.min(
                1000f * (widthWithPadding / (paint.measureText(getResources().getString(R.string.instructions)))),
                textSize / 1.5f
        );
        gameOverTextSize = Math.min(
                Math.min(
                        1000f * ((widthWithPadding - gridWidth * 2) / (paint.measureText(getResources().getString(R.string.game_over)))),
                        textSize * 2
                ),
                1000f * ((widthWithPadding - gridWidth * 2) / (paint.measureText(getResources().getString(R.string.you_win))))
        );

        paint.setTextSize(cellSize);
        cellTextSize = textSize;
        titleTextSize = textSize / 3;
        bodyTextSize = (int) (textSize / 1.5);
        headerTextSize = textSize * 2;
        textPaddingSize = (int) (textSize / 3);
        iconPaddingSize = (int) (textSize / 5);

        paint.setTextSize(titleTextSize);

        int textShiftYAll = centerText();
        //Variabili statiche di misura
        sYAll = (int) (startingY - cellSize * 1.5);
        titleStartYAll = (int) (sYAll + textPaddingSize + titleTextSize / 2 - textShiftYAll);
        bodyStartYAll = (int) (titleStartYAll + textPaddingSize + titleTextSize / 2 + bodyTextSize / 2);

        titleWidthHighScore = (int) (paint.measureText(getResources().getString(R.string.high_score)));
        titleWidthScore = (int) (paint.measureText(getResources().getString(R.string.score)));
        paint.setTextSize(bodyTextSize);
        textShiftYAll = centerText();
        eYAll = (int) (bodyStartYAll + textShiftYAll + bodyTextSize / 2 + textPaddingSize);

        sYIcons = (startingY + eYAll) / 2 - iconSize / 2;
        sXNewGame = (endingX - iconSize);
        sXUndo = sXNewGame - iconSize  * 3 / 2 - iconPaddingSize + 40;
        resyncTime();
    }

    //fa in modo che ogni paint sia centrato
    private int centerText() {
        return (int) ((paint.descent() + paint.ascent()) / 2);
    }


}
