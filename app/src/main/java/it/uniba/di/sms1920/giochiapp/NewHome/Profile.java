package it.uniba.di.sms1920.giochiapp.NewHome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeElement();

        final String welcomeInitialString = welcome.getText().toString();

        /*Dopo il caricamento di tutti gli utenti, vengono impostati i testi delle textview con i dati
         * dell'utente corrente*/
        UsersManager.getInstance().getAllUsers(new UsersManager.IUsersLoadedCallback() {
            @Override
            public void OnAllUsersLoaded(Map<String, User> users) {
                User user = UsersManager.getInstance().getCurrentUser();

              welcome.setText(welcomeInitialString + user.name);
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
                        welcome.setText("Welcome Back " + name.getText().toString());
        //todo perchè non posso mettere R.string.welcome al posto della stringa in sè per sè?
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
    }

    /*Salvo l'utente corrente quando l'activity viene chiusa*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsersManager.getInstance().saveCurrentUser();
    }
}
