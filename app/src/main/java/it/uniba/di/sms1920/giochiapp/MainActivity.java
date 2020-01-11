package it.uniba.di.sms1920.giochiapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import it.uniba.di.sms1920.giochiapp.GameLeaderboard.SingleGameLeaderboard;
import it.uniba.di.sms1920.giochiapp.Home.GameFragment;
import it.uniba.di.sms1920.giochiapp.Leaderboard.LeaderboardFragment;
import it.uniba.di.sms1920.giochiapp.Setting.Setting;

public class MainActivity extends AppCompatActivity {

    final Fragment gameListFragment = new GameFragment();
    final Fragment leaderboardFragment = new LeaderboardFragment();
    final Fragment setting = new Setting();
    final SingleGameLeaderboard singleGameLeaderboard = new SingleGameLeaderboard();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = gameListFragment;

    static MainActivity _instance = null;

    public enum GameLeaderboard{
        TETRIS,
        GAME2048,
        ALIEN_RUN,
        ROCKET,
        FROGGER
    }

    public GameLeaderboard gameLeaderboard = GameLeaderboard.TETRIS;

    public static MainActivity getInstance() {
        return _instance;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(_instance == null){
            _instance = this;
        }

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().add(R.id.main_container, singleGameLeaderboard, "4").hide(singleGameLeaderboard).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.main_container, setting, "3").hide(setting).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.main_container, leaderboardFragment, "2").hide(leaderboardFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().add(R.id.main_container, gameListFragment, "1").commitAllowingStateLoss();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(active).show(gameListFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().hide(setting).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().hide(singleGameLeaderboard).commitAllowingStateLoss();
                    active = gameListFragment;
                    return  true;
                case R.id.navigation_leaderboard:
                    fragmentManager.beginTransaction().hide(active).show(leaderboardFragment).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().hide(setting).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().hide(singleGameLeaderboard).commitAllowingStateLoss();
                    active = leaderboardFragment;
                    return true;
            }
            return false;
        }
    };

    //Metodi per la toolbar superiore
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        User userTemp = UsersManager.getInstance().getCurrentUser();
        Log.i("USER_DEBUG", userTemp.toString());

        fragmentManager.beginTransaction().hide(active).show(setting).commitAllowingStateLoss();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsersManager.getInstance().saveCurrentUser();
    }


    public void gameTransaction(GameLeaderboard gameLeaderboard){
        this.gameLeaderboard = gameLeaderboard;

        singleGameLeaderboard.setRecyclerView(gameLeaderboard);

        fragmentManager.beginTransaction().hide(gameListFragment).commitAllowingStateLoss();
        fragmentManager.beginTransaction().show(singleGameLeaderboard).commitAllowingStateLoss();
    }

}
