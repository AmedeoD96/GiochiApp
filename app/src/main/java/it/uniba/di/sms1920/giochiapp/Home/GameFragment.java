package it.uniba.di.sms1920.giochiapp.Home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class GameFragment extends Fragment {
    private static final String HIGH_SCORE = "high score temp 2048";
    Context context = GlobalApplicationContext.getAppContext();

    private List<Game> gameList;
    private RecyclerView recyclerView;

    private Game tetris;
    private Game game2048;
    private Game endless;
    private Game elicottero;
    private Game frogger;


    public GameFragment(){
        //Deve essere vuoto
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        initializeUI();
        recyclerView = view.findViewById(R.id.rvGame);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        createElement();
        GameAdapter mAdapter = new GameAdapter(gameList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        tetris.setHighScore(getTetrisHighScore());
        game2048.setHighScore(get2048HighScore());
        elicottero.setHighScore(getHelicopterHighScore());
        endless.setHighScore(getScoreEndless());
        frogger.setHighScore(getScoreFrogger());
        return view;
    }

    private void initializeUI(){
        gameList = new ArrayList<>();
    }

    private void createElement(){
        tetris = new Game("Tetris", getTetrisHighScore(), R.drawable.tetris_launch_app);
        gameList.add(tetris);

        game2048 = new Game("2048", get2048HighScore(), R.drawable.game2048);
        gameList.add(game2048);

        endless = new Game("Endless", getScoreEndless(), R.drawable.endless);
        gameList.add(endless);

        elicottero = new Game("Elicottero", getHelicopterHighScore(), R.drawable.helicopterrun);
        gameList.add(elicottero);

        //Aggiusta questo
        frogger = new Game("Frogger", getScoreFrogger(), R.drawable.frog);
        gameList.add(frogger);
    }

    private int getTetrisHighScore(){
        // SharedPreferences tetrisPref = context.getSharedPreferences("info", MODE_PRIVATE);
        // int highScore = tetrisPref.getInt("TopScoreTetris", 0);
        // return highScore;

        return UsersManager.getInstance().getCurrentUser().scoreTetris;
    }

    private int get2048HighScore(){
        // SharedPreferences game2048Pref = context.getSharedPreferences("info", MODE_PRIVATE);
        //  long highScore = game2048Pref.getLong(HIGH_SCORE, 0);
        //  String num = String.valueOf(highScore);
        //  int i = Integer.valueOf(num);
        // return i;

        return UsersManager.getInstance().getCurrentUser().score2048;
    }

    private int getHelicopterHighScore(){
        // SharedPreferences tetrisPref = context.getSharedPreferences("info", MODE_PRIVATE);
        // int highScore = tetrisPref.getInt("TopScoreHelicopter", 0);
        // return highScore;

        return UsersManager.getInstance().getCurrentUser().scoreHelicopter;
    }

    private int getScoreEndless(){
        // SharedPreferences endless = context.getSharedPreferences("info", MODE_PRIVATE);
        // int highScore = endless.getInt("TopScoreEndless", 0);
        // return highScore;

        return UsersManager.getInstance().getCurrentUser().scoreHelicopter;
    }

    private int getScoreFrogger() {
        // SharedPreferences frogger = context.getSharedPreferences("info", MODE_PRIVATE);
        // int highScore = frogger.getInt("TopScoreFrogger", 0);
        // return highScore;

        return UsersManager.getInstance().getCurrentUser().scoreFrogger;
    }

    @Override
    public void onResume() {
        //serve per avere il best score nella home premendo il tasto indietro di android. 
        super.onResume();

        gameList.remove(tetris);
        gameList.remove(game2048);
        gameList.remove(elicottero);
        gameList.remove(endless);
        gameList.remove(frogger);

        GameAdapter mAdapter = new GameAdapter(gameList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        createElement();
    }

    @Override
    public void onStop() {
        super.onStop();
        tetris.setHighScore(getTetrisHighScore());
        game2048.setHighScore(get2048HighScore());
        elicottero.setHighScore(getHelicopterHighScore());
        endless.setHighScore(getScoreEndless());
        frogger.setHighScore(getScoreFrogger());
    }

    @Override
    public void onPause() {
        super.onPause();
        tetris.setHighScore(getTetrisHighScore());
        game2048.setHighScore(get2048HighScore());
        elicottero.setHighScore(getHelicopterHighScore());
        endless.setHighScore(getScoreEndless());
        frogger.setHighScore(getScoreFrogger());
    }


}
