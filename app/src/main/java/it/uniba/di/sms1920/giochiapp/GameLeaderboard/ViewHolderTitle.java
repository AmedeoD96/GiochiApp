package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import it.uniba.di.sms1920.giochiapp.R;

class ViewHolderTitle extends ParentViewHolder {
    TextView _userName;
    TextView _score;
    ImageView _trophyImage;

    ViewHolderTitle(View itemView) {
        //la view delle righe della scoreboard
        super(itemView);
        _userName = itemView.findViewById(R.id.userName);
        _score = itemView.findViewById(R.id.punteggio);
        _trophyImage = itemView.findViewById(R.id.trophyImg);

    }
}
