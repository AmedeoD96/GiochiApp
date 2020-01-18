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

import org.w3c.dom.Text;

import java.util.Map;

import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class SetUserName extends AppCompatActivity {
    private TextView welcome;
    private EditText name;
    private ToggleButton saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_name);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        initializeElement();


        final String welcomeInitialString = welcome.getText().toString();


        UsersManager.getInstance().getAllUsers(new UsersManager.IUsersLoadedCallback() {
            @Override
            public void OnAllUsersLoaded(Map<String, User> users) {
                User user = UsersManager.getInstance().getCurrentUser();

                welcome.setText(welcomeInitialString + user.name);
                name.setText(user.name);
            }
        });


        saveButton.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saveButton.isChecked()){
                    name.setEnabled(true);
                }else{
                    name.setEnabled(false);

                    if(!name.getText().toString().equals("")) {
                        UsersManager.getInstance().getCurrentUser().setName(name.getText().toString());
                        welcome.setText("Welcome Back: " + name.getText().toString());
                    }
                }
            }
        });

        final BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.getMenu().getItem(0).setCheckable(false);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
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
                    case R.id.navigation_profile:
                        Intent profile = new Intent(getApplicationContext(), Profile.class);
                        startActivity(profile);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    private void initializeElement(){
        welcome = findViewById(R.id.welcome);
        name = findViewById(R.id.etName);
        saveButton = findViewById(R.id.toggleButton);
        name.setEnabled(false);
    }

    //Metodi per la toolbar superiore
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //In questa classe il bottone non fa nulla.
        return super.onOptionsItemSelected(item);
    }
}
