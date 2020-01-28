package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    private List<Game> gameList;
    GameAdapter(List<Game> gameList){
        this.gameList = gameList;
    }
    private int lastPosition = -1;



    /*Creazione del layout*/
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    /*Metodo per settare specifiche azioni a specifici elementi della recyclerView*/
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Game game = gameList.get(position);
        Context context = GlobalApplicationContext.getAppContext();

        holder.name.setText(game.getName());
        String score = context.getResources().getString(R.string.high_score_game) + " " + game.getHighScore();
        holder.highScore.setText(score);
        holder.image.setImageResource(game.getImage());

        if(position > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scoll_animation);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }


        switch (position) {
            //Tetris
            case 0:
               holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.TETRIS);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.TETRIS);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.showGameLeaderboard(v.getContext(), GameHelper.Games.TETRIS);
                    }
                });
                break;
            //2048
            case 1:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.GAME_2048);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.GAME_2048);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.showGameLeaderboard(v.getContext(), GameHelper.Games.GAME_2048);
                    }
                });
                break;
            //Endless
            case 2:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.ENDLESS);
                    }
                });
                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.ENDLESS);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.showGameLeaderboard(v.getContext(), GameHelper.Games.HELICOPTER);
                    }
                });
                break;
            //Elicottero
            case 3:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.HELICOPTER);
                    }
                });


                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.HELICOPTER);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.showGameLeaderboard(v.getContext(), GameHelper.Games.HELICOPTER);
                    }
                });
                break;
            //Frogger
            case 4:
                holder.button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.FROGGER);
                    }
                });

                holder.image.setOnClickListener(new ImageView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.playGame(v.getContext(), GameHelper.Games.FROGGER);
                    }
                });
                holder.leaderboard.setOnClickListener(new ImageButton.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GameHelper.showGameLeaderboard(v.getContext(), GameHelper.Games.FROGGER);
                    }
                });
                break;

        }
    }


    /*Metodo che ritorna il numero di elmenti contenuti nella recyclerView*/
    @Override
    public int getItemCount() {
        return gameList.size();
    }


    /*Iniziazlizzazione degli elementi contenuti nel layout*/
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView highScore;
        ImageView image;

        ImageButton button;
        ImageButton leaderboard;

        private MyViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.tvGameName);
            highScore = itemView.findViewById(R.id.tvHighScore);
            image = itemView.findViewById(R.id.imgViewGame);
            button = itemView.findViewById(R.id.btPlay);
            leaderboard = itemView.findViewById(R.id.imageButton);
        }
    }
}
