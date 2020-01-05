package it.uniba.di.sms1920.giochiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.Database.DBOpenHelper;
import it.uniba.di.sms1920.giochiapp.Helicopter.Player;

public class MainActivity extends AppCompatActivity {
    private static final String HIGH_SCORE = "high score temp 2048";

    private RecyclerView rvGame;
    private List<Game> gameList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();

        //Crea la lista dei giochi
        createElement();

        //Imposto il layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvGame.setLayoutManager(layoutManager);

        //Imposto l'adapter
        GameAdapter gameAdapter = new GameAdapter(gameList);
        rvGame.setAdapter(gameAdapter);
    }

    private void initializeUI(){
        rvGame = findViewById(R.id.rvGame);
    }

    private void createElement(){
        Game tetris = new Game("Tetris", getTetrisHighScore(), R.drawable.tetris_launch_app);
        gameList.add(tetris);

        Game game2048 = new Game("2048", get2048HighScore(), R.drawable.game2048);
        gameList.add(game2048);

        //Aggiustare il punteggio di questi
        Game endless = new Game("Endless", Integer.parseInt(String.valueOf(47)), R.drawable.endless);
        gameList.add(endless);

        Game pinball = new Game("Pinball", Integer.parseInt(String.valueOf(3231)), R.drawable.pinball);
        gameList.add(pinball);

        Game flappy = new Game("Flappy", Integer.parseInt(String.valueOf(32)), R.drawable.flappy);
        gameList.add(flappy);

        Game frogger = new Game("Frogger", Integer.parseInt(String.valueOf(56)), R.drawable.frog);
        gameList.add(frogger);
    }



    private int getTetrisHighScore(){
        SharedPreferences tetrisPref = getSharedPreferences("info", MODE_PRIVATE);
        int highScore = tetrisPref.getInt("TopScore", 0);
        return highScore;
    }

    private int get2048HighScore(){
        SharedPreferences game2048Pref = getSharedPreferences("info", MODE_PRIVATE);
        long highScore = game2048Pref.getLong(HIGH_SCORE, 0);
        String num = String.valueOf(highScore);
        int i = Integer.valueOf(num);
        return i;
    }

}
