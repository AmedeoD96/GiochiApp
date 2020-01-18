package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import it.uniba.di.sms1920.giochiapp.R;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {

    //TODO Cambiare green e gli altri colori di sfondo. Il WHITE è il colore di default

    LayoutInflater inflater;
    RecyclerView recyclerView;

    public MyAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);

    }

    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_parent,viewGroup,false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child,viewGroup,false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent)o;
        titleParentViewHolder._textView.setText(title.getTitle());
        titleParentViewHolder._score.setText(title.getGlobalScore());

        if(title.isCurrentUser()){
            titleParentViewHolder._textView.setTextColor(Color.GREEN);
            titleParentViewHolder._score.setTextColor(Color.GREEN);

        }else {
            titleParentViewHolder._textView.setTextColor(Color.parseColor("#757575"));
            titleParentViewHolder._score.setTextColor(Color.parseColor("#757575"));
        }

        int position = title.getPosition();

        if(position == 0){
            titleParentViewHolder.itemView.setBackgroundColor(Color.parseColor("#FFCA28"));
        }else if(position == 1){
            titleParentViewHolder.itemView.setBackgroundColor(Color.parseColor("#B0BEC5"));
        }else if(position == 2){
            titleParentViewHolder.itemView.setBackgroundColor(Color.parseColor("#cc6633"));
        }else {
            titleParentViewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        TitleChild title = (TitleChild)o;
        titleChildViewHolder.tetris.setText(title.getTetris());
        titleChildViewHolder.game2048.setText(title.getGame2084());
        titleChildViewHolder.alienRun.setText(title.getAlienRun());
        titleChildViewHolder.rocket.setText(title.getRocket());
        titleChildViewHolder.frogger.setText(title.getFrogger());

        titleChildViewHolder.tetrisScore.setText(title.getScoreTetris());
        titleChildViewHolder.score2048.setText(title.getScore2048());
        titleChildViewHolder.scoreAlien.setText(title.getAlienRunScore());
        titleChildViewHolder.scoreRocket.setText(title.getRocketScore());
        titleChildViewHolder.scoreFrog.setText(title.getFroggerScore());
    }
}
