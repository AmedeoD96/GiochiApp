package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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


    private final String ORDINAL_VALUE = "°    ";


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
        GameHelper.Games value = GameHelper.Games.valueOf(bundle.getString(GameHelper.GAME_LEADERBOADR_EXTRA_GAME));

        User currentUser = UsersManager.getInstance().getCurrentUser();

        int position = 0;
        Collection<User> allUserSorted = UsersManager.getInstance().getAllUserSort(getGameOrder(value), false);

        for (User user : allUserSorted) {

            boolean isCurrentUser = false;
            if (user.equals(currentUser)) {
                isCurrentUser = true;
                find = position;
            }

            String ordinalPosition = String.valueOf(position + 1);

            String userName = ordinalPosition + ORDINAL_VALUE + user.name;
            Parent parent = new Parent(userName, user.getScore(value), isCurrentUser, position);
            elementCreator.addElement(parent);
            parentObject.add(parent);

            position++;
        }

        gameName.setText(GameHelper.getGameName(value));

        return parentObject;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsersManager.getInstance().saveCurrentUser();
    }


    UsersManager.OrderType getGameOrder(GameHelper.Games game) {

        UsersManager.OrderType orderDecided = UsersManager.OrderType.SCORE_TETRIS;
        switch (game) {

            case TETRIS:
                orderDecided = UsersManager.OrderType.SCORE_TETRIS;
                break;
            case GAME_2048:
                orderDecided = UsersManager.OrderType.SCORE_2048;
                break;
            case ENDLESS:
                orderDecided = UsersManager.OrderType.SCORE_ALIENRUN;
                break;
            case HELICOPTER:
                orderDecided = UsersManager.OrderType.SCORE_HELICOPTER;
                break;
            case FROGGER:
                orderDecided = UsersManager.OrderType.SCORE_FROGGER;
                break;
        }
        return orderDecided;
    }

}
