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
import it.uniba.di.sms1920.giochiapp.GameLeaderboard.TetrisLeaderboard;
import it.uniba.di.sms1920.giochiapp.Home.GameFragment;
import it.uniba.di.sms1920.giochiapp.Leaderboard.LeaderboardFragment;
import it.uniba.di.sms1920.giochiapp.Setting.Setting;

public class MainActivity extends AppCompatActivity {
    //TODO PER LUCA: NON SALVA IL NOME E I PUNTEGGI SU FIREBASE SE MODIFICATI IN MODALITA OFFLINE
    final Fragment gameListFragment = new GameFragment();
    final Fragment leaderboardFragment = new LeaderboardFragment();
    final Fragment setting = new Setting();
    final Fragment tetrisLeaderboard = new TetrisLeaderboard();
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

        fragmentManager.beginTransaction().add(R.id.main_container, tetrisLeaderboard, "4").hide(tetrisLeaderboard).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, setting, "3").hide(setting).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, leaderboardFragment, "2").hide(leaderboardFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, gameListFragment, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(active).show(gameListFragment).commit();
                    fragmentManager.beginTransaction().hide(setting).commit();
                    fragmentManager.beginTransaction().hide(tetrisLeaderboard).commit();
                    active = gameListFragment;
                    return  true;
                case R.id.navigation_leaderboard:
                    fragmentManager.beginTransaction().hide(active).show(leaderboardFragment).commit();
                    fragmentManager.beginTransaction().hide(setting).commit();
                    fragmentManager.beginTransaction().hide(tetrisLeaderboard).commit();
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

        fragmentManager.beginTransaction().hide(active).show(setting).commit();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        UsersManager.getInstance().saveCurrentUser();
    }
    public void gameTransaction(GameLeaderboard gameLeaderboard){
        fragmentManager.beginTransaction().hide(gameListFragment).commit();
        fragmentManager.beginTransaction().show(tetrisLeaderboard).commit();
    }
}
