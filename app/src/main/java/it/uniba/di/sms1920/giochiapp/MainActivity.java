package it.uniba.di.sms1920.giochiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        Game game = new Game("Tetris", Integer.parseInt(String.valueOf(10)), R.drawable.tetris_launch_app);
        gameList.add(game);

        game = new Game("2048", Integer.parseInt(String.valueOf(4096)), R.drawable.game2048);
        gameList.add(game);

        game = new Game("Endless", Integer.parseInt(String.valueOf(47)), R.drawable.endless);
        gameList.add(game);

        game = new Game("Pinball", Integer.parseInt(String.valueOf(3231)), R.drawable.pinball);
        gameList.add(game);

        game = new Game("Flappy", Integer.parseInt(String.valueOf(31)), R.drawable.flappy);
        gameList.add(game);
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
