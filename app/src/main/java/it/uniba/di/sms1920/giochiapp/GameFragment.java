package it.uniba.di.sms1920.giochiapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import static android.content.Context.MODE_PRIVATE;

public class GameFragment extends Fragment {
    private static final String HIGH_SCORE = "high score temp 2048";
    Context context = GlobalApplicationContext.getAppContext();

    private List<Game> gameList;
    private RecyclerView recyclerView;

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
        return view;
    }

    private void initializeUI(){
        gameList = new ArrayList<>();
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
        Game frogger = new Game("Frogger", getScoreFrogger(), R.drawable.frog);
        gameList.add(frogger);
    }

    private int getTetrisHighScore(){
        SharedPreferences tetrisPref = context.getSharedPreferences("info", MODE_PRIVATE);
        int highScore = tetrisPref.getInt("TopScore", 0);
        return highScore;
    }

    private int get2048HighScore(){
        SharedPreferences game2048Pref = context.getSharedPreferences("info", MODE_PRIVATE);
        long highScore = game2048Pref.getLong(HIGH_SCORE, 0);
        String num = String.valueOf(highScore);
        int i = Integer.valueOf(num);
        return i;
    }

    private int getHelicopterHighScore(){
        SharedPreferences tetrisPref = context.getSharedPreferences("info", MODE_PRIVATE);
        int highScore = tetrisPref.getInt("TopScoreHelicopter", 0);
        return highScore;
    }

    private int getScoreEndless(){
        SharedPreferences endless = context.getSharedPreferences("info", MODE_PRIVATE);
        int highScore = endless.getInt("TopScoreEndless", 0);
        return highScore;
    }

    private int getScoreFrogger() {
        SharedPreferences frogger = context.getSharedPreferences("info", MODE_PRIVATE);
        int highScore = frogger.getInt("TopScoreFrogger", 0);
        return highScore;
    }
}
