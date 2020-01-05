package it.uniba.di.sms1920.giochiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.Database.DBOpenHelper;
import it.uniba.di.sms1920.giochiapp.Helicopter.Player;

import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.AppBarLayout;

public class MainActivity extends AppCompatActivity {
    /*private static final String HIGH_SCORE = "high score temp 2048";

    private RecyclerView rvGame;
    private List<Game> gameList = new ArrayList<>();*/

    final Fragment fragment1 = new GameFragment();
    final Fragment fragment2 = new LeaderboardFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = fragment1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
/*
        initializeUI();

        //Crea la lista dei giochi
        createElement();

        //Imposto il layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvGame.setLayoutManager(layoutManager);

        //Imposto l'adapter
        GameAdapter gameAdapter = new GameAdapter(gameList);
        rvGame.setAdapter(gameAdapter);
 */
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return  true;
                case R.id.navigation_leaderboard:
                    fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /*
    private void initializeUI(){
        rvGame = findViewById(R.id.rvGame);
    }

    private void createElement(){
        Game tetris = new Game("Tetris", getTetrisHighScore(), R.drawable.tetris_launch_app);
        gameList.add(tetris);

        Game game2048 = new Game("2048", get2048HighScore(), R.drawable.game2048);
        gameList.add(game2048);

        Game endless = new Game("Endless", getScoreEndless(), R.drawable.endless);
        gameList.add(endless);

        Game elicottero = new Game("Elicottero", getHelicopterHighScore(), R.drawable.flappy);
        gameList.add(elicottero);

        //Aggiusta questo
        Game frogger = new Game("Frogger", Integer.parseInt(String.valueOf(56)), R.drawable.frog);
        gameList.add(frogger);
    }
 */
/*
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

    private int getHelicopterHighScore(){
        SharedPreferences tetrisPref = getSharedPreferences("info", MODE_PRIVATE);
        int highScore = tetrisPref.getInt("TopScoreHelicopter", 0);
        return highScore;
    }

    private int getScoreEndless(){
        SharedPreferences endless = getSharedPreferences("info", MODE_PRIVATE);
        int highScore = endless.getInt("TopScoreEndless", 0);
        return highScore;
    }

 */
}
