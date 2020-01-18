package it.uniba.di.sms1920.giochiapp.Leaderboard;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import it.uniba.di.sms1920.giochiapp.R;

class TitleParentViewHolder extends ParentViewHolder {
    //text view utente nella leaderboard
    TextView _textView;
    TextView _score;
    ImageView _image;

    TitleParentViewHolder(View itemView) {
        super(itemView);
        _textView = itemView.findViewById(R.id.parentTitle);
        _score = itemView.findViewById(R.id.score1);
        _image = itemView.findViewById(R.id.down_arrow);
    }


}

