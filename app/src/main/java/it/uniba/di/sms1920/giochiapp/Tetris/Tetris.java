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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tetris);


        DisplayMetrics dm = this.getApplicationContext().getResources().getDisplayMetrics();
        mScreenSize.x = dm.widthPixels;
        mScreenSize.y = dm.heightPixels;
        mCellSize = (int)(mScreenSize.x / 8);

        mMediaPlayer= MediaPlayer.create(Tetris.this,R.raw.tetris);
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);

        initTetrisCtrl();
    }

    void initTetrisCtrl() {
        mTetrisCtrl = new TetrisCtrl(this);
        for(int i=0; i <= 7; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cell0 + i);
            mTetrisCtrl.addCellImage(i, bitmap);
        }
        RelativeLayout layoutCanvas = findViewById(R.id.layoutCanvas);
        layoutCanvas.addView(mTetrisCtrl);
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch( event.getAction() ) {
            case MotionEvent.ACTION_DOWN :
                mIsTouchMove = false;
                if( event.getY() < (int)(mScreenSize.y * 0.75)) {
                    mMousePos.x = (int) event.getX();
                    mMousePos.y = (int) event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE :
                if( mMousePos.x < 0 )
                    break;
                if( (event.getX() - mMousePos.x) > mCellSize ) {
                    mTetrisCtrl.block2Right();
                    mMousePos.x = (int) event.getX();
                    mMousePos.y = (int) event.getY();
                    mIsTouchMove = true;
                } else if( (mMousePos.x - event.getX()) > mCellSize ) {
                    mTetrisCtrl.block2Left();
                    mMousePos.x = (int) event.getX();
                    mMousePos.y = (int) event.getY();
                    mIsTouchMove = true;
                }
                break;
            case MotionEvent.ACTION_UP :
                if( mIsTouchMove == false && mMousePos.x > 0 )
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

    public void onClick(View view) {
        switch( view.getId() ) {
            case R.id.btnLeft :
                mTetrisCtrl.block2Left();
                break;
            case R.id.btnRight :
                mTetrisCtrl.block2Right();
                break;
            case R.id.btnBottom :
                mTetrisCtrl.block2Bottom();
                break;
            /*case R.id.btnRotate :
                mTetrisCtrl.block2Rotate();
                break;*/
        }
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
