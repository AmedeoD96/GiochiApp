package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.view.View;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import it.uniba.di.sms1920.giochiapp.R;

public class TitleParentViewHolder extends ParentViewHolder {
    public TextView _textView;
    public TextView _score;

    public TitleParentViewHolder(View itemView) {
        super(itemView);
        _textView = itemView.findViewById(R.id.parentTitle);
        _score = itemView.findViewById(R.id.score1);
    }
}

