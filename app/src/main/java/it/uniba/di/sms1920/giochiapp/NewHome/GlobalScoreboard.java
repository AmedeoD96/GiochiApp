package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

        /*Set toolbar*/
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        initializeElement();

        /*Cliccando il bottone di ricerca, la recyclerView scolla in automatico alla posizione
        * in cui c'è l'utente corrente*/
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(find);
            }
        });

        /*Dopo il caricamento di tutti gli utenti, viene creata la classifica.*/
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


        /*Inizializzazione della BottomNavigationView*/
        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_leaderboard);

        /*Azioni che ogni bottone della BottomNavigationVIew deve compiere*/
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
                    case R.id.navigation_profile:
                        Intent profile = new Intent(getApplicationContext(), Profile.class);
                        startActivity(profile);
                        finish();

                }
                return false;
            }
        });
    }

    /*Inizializzazione degli elementi del layout*/
    private void initializeElement(){
        recyclerView = findViewById(R.id.rvGlobalScoreboard);
        recyclerView.setLayoutManager(new CenterLayoutManager(this));
        button = findViewById(R.id.findButton);
    }


    /*Metodo che ritorna la lista degli elementi che compongono la scoreboard*/
    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<ParentObject> parentObject = new ArrayList<>();
        titleCreator.clearTitles();

        /*Get di tutti gli utenti ordinati per punteggio (dal maggiore al minore)*/
        Collection<User> allUser = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.TOTAL_SCORE, false);

        int count = 1; /*Variabile usata per mostrare la posizione in classifica*/
        User currentUser = UsersManager.getInstance().getCurrentUser();/*Caricamento utente corrente*/
        int position = 0;
        for (User user : allUser) {

            boolean isCurrentUser = false;

            /*Cerco l'utente corrente*/
            if(user.equals(currentUser)){
                isCurrentUser = true;
                find = position;
            }

            /*Set del titolo dell'elemento della leaderboard. Contiene la posizione in classifica, il nome utente, il suo punteggio
            * una variabile boolean che viene usata per evidenziare il testo solo se si tratta dell'utente corrente e l'indice
            * dell'elemento della recyclerView che viene usato per lo scorll automatico della recyclerView quando si clicca sul bottone*/
            TitleParent title = new TitleParent(count + "°   "  + user.name, user.getTotalScore(), isCurrentUser, position);

            position++;
            count++;

            /*Aggiunta degli score del singolo gioco*/
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

    /*Aggiornamento dei dati quando si ritorna all'activity*/
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

    /*Salvataggio dell'utente quando l'activity va in onDestroy*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UsersManager.getInstance().saveCurrentUser();
    }

    /*Metodi per la toolbar superiore*/
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
