package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import it.uniba.di.sms1920.giochiapp.R;

public class ViewHolderTitle extends ParentViewHolder {
    public TextView _userName;
    public TextView _score;

    public ViewHolderTitle(View itemView) {
        super(itemView);
        _userName = itemView.findViewById(R.id.userName);
        _score = itemView.findViewById(R.id.punteggio);
    }
}
