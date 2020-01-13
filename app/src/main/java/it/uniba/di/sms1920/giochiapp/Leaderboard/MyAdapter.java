package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import it.uniba.di.sms1920.giochiapp.NewHome.GlobalScoreboard;
import it.uniba.di.sms1920.giochiapp.R;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {

    GlobalScoreboard globalScoreboard = new GlobalScoreboard();
    boolean flag = true;

    LayoutInflater inflater;

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

        Log.i("posizione", String.valueOf(i) + titleParentViewHolder._textView.getText());

        if(title.isCurrentUser()){
            titleParentViewHolder._textView.setTextColor(Color.BLUE);

        }
/*
        if(globalScoreboard.getPosition() == i && flag){
            titleParentViewHolder._textView.setTextColor(Color.BLUE);
            flag = false;
        }

 */

    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        TitleChild title = (TitleChild)o;
        titleChildViewHolder.tetris.setText(title.getOption1());
        titleChildViewHolder.game2048.setText(title.getOption2());
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
