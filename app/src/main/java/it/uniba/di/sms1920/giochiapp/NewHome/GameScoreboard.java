package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.GameLeaderboard.ElementCreator;
import it.uniba.di.sms1920.giochiapp.GameLeaderboard.Parent;
import it.uniba.di.sms1920.giochiapp.GameLeaderboard.ScoreAdapter;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class GameScoreboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BottomNavigationView navigation;
    private ImageButton imageButton;
    private int find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scoreboard);


        initializeElement();

        navigation = findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);

        ScoreAdapter scoreAdapter = new ScoreAdapter(getApplicationContext(), initData());
        scoreAdapter.setParentAndIconExpandOnClick(false);

        recyclerView.setAdapter(scoreAdapter);

        imageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(find);
            }
        });


        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
    }


    private void initializeElement() {
        recyclerView = findViewById(R.id.rvSingleGameScoreboard);
        recyclerView.setLayoutManager(new CenterLayoutManager(this));
        navigation = findViewById(R.id.navigation);
        imageButton = findViewById(R.id.ibFind);
    }

    private List<ParentObject> initData() {

        ElementCreator elementCreator = ElementCreator.get(getApplicationContext());

        List<ParentObject> parentObject = new ArrayList<>();

        elementCreator.clearTitles();
        TextView gameName = findViewById(R.id.gameName);
        Bundle bundle = getIntent().getExtras();
        int value = bundle.getInt("game");

        User currentUser = UsersManager.getInstance().getCurrentUser();
        boolean flag;
        int position;
        switch (value) {
            case 0:
                flag = false;
                position = 0;
                Collection<User> allUserTetris = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_TETRIS, false);

                for (User user : allUserTetris) {
                    if (user.equals(currentUser)) {
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(position + 1 + "°    " + user.name, user.scoreTetris, flag, position);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                    flag = false;
                    position++;
                }

                gameName.setText("Tetris");
                break;
            case 1:
                flag = false;
                position = 0;
                Collection<User> allUser2048 = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_2048, false);

                for (User user : allUser2048) {
                    if (user.equals(currentUser)) {
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(position + 1 + "°    " + user.name, user.score2048, flag, position);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                    flag = false;
                    position++;
                }
                gameName.setText("2048");
                break;
            case 2:
                flag = false;
                position = 0;
                Collection<User> allUserAlienRun = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_ALIENRUN, false);

                for (User user : allUserAlienRun) {
                    if (user.equals(currentUser)) {
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(position + 1 + "°    " + user.name, user.scoreAlienrun, flag, position);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                    flag = false;
                    position++;
                }
                gameName.setText("Alien Run");
                break;
            case 3:
                flag = false;
                position = 0;
                Collection<User> allUserRocket = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_HELICOPTER, false);

                for (User user : allUserRocket) {
                    if (user.equals(currentUser)) {
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(position + 1 + "°    " + user.name, user.scoreHelicopter, flag, position);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                    flag = false;
                    position++;
                }
                gameName.setText("Rocket");
                break;
            case 4:
                flag = false;
                position = 0;
                Collection<User> allUserFrogger = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_FROGGER, false);

                for (User user : allUserFrogger) {
                    if (user.equals(currentUser)) {
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(position + 1 + "°    " + user.name, user.scoreFrogger, flag, position);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                    flag = false;
                    position++;
                }

                gameName.setText("Frogger");
                break;

        }
        return parentObject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsersManager.getInstance().saveCurrentUser();
    }

}
