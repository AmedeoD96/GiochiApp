package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;
import it.uniba.di.sms1920.giochiapp.Home.Game;
import it.uniba.di.sms1920.giochiapp.Home.GameAdapter;
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
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
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
                }
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0 && navigationView.isShown()){
                    navigationView.setVisibility(View.GONE);
                }else if(dy<0){
                    navigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initializeElement(){
        gameList = new ArrayList<>();
        recyclerView = findViewById(R.id.rvGameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void createGameList(){
        tetris = new Game("Tetris", getTetrisHighScore(), R.drawable.tetris_launch_app);
        gameList.add(tetris);

        game2048 = new Game("2048", get2048HighScore(), R.drawable.game2048);
        gameList.add(game2048);

        alienRun = new Game("Alien Run", getScoreAlienRun(), R.drawable.endless);
        gameList.add(alienRun);

        rocket = new Game("Rocket", getRocketScore(), R.drawable.helicopterrun);
        gameList.add(rocket);

        frogger = new Game("Frogger", getScoreFrogger(), R.drawable.frog);
        gameList.add(frogger);
    }

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

    //Metodi per la toolbar superiore
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), SetUserName.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Gamelist", "ondestroy");
        UsersManager.getInstance().saveCurrentUser();
    }
}
