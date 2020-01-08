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
import it.uniba.di.sms1920.giochiapp.GameLeaderboard.GameLeaderboard;
import it.uniba.di.sms1920.giochiapp.Home.GameFragment;
import it.uniba.di.sms1920.giochiapp.Leaderboard.LeaderboardFragment;
import it.uniba.di.sms1920.giochiapp.Setting.Setting;

public class MainActivity extends AppCompatActivity {
    final Fragment fragment1 = new GameFragment();
    final Fragment fragment2 = new LeaderboardFragment();
    final Fragment setting = new Setting();
    final Fragment gameLeaderboard = new GameLeaderboard();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = fragment1;

    static MainActivity _instance = null;

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

        fragmentManager.beginTransaction().add(R.id.main_container, gameLeaderboard, "4").hide(gameLeaderboard).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, setting, "3").hide(setting).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, fragment2, "2").hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, fragment1, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    fragmentManager.beginTransaction().hide(setting).commit();
                    fragmentManager.beginTransaction().hide(gameLeaderboard).commit();
                    active = fragment1;
                    return  true;
                case R.id.navigation_leaderboard:
                    fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                    fragmentManager.beginTransaction().hide(setting).commit();
                    fragmentManager.beginTransaction().hide(gameLeaderboard).commit();
                    active = fragment2;
                    return true;
            }
            return false;
        }
    };

    //Metodi per la toolbar superiore
    //TODO mettere il tasto indietro nella toolbar
    //TODO cambiare i tre puntini e mettere l'icona dei setting
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
    public void transaction(){
        fragmentManager.beginTransaction().hide(fragment1).commit();
        fragmentManager.beginTransaction().show(gameLeaderboard).commit();
    }
}
