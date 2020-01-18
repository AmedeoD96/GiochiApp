package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import it.uniba.di.sms1920.giochiapp.R;

public class TitleChildViewHolder extends ChildViewHolder {
    TextView tetris, game2048, alienRun, rocket, frogger, tetrisScore, score2048, scoreAlien, scoreRocket, scoreFrog;

    TitleChildViewHolder(View itemView) {
        super(itemView);
        tetris = itemView.findViewById(R.id.scoreGame);
        game2048 = itemView.findViewById(R.id.game2048);
        alienRun = itemView.findViewById(R.id.alienRun);
        rocket = itemView.findViewById(R.id.rocket);
        frogger = itemView.findViewById(R.id.frogger);

        tetrisScore = itemView.findViewById(R.id.score1);
        score2048 = itemView.findViewById(R.id.score2048);
        scoreAlien = itemView.findViewById(R.id.alienRunScore);
        scoreRocket = itemView.findViewById(R.id.rocketScore);
        scoreFrog = itemView.findViewById(R.id.froggerScore);

    }
}
