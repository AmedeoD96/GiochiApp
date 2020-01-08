package it.uniba.di.sms1920.giochiapp.Frogger;

import android.os.Bundle;
import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    GameView gv;
    int score;
    int lives;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gv = new GameView(this);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gv);

        //TODO sta da mettere la musica a frogger


    }

    @Override
    protected void onPause() {
        super.onPause();
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
        //chiamare il metodo di salvataggio su database

    }


}
