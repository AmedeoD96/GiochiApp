package it.uniba.di.sms1920.giochiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.Helicopter.Player;

public class MainActivity extends AppCompatActivity {
    private static final String HIGH_SCORE = "high score temp 2048";

    private RecyclerView rvGame;
    private List<Game> gameList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();

        //Crea la lista dei giochi
        createElement();

        //Imposto il layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvGame.setLayoutManager(layoutManager);

        //Imposto l'adapter
        GameAdapter gameAdapter = new GameAdapter(gameList);
        rvGame.setAdapter(gameAdapter);
    }

    private void initializeUI(){
        rvGame = findViewById(R.id.rvGame);
    }

    private void createElement(){
        Game tetris = new Game("Tetris", getTetrisHighScore(), R.drawable.tetris_launch_app);
        gameList.add(tetris);

        Game game2048 = new Game("2048", get2048HighScore(), R.drawable.game2048);
        gameList.add(game2048);

        //Aggiustare il punteggio di questi
        Game endless = new Game("Endless", Integer.parseInt(String.valueOf(47)), R.drawable.endless);
        gameList.add(endless);

        Game pinball = new Game("Pinball", Integer.parseInt(String.valueOf(3231)), R.drawable.pinball);
        gameList.add(pinball);

        Game flappy = new Game("Flappy", Integer.parseInt(String.valueOf(32)), R.drawable.flappy);
        gameList.add(flappy);
    }



    private int getTetrisHighScore(){
        SharedPreferences tetrisPref = getSharedPreferences("info", MODE_PRIVATE);
        int highScore = tetrisPref.getInt("TopScore", 0);
        return highScore;
    }

    private int get2048HighScore(){
        SharedPreferences game2048Pref = getSharedPreferences("info", MODE_PRIVATE);
        long highScore = game2048Pref.getLong(HIGH_SCORE, 0);
        String num = String.valueOf(highScore);
        int i = Integer.valueOf(num);
        return i;
    }


    void writeDB(){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    void readDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("DB", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DB", "Failed to read value.", error.toException());
            }
        });
    }
}
