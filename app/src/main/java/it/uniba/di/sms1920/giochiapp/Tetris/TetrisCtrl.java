package it.uniba.di.sms1920.giochiapp.Tetris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class TetrisCtrl extends View {
    Context context;
    final int EMPTY_WHAT_MESSAGE = 0;
    final long SMALL_TIME = 10;
    final long LARGE_TIME = 1000;

    final int MatrixSizeH = 10;
    final int MatrixSizeV = 18;
    final int DirRotate = 0;
    final int DirLeft = 1;
    final int DirRight = 2;
    final int DirDown = 3;
    final int TimerGapStart = 1000;
    int TimerGapNormal = TimerGapStart;
    int TimerGapFast = 50;
    int mTimerGap = TimerGapNormal;

    int[][] mArMatrix = new int[MatrixSizeV][MatrixSizeH];
    double mBlockSize = 0;
    Point mScreenSize = new Point(0, 0);
    int mNewBlockArea = 5;
    int[][] mArNewBlock = new int[mNewBlockArea][mNewBlockArea];
    int[][] mArNextBlock = new int[mNewBlockArea][mNewBlockArea];
    Point mNewBlockPos = new Point(0, 0);
    Bitmap[] mArBmpCell = new Bitmap[8];
    AlertDialog mDlgMsg = null;
    int mScore = 0;

    //ottiene l'area di un blocco
    Rect getBlockArea(int x, int y) {
        Rect rtBlock = new Rect();
        rtBlock.left = (int)(x * mBlockSize);
        rtBlock.right = (int)(rtBlock.left + mBlockSize);
        rtBlock.bottom = mScreenSize.y - (int)(y * mBlockSize);
        rtBlock.top = (int)(rtBlock.bottom - mBlockSize);
        return rtBlock;
    }

    //sceglie un numero tra 0 e 7 per la generazione dei blocchi
    int random() {
        return (int)(Math.random() * (7 - 1 + 1)) + 1;
    }

    //costruttore
    public TetrisCtrl(Context context) {
        super(context);
        this.context = context;
    }

    //inizializza le variabili della grandezza dello schermo
    void initVariables(Canvas canvas) {
        mScreenSize.x = canvas.getWidth();
        mScreenSize.y = canvas.getHeight();
        mBlockSize = mScreenSize.x / MatrixSizeH;

        //avvio il gioco
        startGame();
    }

    //genera le coordinate di spawn dei nuovi blocchi
    //scorre in orizzontale e in verticale l'area di spawn
    //si assegna a 0 tutta la p'area
    void addNewBlock(int[][] arBlock) {
        for(int i=0; i < mNewBlockArea; i++) {
            for(int j=0; j < mNewBlockArea; j++) {
                arBlock[i][j] = 0;
            }
        }

        //si aggiorna la posizione di spawn del nuovo blocco
        mNewBlockPos.x = (MatrixSizeH - mNewBlockArea) / 2;
        mNewBlockPos.y = MatrixSizeV - mNewBlockArea;

        int blockType = random();

        switch(blockType) {
            case 1:
                // Block 1 : --
                arBlock[2][1] = 1;
                arBlock[2][2] = 1;
                arBlock[2][3] = 1;
                arBlock[2][4] = 1;
                break;
            case 2:
                // Block 2 : └-
                arBlock[3][1] = 2;
                arBlock[2][1] = 2;
                arBlock[2][2] = 2;
                arBlock[2][3] = 2;
                break;
            case 3:
                // Block 3 : -┘
                arBlock[2][1] = 3;
                arBlock[2][2] = 3;
                arBlock[2][3] = 3;
                arBlock[3][3] = 3;
                break;
            case 4:
                // Block 4 : ▣
                arBlock[2][2] = 4;
                arBlock[2][3] = 4;
                arBlock[3][2] = 4;
                arBlock[3][3] = 4;
                break;
            case 5:
                // Block 5 : ＿｜￣
                arBlock[3][3] = 5;
                arBlock[3][2] = 5;
                arBlock[2][2] = 5;
                arBlock[2][1] = 5;
                break;
            case 6:
                // Block 6 : ＿｜＿
                arBlock[2][1] = 6;
                arBlock[2][2] = 6;
                arBlock[2][3] = 6;
                arBlock[3][2] = 6;
                break;
            default:
                // Block 7 : ￣｜＿
                arBlock[2][3] = 7;
                arBlock[2][2] = 7;
                arBlock[3][2] = 7;
                arBlock[3][1] = 7;
                break;
        }
        redraw();
    }

    //invalida l'intera view
    public void redraw() {
        this.invalidate();
    }

    //controlla se un'area di schermo sia libera
    boolean checkBlockSafe(int[][] arNewBlock, Point posBlock) {
        for(int i=0; i < mNewBlockArea ; i++) {
            for(int j=0; j < mNewBlockArea ; j++) {
                if( arNewBlock[i][j] == 0 )
                    continue;
                int x = posBlock.x + j;
                int y = posBlock.y + i;
                //se la cella non fosse libera
                if(!checkCellSafe(x, y))
                    return false;
            }
        }
        return true;
    }

    //controlla se la singola cella sia vuota
    boolean checkCellSafe(int x, int y) {
        //si controlla se la cella sia in una zona della schermata realmente utilizzabile
        if( x < 0 )
            return false;
        if( x >= MatrixSizeH )
            return false;
        if( y < 0 )
            return false;
        if( y >= MatrixSizeV )
            return true;
        //se la schermata sia libera o meno
        return mArMatrix[y][x] <= 0;
    }

    void moveNewBlock(int dir, int[][] arNewBlock, Point posBlock) {
        switch( dir ) {
            case DirRotate :
                //in caso di rotazione
                int[][] arRotate = new int[mNewBlockArea ][mNewBlockArea ];
                for(int i=0; i < mNewBlockArea ; i++) {
                    for(int j=0; j < mNewBlockArea ; j++) {
                        //per ogni elemento della matrice si sposta nella casella reciproca rispetto a quella di appartenenza
                        arRotate[mNewBlockArea - j - 1][i] = arNewBlock[i][j];
                    }
                }
                for(int i=0; i < mNewBlockArea ; i++) {
                    System.arraycopy(arRotate[i], 0, arNewBlock[i], 0, mNewBlockArea);
                }
                break;
                //in caso di spostamento verso sinistra
            case DirLeft :
                posBlock.x --;
                break;
            //in caso di spostamento verso destra
            case DirRight :
                posBlock.x ++;
                break;
            //in caso di spostamento verso il basso
            case DirDown :
                posBlock.y --;
                break;
        }
    }


    //si clona una zona della schermata
    int[][] duplicateBlockArray(int[][] arBlock) {
        int size1 = mNewBlockArea , size2 = mNewBlockArea ;
        int[][] arClone = new int[size1][size2];
        for(int i=0; i < size1; i++) {
            System.arraycopy(arBlock[i], 0, arClone[i], 0, size2);
        }
        return arClone;
    }

    //copia tutti i blocchi in una seconda matrice
    void copyBlock2Matrix(int[][] arBlock, Point posBlock) {
        for(int i=0; i < mNewBlockArea ; i++) {
            for(int j=0; j < mNewBlockArea ; j++) {
                if( arBlock[i][j] == 0 )
                    continue;
                mArMatrix[posBlock.y + i][posBlock.x + j] = arBlock[i][j];
                arBlock[i][j] = 0;
            }
        }
    }

    //controllo se una riga sia completta
    void checkLineFilled() {
        int filledCount = 0;
        boolean bFilled;

        for(int i=0; i < MatrixSizeV; i++) {
            bFilled = true;
            for(int j=0; j < MatrixSizeH; j++) {
                //se ci fosse una casella vuota al boolean si assegna false
                if( mArMatrix[i][j] == 0 ) {
                    bFilled = false;
                    break;
                }
            }
            if(!bFilled)
                continue;

            //conto di righe completate
            filledCount ++;
            //copia della schermata tranne la riga completata
            for(int k=i+1; k < MatrixSizeV; k++) {
                System.arraycopy(mArMatrix[k], 0, mArMatrix[k - 1], 0, MatrixSizeH);
            }
            //si annullano gli elementi della riga completata
            for (int j = 0; j < MatrixSizeH; j++) {
                mArMatrix[MatrixSizeV - 1][j] = 0;
            }
            i--;
        }

        //si aggiorna il punteggio
        mScore += filledCount * filledCount;

        User user = UsersManager.getInstance().getCurrentUser();

        if(user.scoreTetris < mScore) {
            user.setScoreTetris(mScore);
        }
    }

    boolean isGameOver() {
        boolean canMove = checkBlockSafe(mArNewBlock, mNewBlockPos);
        return !canMove;
    }

    boolean moveNewBlock(int dir) {
        //si ottiene la posizione modificata ad ogni ciclo
        //se la posizione non cambiasse si va avanti nel codice
        int[][] arBackup = duplicateBlockArray( mArNewBlock );
        Point posBackup = new Point(mNewBlockPos);

        moveNewBlock(dir, mArNewBlock, mNewBlockPos);
        //si controlla se lo spostamento sia effettivamente consentito
        boolean canMove = checkBlockSafe(mArNewBlock, mNewBlockPos);
        //se i movimenti fossero consentiti il blocco verrebbe ricreato
        if( canMove ) {
            redraw();
            return true;
        }

        for(int i=0; i < mNewBlockArea ; i++) {
            //viene copiato il blocco
            System.arraycopy(arBackup[i], 0, mArNewBlock[i], 0, mNewBlockArea);
        }

        //si salva la nuova posizione del blocco appena creato
        mNewBlockPos.set(posBackup.x, posBackup.y);
        return false;
    }

    //si mostra il punteggio
    void showScore(Canvas canvas) {
        int fontSize = mScreenSize.x / 20;
        Paint pnt = new Paint();
        pnt.setTextSize(fontSize);
        pnt.setARGB(128, 255, 255,255);
        int posX = (int)(fontSize * 0.5);
        int poxY = (int)(fontSize * 1.5);
        pnt.setColor(Color.WHITE);
        canvas.drawText(getContext().getString(R.string.scoreMin) + mScore, posX, poxY, pnt);

        poxY += (int)(fontSize * 1.5);
        canvas.drawText(getContext().getString(R.string.topScore) + " " + UsersManager.getInstance().getCurrentUser().scoreTetris, posX, poxY, pnt);
    }

    //si mostra la matrice tabella di gioco
    void showMatrix(Canvas canvas, int[][] arMatrix) {
        for(int i=0; i < MatrixSizeV; i++) {
            for(int j=0; j < MatrixSizeH; j++) {
                showBlockImage(canvas, j, i, arMatrix[i][j]);
            }
        }
    }

    //Si crea l'immagine dei blocchi
    void showBlockImage(Canvas canvas, int blockX, int blockY, int blockType) {
        Rect rtBlock = getBlockArea(blockX, blockY);

        canvas.drawBitmap(mArBmpCell[blockType], null, rtBlock, null);
    }

    /*** Interface start ***/

    //si aggiunge l'immagine della cella
    public void addCellImage(int index, Bitmap bmp) {
        mArBmpCell[index] = bmp;
    }

    //spostamento a sinistra del blocco
    public void block2Left() {
        moveNewBlock(DirLeft);
    }

    //spostamento a destra del blocco
    public void block2Right() {
        moveNewBlock(DirRight);
    }

    public void block2Rotate() {
        moveNewBlock(DirRotate);
    }

    //si incrementa la velocità e si pone il blocco velocemente in basso
    public void block2Bottom() {
        mTimerFrame.removeMessages(EMPTY_WHAT_MESSAGE);
        mTimerGap = TimerGapFast;
        mTimerFrame.sendEmptyMessageDelayed(EMPTY_WHAT_MESSAGE, SMALL_TIME);
    }


    public void pauseGame() {
        if( mDlgMsg != null )
            return;

        mTimerFrame.removeMessages(0);
    }

    public void restartGame() {
        if( mDlgMsg != null )
            return;

        mTimerFrame.sendEmptyMessageDelayed(EMPTY_WHAT_MESSAGE, LARGE_TIME);
    }

    public void startGame() {
        mScore = 0;

        //ogni cella della zona di spawn dei blocchi viene posta a 0
        for(int i=0; i < MatrixSizeV; i++) {
            for(int j=0; j < MatrixSizeH; j++) {
                mArMatrix[i][j] = 0;
            }
        }

        //i nuovi blocchi vengono aggiunti
        addNewBlock(mArNewBlock);
        addNewBlock(mArNextBlock);
        //si setta il timer
        TimerGapNormal = TimerGapStart;
        //vengono mandati messaggi vuoti ad intervalli regolari
        mTimerFrame.sendEmptyMessageDelayed(EMPTY_WHAT_MESSAGE, SMALL_TIME);
    }

    /*** Interface end ***/
    void showDialog_GameOver() {
        //viene mostrata il messaggio di game over
        mDlgMsg = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_DARK)
                .setTitle(R.string.notice)
                .setMessage(getContext().getString(R.string.gameOverTetris) + mScore )
                .setPositiveButton(R.string.again,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDlgMsg = null;
                                startGame();
                            }
                        })
                .show();
    }

    //attua il draw dei blocchi
    public void onDraw(Canvas canvas) {
        if( mBlockSize < 1 )
            initVariables(canvas);
        canvas.drawColor(Color.DKGRAY);

        showMatrix(canvas, mArMatrix);
        showNewBlock(canvas);
        //mostra lo score
        showScore(canvas);
        //mostra il blocco successivo
        showNextBlock(canvas, mArNextBlock);
    }

    //mostra l'immagine dei blocchi
    void showNewBlock(Canvas canvas) {
        for(int i=0; i < mNewBlockArea ; i++) {
            for(int j=0; j < mNewBlockArea ; j++) {
                if( mArNewBlock[i][j] == 0 )
                    continue;
                showBlockImage(canvas, mNewBlockPos.x + j, mNewBlockPos.y + i, mArNewBlock[i][j]);
            }
        }
    }

    Handler mTimerFrame = new Handler() {
        public void handleMessage(Message msg) {
            boolean canMove = moveNewBlock(DirDown);
            //se l'utente non potesse muoversi si effettuerebbero tutti i controlli per il punteggio e per il game over
            if( !canMove ) {
                copyBlock2Matrix(mArNewBlock, mNewBlockPos);
                checkLineFilled();
                copyBlockArray(mArNextBlock, mArNewBlock);
                addNewBlock(mArNextBlock);
                TimerGapNormal -= 2;
                mTimerGap = TimerGapNormal;
                if( isGameOver() ) {
                    showDialog_GameOver();
                    return;
                }
            }

            this.sendEmptyMessageDelayed(0, mTimerGap);
        }
    };

    //copia matrici dei blocchi
    void copyBlockArray(int[][] arFrom, int[][] arTo) {
        for(int i=0; i < mNewBlockArea; i++) {
            System.arraycopy(arFrom[i], 0, arTo[i], 0, mNewBlockArea);
        }
    }

    //mostra in alto a destra il blocco successivo che apparirà sulla schermata
    void showNextBlock(Canvas canvas, int[][] arBlock) {
        for(int i=0; i < mNewBlockArea; i++) {
            for(int j=0; j < mNewBlockArea; j++) {
                int blockY = mNewBlockArea - i;
                showBlockColor(canvas, j, blockY, arBlock[i][j]);
            }
        }
    }

    //mostra i colori dei blocchi
    void showBlockColor(Canvas canvas, int blockX, int blockY, int blockType) {
        int[] arColor = {Color.argb(32,255,255,255),
                Color.argb(128,255,0,0),
                Color.argb(128,255,255,0),
                Color.argb(128,255,160,160),
                Color.argb(128,100,255,100),
                Color.argb(128,255,128,100),
                Color.argb(128,0,0,255),
                Color.argb(128,100,100,255)};
        int previewBlockSize = mScreenSize.x / 20;

        //si ottengono le misure del blocco
        Rect rtBlock = new Rect();
        rtBlock.top = (blockY - 1) * previewBlockSize;
        rtBlock.bottom = rtBlock.top + previewBlockSize;
        rtBlock.left = mScreenSize.x - previewBlockSize * (mNewBlockArea - blockX);
        rtBlock.right = rtBlock.left + previewBlockSize;
        int crBlock = arColor[ blockType ];

        //si impostano le caratteristiche del blocco
        Paint pnt = new Paint();
        pnt.setStyle(Paint.Style.FILL);
        pnt.setColor(crBlock);
        canvas.drawRect(rtBlock, pnt);
    }

}