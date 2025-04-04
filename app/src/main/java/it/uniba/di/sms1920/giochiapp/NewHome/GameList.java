package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class GameList extends AppCompatActivity {

    private List<Game> gameList;
    private RecyclerView recyclerView;

    private Game tetris;
    private Game game2048;
    private Game alienRun;
    private Game rocket;
    private Game frogger;

    static GameList _instance;


    public static GameList getInstance() {
        return _instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list);
        initializeElement();



        createGameList();

        /*Set adapter*/
        GameAdapter gameAdapter = new GameAdapter(gameList);
        recyclerView.setAdapter(gameAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        /*Set high score*/
        tetris.setHighScore(getTetrisHighScore());
        game2048.setHighScore(get2048HighScore());
        rocket.setHighScore(getRocketScore());
        alienRun.setHighScore(getScoreAlienRun());
        frogger.setHighScore(getScoreFrogger());

        /*Set up della bottomNavigationView*/
        final BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_leaderboard:
                        Intent globalScoreboard = new Intent(getApplicationContext(), GlobalScoreboard.class);
                        startActivity(globalScoreboard);
                        finish();
                        break;
                    case R.id.navigation_profile:
                        Intent profile = new Intent(getApplicationContext(), Profile.class);
                        startActivity(profile);
                        break;
                }
                return false;
            }
        });
    }

    /*Inizializzazione degli elementi del layout*/
    private void initializeElement(){
        gameList = new ArrayList<>();
        recyclerView = findViewById(R.id.rvGameList);

        /*Setta lo scorrimento della scroll view
        * - Verticale se il device è in portrait mode
        * - Orizzontale se il device è in landscape mode*/
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
        }


    }

    /*Creazione della lista giochi*/
    private void createGameList(){
        tetris = new Game(GameHelper.getGameName(GameHelper.Games.TETRIS), getTetrisHighScore(), R.drawable.tetris_launch_app);
        gameList.add(tetris);

        game2048 = new Game(GameHelper.getGameName(GameHelper.Games.GAME_2048), get2048HighScore(), R.drawable.game2048);
        gameList.add(game2048);

        alienRun = new Game(GameHelper.getGameName(GameHelper.Games.ENDLESS), getScoreAlienRun(), R.drawable.endless);
        gameList.add(alienRun);

        rocket = new Game(GameHelper.getGameName(GameHelper.Games.HELICOPTER), getRocketScore(), R.drawable.helicopterrun);
        gameList.add(rocket);

        frogger = new Game(GameHelper.getGameName(GameHelper.Games.FROGGER), getScoreFrogger(), R.drawable.frogger);
        gameList.add(frogger);
    }


    /*Get dei punteggi che l'utente ha totalizzato per ogni singolo gioco*/
    private int getTetrisHighScore(){
        return UsersManager.getInstance().getCurrentUser().scoreTetris;
    }

    private int get2048HighScore(){
        return UsersManager.getInstance().getCurrentUser().score2048;
    }

    private int getScoreAlienRun(){
        return UsersManager.getInstance().getCurrentUser().scoreAlienrun;
    }

    private int getRocketScore(){
        return UsersManager.getInstance().getCurrentUser().scoreHelicopter;
    }

    private int getScoreFrogger() {
        return UsersManager.getInstance().getCurrentUser().scoreFrogger;
    }

    /*Aggiornamento dei dati contenuti nella recycler view*/
    @Override
    public void onResume() {
        //serve per avere il best score nella home premendo il tasto indietro di android.
        super.onResume();

        gameList.remove(tetris);
        gameList.remove(game2048);
        gameList.remove(rocket);
        gameList.remove(alienRun);
        gameList.remove(frogger);

        GameAdapter mAdapter = new GameAdapter(gameList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        createGameList();
    }

    @Override
    public void onStop() {
        super.onStop();
        tetris.setHighScore(getTetrisHighScore());
        game2048.setHighScore(get2048HighScore());
        rocket.setHighScore(getRocketScore());
        alienRun.setHighScore(getScoreAlienRun());
        frogger.setHighScore(getScoreFrogger());
    }

    @Override
    public void onPause() {
        super.onPause();
        tetris.setHighScore(getTetrisHighScore());
        game2048.setHighScore(get2048HighScore());
        rocket.setHighScore(getRocketScore());
        alienRun.setHighScore(getScoreAlienRun());
        frogger.setHighScore(getScoreFrogger());
    }

    /*Salvataggio dei dati dell'utente corrente alla distruzione
     * dell'activity*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsersManager.getInstance().saveCurrentUser();
    }
}
