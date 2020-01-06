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

import it.uniba.di.sms1920.giochiapp.Home.GameFragment;
import it.uniba.di.sms1920.giochiapp.Leaderboard.LeaderboardFragment;


public class MainActivity extends AppCompatActivity {

    final Fragment fragment1 = new GameFragment();
    final Fragment fragment2 = new LeaderboardFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment active = fragment1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
                    active = fragment1;
                    return  true;
                case R.id.navigation_leaderboard:
                    fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        User userTemp = UsersManager.getInstance().getCurrentUser();
        Log.i("USER_DEBUG", userTemp.toString());

        return super.onOptionsItemSelected(item);
    }
}
