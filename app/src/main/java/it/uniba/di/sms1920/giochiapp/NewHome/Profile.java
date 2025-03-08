package it.uniba.di.sms1920.giochiapp.NewHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Map;

import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class Profile extends AppCompatActivity {
    private TextView welcome, tvTetris, tv2048, tvAlienRun, tvRocket, tvFrogger;
    private EditText name;
    private ToggleButton saveButton;
    private ImageView toTetris, to2048, toAlienRun, toRocket, toFrogger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeElement();
        listener();

        final String welcomeInitialString = welcome.getText().toString();

        /*Dopo il caricamento di tutti gli utenti, vengono impostati i testi delle textview con i dati
         * dell'utente corrente*/
        UsersManager.getInstance().getAllUsers(new UsersManager.IUsersLoadedCallback() {
            @Override
            public void OnAllUsersLoaded(Map<String, User> users) {
                User user = UsersManager.getInstance().getCurrentUser();
                String welcomeString = welcomeInitialString + "\n" + user.name;

              welcome.setText(welcomeString);
              name.setText(user.name);
              tvTetris.setText(String.valueOf(user.scoreTetris));
              tv2048.setText(String.valueOf(user.score2048));
              tvAlienRun.setText(String.valueOf(user.scoreAlienrun));
              tvRocket.setText(String.valueOf(user.scoreHelicopter));
              tvFrogger.setText(String.valueOf(user.scoreFrogger));
            }
        });


        /*Toggle button che permette di abilitare l'edit text e modificare l'username.
         * Dopo la modifica, cliccando nuovamente sul toggle button il nome viene salvato sul
         * database e l'edit text viene disabilitata.*/
        saveButton.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveButton.isChecked()){
                    name.setEnabled(true);
                }else{
                    name.setEnabled(false);

                    if(!name.getText().toString().equals("")) {
                        UsersManager.getInstance().getCurrentUser().setName(name.getText().toString());
                        String welcomeBackUser = getApplicationContext().getString(R.string.welcome) + "\n" + name.getText().toString();
                        welcome.setText(welcomeBackUser);
                    }
                }
            }
        });



        /*Inizializzazione della BottomNavigationView*/
        final BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setSelectedItemId(R.id.navigation_profile);

        /*Set azioni dei bottoni della BottomNavigationView*/
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_home:
                        Intent home = new Intent(getApplicationContext(), GameList.class);
                        startActivity(home);
                        finish();
                        break;
                    case R.id.navigation_leaderboard:
                        Intent globalScoreboard = new Intent(getApplicationContext(), GlobalScoreboard.class);
                        startActivity(globalScoreboard);
                        finish();
                        break;
                    case R.id.navigation_profile:
                        break;
                }
                return false;
            }
        });
    }

    /*Inizializzazione degli elementi del layout*/
    private void initializeElement(){
        welcome = findViewById(R.id.welcome);
        name = findViewById(R.id.etName);
        saveButton = findViewById(R.id.toggleButton);
        name.setEnabled(false);
        tvTetris = findViewById(R.id.tvScoreTetris);
        tv2048 = findViewById(R.id.TvScore2048);
        tvAlienRun = findViewById(R.id.tvScoreAlienRun);
        tvFrogger = findViewById(R.id.tvScoreFrogger);
        tvRocket = findViewById(R.id.tvScoreRocket);
        toTetris = findViewById(R.id.tetrisicon);
        to2048 = findViewById(R.id.icon2048);
        toAlienRun = findViewById(R.id.alienRunIcon);
        toRocket = findViewById(R.id.rocketIcon);
        toFrogger = findViewById(R.id.froggericon);
    }

    /*Set listener*/
    private void listener(){
        toTetris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameHelper.playGame(v.getContext(), GameHelper.Games.TETRIS);
            }
        });


        to2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameHelper.playGame(v.getContext(), GameHelper.Games.GAME_2048);
            }
        });


        toAlienRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameHelper.playGame(v.getContext(), GameHelper.Games.ENDLESS);
            }
        });


        toRocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameHelper.playGame(v.getContext(), GameHelper.Games.HELICOPTER);
            }
        });


        toFrogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameHelper.playGame(v.getContext(), GameHelper.Games.FROGGER);
            }
        });
    }

    /*Salvo l'utente corrente quando l'activity viene chiusa*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsersManager.getInstance().saveCurrentUser();
    }
}
