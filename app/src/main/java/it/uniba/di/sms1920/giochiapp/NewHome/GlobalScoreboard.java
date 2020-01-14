package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.uniba.di.sms1920.giochiapp.Leaderboard.MyAdapter;
import it.uniba.di.sms1920.giochiapp.Leaderboard.TitleChild;
import it.uniba.di.sms1920.giochiapp.Leaderboard.TitleCreator;
import it.uniba.di.sms1920.giochiapp.Leaderboard.TitleParent;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class GlobalScoreboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageButton button;
    private int find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_scoreboard);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        initializeElement();

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(find);
            }
        });

        UsersManager.getInstance().getAllUsers(new UsersManager.IUsersLoadedCallback() {
            @Override
            public void OnAllUsersLoaded(Map<String, User> users) {
                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), initData());
                myAdapter.setParentClickableViewAnimationDefaultDuration();
                myAdapter.setParentAndIconExpandOnClick(true);

                recyclerView.setAdapter(myAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });


        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_leaderboard);

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
        recyclerView = findViewById(R.id.rvGlobalScoreboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        button = findViewById(R.id.findButton);
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<ParentObject> parentObject = new ArrayList<>();
        titleCreator.clearTitles();

        Collection<User> allUser = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.TOTAL_SCORE, false);

        int count = 1;
        User currentUser = UsersManager.getInstance().getCurrentUser();
        int position = 0;
        for (User user : allUser) {
            boolean isCurrentUser = false;
            if(user.equals(currentUser)){
                //position = count-1;
                isCurrentUser = true;
                find = position;
            }
            TitleParent title = new TitleParent("#" + count + "   "  + user.name, user.getTotalScore(), isCurrentUser, position);
            position++;
            count++;

            List<Object> childList = new ArrayList<>();

            childList.add(new TitleChild(
                            "Tetris",
                            "2048",
                            "Alien Run",
                            "Rocket",
                            "Frogger",
                            user.scoreTetris,
                            user.score2048,
                            user.scoreAlienrun,
                            user.scoreHelicopter,
                            user.scoreFrogger
                    )
            );

            title.setChildObjectList(childList);
            titleCreator.addTitle(title);
            parentObject.add(title);
        }

        return parentObject;
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        MyAdapter myAdapter = new MyAdapter(this, initData());
        myAdapter.setParentClickableViewAnimationDefaultDuration();
        myAdapter.setParentAndIconExpandOnClick(true);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
