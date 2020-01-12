package it.uniba.di.sms1920.giochiapp.Home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.di.sms1920.giochiapp.EndlessRun.MainActivityrun;
import it.uniba.di.sms1920.giochiapp.Game2048.MainActivity2048;
import it.uniba.di.sms1920.giochiapp.MainActivity;
import it.uniba.di.sms1920.giochiapp.NewHome.GameScoreboard;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.Tetris.Tetris;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    private List<Game> gameList;
    public GameAdapter(List<Game> gameList){
        this.gameList = gameList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Game game = gameList.get(position);

        holder.name.setText(game.getName());
        holder.highScore.setText("High Score : " + game.getHighScore());
        holder.image.setImageResource(game.getImage());

        switch (position) {
            //Tetris
            case 0:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, Tetris.class);
                        context.startActivity(intent);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, Tetris.class);
                        context.startActivity(intent);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MainActivity.getInstance().gameTransaction(MainActivity.GameLeaderboard.TETRIS);
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GameScoreboard.class);
                        intent.putExtra("game", 0);
                        context.startActivity(intent);

                    }
                });
                break;
                //2048
            case 1:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MainActivity2048.class);
                        context.startActivity(intent);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MainActivity2048.class);
                        context.startActivity(intent);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MainActivity.getInstance().gameTransaction(MainActivity.GameLeaderboard.GAME2048);
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GameScoreboard.class);
                        intent.putExtra("game", 1);
                        context.startActivity(intent);
                    }
                });
                break;
                //Endless
            case 2:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MainActivityrun.class);
                        context.startActivity(intent);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MainActivityrun.class);
                        context.startActivity(intent);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MainActivity.getInstance().gameTransaction(MainActivity.GameLeaderboard.ALIEN_RUN);
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GameScoreboard.class);
                        intent.putExtra("game", 2);
                        context.startActivity(intent);
                    }
                });
                break;
                //Elicottero
            case 3:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Helicopter.Game.class);
                        context.startActivity(intent);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Helicopter.Game.class);
                        context.startActivity(intent);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MainActivity.getInstance().gameTransaction(MainActivity.GameLeaderboard.ROCKET);
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GameScoreboard.class);
                        intent.putExtra("game", 3);
                        context.startActivity(intent);
                    }
                });
                break;
                //Frogger
            case 4:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Frogger.MainActivity.class);
                        context.startActivity(intent);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Frogger.MainActivity.class);
                        context.startActivity(intent);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //MainActivity.getInstance().gameTransaction(MainActivity.GameLeaderboard.FROGGER);
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GameScoreboard.class);
                        intent.putExtra("game", 4);
                        context.startActivity(intent);
                    }
                });
                break;

        }
    }


    @Override
    public int getItemCount() {
        return gameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView highScore;
        ImageView image;

        ImageButton button;
        ImageButton leaderboard;

        public MyViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.tvGameName);
            highScore = itemView.findViewById(R.id.tvHighScore);
            image = itemView.findViewById(R.id.imgViewGame);
            button = itemView.findViewById(R.id.btPlay);
            leaderboard = itemView.findViewById(R.id.imageButton);
        }
    }
}
