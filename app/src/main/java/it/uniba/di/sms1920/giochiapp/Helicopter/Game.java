package it.uniba.di.sms1920.giochiapp.Helicopter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import it.uniba.di.sms1920.giochiapp.R;

public class Game extends Activity {
    MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Non si mostra la title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Si ottiene lo schermo interno
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new GamePanel(this));

        //per la riproduzione musicale
        mMediaPlayer= MediaPlayer.create(Game.this, R.raw.jetpacktheme);
        mMediaPlayer.start();
        mMediaPlayer.setLooping(true);
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

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.stop();
    }
}
