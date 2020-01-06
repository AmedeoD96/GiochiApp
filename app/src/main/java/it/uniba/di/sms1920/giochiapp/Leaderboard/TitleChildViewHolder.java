package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import it.uniba.di.sms1920.giochiapp.R;

public class TitleChildViewHolder extends ChildViewHolder {
    public TextView tetris, game2048, alienRun, rocket, frogger;

    public TitleChildViewHolder(View itemView) {
        super(itemView);
        tetris = itemView.findViewById(R.id.tetris);
        game2048 = itemView.findViewById(R.id.game2048);
        alienRun = itemView.findViewById(R.id.alienRun);
        rocket = itemView.findViewById(R.id.rocket);
        frogger = itemView.findViewById(R.id.frogger);
    }
}
