package it.uniba.di.sms1920.giochiapp.NewHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.GameLeaderboard.ElementCreator;
import it.uniba.di.sms1920.giochiapp.GameLeaderboard.Parent;
import it.uniba.di.sms1920.giochiapp.GameLeaderboard.ScoreAdapter;
import it.uniba.di.sms1920.giochiapp.MainActivity;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class GameScoreboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scoreboard);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        initializeElement();

        navigation = findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);

        ScoreAdapter scoreAdapter = new ScoreAdapter(getApplicationContext(), initData());
        scoreAdapter.setParentAndIconExpandOnClick(false);

        recyclerView.setAdapter(scoreAdapter);



        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        Intent gameList = new Intent(getApplicationContext(), GameList.class);
                        startActivity(gameList);
                        finish();
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
                if(dy > 0 && navigation.isShown()){
                    navigation.animate().translationY(navigation.getHeight()).alpha(1.0f).setListener(null);
                }else if(dy<0){
                    navigation.animate().translationY(0).setDuration(200).alpha(1.0f).setListener(null);
                }
            }
        });
    }

    private void initializeElement(){
        recyclerView = findViewById(R.id.rvSingleGameScoreboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        navigation = findViewById(R.id.navigation);
    }

    private List<ParentObject> initData(){

        ElementCreator elementCreator = ElementCreator.get(getApplicationContext());

        List<ParentObject> parentObject = new ArrayList<>();

        elementCreator.clearTitles();
        TextView gameName = findViewById(R.id.gameName);
        Bundle bundle = getIntent().getExtras();
        int value = bundle.getInt("game");

        switch (value){
            case 0:
                Collection<User> allUserTetris = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_TETRIS, false);

                for(User user: allUserTetris){
                    Parent parent = new Parent(user.name, user.scoreTetris);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }

                gameName.setText("Tetris");
                break;
            case 1:
                Collection<User> allUser2048 = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_2048, false);

                for (User user : allUser2048) {
                    Parent parent = new Parent(user.name, user.score2048);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
                gameName.setText("2048");
                break;
            case 2:
                Collection<User> allUserAlienRun = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_ALIENRUN, false);

                for (User user : allUserAlienRun) {
                    Parent parent = new Parent(user.name, user.scoreAlienrun);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
                gameName.setText("Alien Run");
                break;
            case 3:
                Collection<User> allUserRocket = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_HELICOPTER, false);

                for (User user : allUserRocket) {
                    Parent parent = new Parent(user.name, user.scoreHelicopter);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
                gameName.setText("Rocket");
                break;
            case 4:
                Collection<User> allUserFrogger = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_FROGGER, false);

                for (User user : allUserFrogger) {
                    Parent parent = new Parent(user.name, user.scoreFrogger);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
                gameName.setText("Frogger");
                break;

        }
        return parentObject;
    }

    /*
    public void setRecyclerView(GameList.GameLeaderboard gameLeaderboard){
        ScoreAdapter scoreAdapter = new ScoreAdapter(getApplicationContext(), initData(gameLeaderboard));
        scoreAdapter.setParentAndIconExpandOnClick(false);

        recyclerView.setAdapter(scoreAdapter);
    }
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Gamescoreboard", "ondestroy");
        UsersManager.getInstance().saveCurrentUser();
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
}
