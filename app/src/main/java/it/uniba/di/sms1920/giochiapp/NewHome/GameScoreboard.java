package it.uniba.di.sms1920.giochiapp.NewHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        initializeElement();

        navigation = findViewById(R.id.navigation);
        navigation.getMenu().getItem(0).setCheckable(false);

        ScoreAdapter scoreAdapter = new ScoreAdapter(getApplicationContext(), initData());
        scoreAdapter.setParentAndIconExpandOnClick(false);

        recyclerView.setAdapter(scoreAdapter);

        imageButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(find);
            }
        });



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
        imageButton = findViewById(R.id.ibFind);
    }

    private List<ParentObject> initData(){

        ElementCreator elementCreator = ElementCreator.get(getApplicationContext());

        List<ParentObject> parentObject = new ArrayList<>();

        elementCreator.clearTitles();
        TextView gameName = findViewById(R.id.gameName);
        Bundle bundle = getIntent().getExtras();
        int value = bundle.getInt("game");

        User currentUser = UsersManager.getInstance().getCurrentUser();
        boolean flag;
        int position;
        switch (value){
            case 0:
                flag = false;
                position = 0;
                Collection<User> allUserTetris = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_TETRIS, false);

                for(User user: allUserTetris){
                    if(user.equals(currentUser)){
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(user.name, user.scoreTetris, flag, position);
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
                    if(user.equals(currentUser)){
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(user.name, user.score2048, flag, position);
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
                    if (user.equals(currentUser)){
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(user.name, user.scoreAlienrun, flag, position);
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
                    if(user.equals(currentUser)){
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(user.name, user.scoreHelicopter, flag, position);
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
                    if(user.equals(currentUser)){
                        flag = true;
                        find = position;
                    }
                    Parent parent = new Parent(user.name, user.scoreFrogger, flag, position);
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
