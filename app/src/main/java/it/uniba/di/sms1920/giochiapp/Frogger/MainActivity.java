package it.uniba.di.sms1920.giochiapp.Frogger;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;

import it.uniba.di.sms1920.giochiapp.R;

public class MainActivity extends AppCompatActivity {

    GameView gv;
    int score;
    int lives;
    MediaPlayer mediaPlayerFrogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gv = new GameView(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gv);

        //impostazione musica di gioco
        mediaPlayerFrogger= MediaPlayer.create(MainActivity.this, R.raw.route1);
        mediaPlayerFrogger.start();
        mediaPlayerFrogger.setLooping(true);


    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayerFrogger.stop();
        score = gv.points;
        lives = gv.heart.getLives();
        this.gv.thread.setRunning(false);
        boolean retry = false;
        while (retry == false) {
            try {
                gv.thread.join();
                retry = true;
            } catch (InterruptedException e) {
            }
        }
        this.gv = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.gv == null) {
            gv = new GameView(this);
            gv.points = score;
            gv.heart.setLives(lives);
            setContentView(gv);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayerFrogger.stop();


    }


}
