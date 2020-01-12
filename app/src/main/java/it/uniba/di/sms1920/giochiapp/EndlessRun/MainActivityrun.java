package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import it.uniba.di.sms1920.giochiapp.R;


public class MainActivityrun extends Activity {
    MediaPlayer mMediaPlayer;
    GamePanel gamePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics ds = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);
        Constants.SCREEN_WIDTH = ds.widthPixels;
        Constants.SCREEN_HEIGHT = ds.heightPixels;

        mMediaPlayer= MediaPlayer.create(MainActivityrun.this, R.raw.doodlejumpsong);
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);

        gamePanel = new GamePanel(this);
        setContentView(gamePanel);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gamePanel.close();
        mMediaPlayer.stop();
    }
}
