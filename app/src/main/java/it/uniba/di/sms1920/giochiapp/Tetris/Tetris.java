package it.uniba.di.sms1920.giochiapp.Tetris;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.giochiapp.R;


public class Tetris extends AppCompatActivity {
    TetrisCtrl mTetrisCtrl;
    Point mScreenSize = new Point(0, 0);
    Point mMousePos = new Point(-1, -1);
    int mCellSize = 0;
    boolean mIsTouchMove = false;
    MediaPlayer mMediaPlayer;
    Button rotate, left, right, down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //si setta il il fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tetris);
        initilizeUI();
        listener();


        //si ottengono le misure dello schermo
        DisplayMetrics dm = this.getApplicationContext().getResources().getDisplayMetrics();
        mScreenSize.x = dm.widthPixels;
        mScreenSize.y = dm.heightPixels;
        mCellSize = (mScreenSize.x / 8);

        //viene attivata la musica
        mMediaPlayer= MediaPlayer.create(Tetris.this,R.raw.tetris);
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);

        //viene inizializzato il controller
        initTetrisCtrl();

    }

    private void initilizeUI(){
        //si istanziano i bottoni
        left = findViewById(R.id.btnLeft);
        right = findViewById(R.id.btnRight);
        down = findViewById(R.id.btnBottom);
        rotate = findViewById(R.id.btnRotate);
    }

    private void listener(){
        //Listener dei bottoni
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTetrisCtrl.block2Rotate();
            }
        });

        left.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTetrisCtrl.block2Left();
            }
        });

        right.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTetrisCtrl.block2Right();
            }
        });

        down.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTetrisCtrl.block2Bottom();
            }
        });

    }

    void initTetrisCtrl() {
        //si istanzia il controller
        mTetrisCtrl = new TetrisCtrl(this);
        for(int i=0; i <= 7; i++) {
            //vengono create bitmap per ogni tipo di blocco
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cell0 + i);
            mTetrisCtrl.addCellImage(i, bitmap);
        }
        //si ottiene il layout
        RelativeLayout layoutCanvas = findViewById(R.id.layoutCanvas);
        layoutCanvas.addView(mTetrisCtrl);
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch( event.getAction() ) {
            case MotionEvent.ACTION_DOWN :
                //alla fine della pressione sullo schermo
                //si è settato il non movimento
                mIsTouchMove = false;
                //se il tap avvenisse nella parte inferiore dello schermo si ottengono le coordinate
                if( event.getY() < (int)(mScreenSize.y * 0.75)) {
                    mMousePos.x = (int) event.getX();
                    mMousePos.y = (int) event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE :
                //in caso di movimento sullo schermo
                if( mMousePos.x < 0 )
                    //se la posizione non fosse valida non avverrebbe nulla
                    break;
                if( (event.getX() - mMousePos.x) > mCellSize ) {
                    //in caso di swipe verso destra
                    //il blocco si muoverebbe verso destra
                    mTetrisCtrl.block2Right();
                    //si ottengono le coordinate
                    mMousePos.x = (int) event.getX();
                    mMousePos.y = (int) event.getY();
                    mIsTouchMove = true;
                } else if( (mMousePos.x - event.getX()) > mCellSize ) {
                    //in caso di swipe verso sinistra
                    //il blocco si muoverebbe verso sinistra
                    mTetrisCtrl.block2Left();
                    //si ottengono le coordinate
                    mMousePos.x = (int) event.getX();
                    mMousePos.y = (int) event.getY();
                    mIsTouchMove = true;
                }
                break;
            case MotionEvent.ACTION_UP :
                //in caso di sollevamento dopo la pressione
                if(!mIsTouchMove && mMousePos.x > 0 )
                    //se il blocco non si è mosso e la posizione sull'asse delle x fosse valida
                    //il blocco ruoterebbe
                    mTetrisCtrl.block2Rotate();
                mMousePos.set(-1, -1);
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTetrisCtrl.pauseGame();
        mMediaPlayer.stop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mTetrisCtrl.restartGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
    }
}
