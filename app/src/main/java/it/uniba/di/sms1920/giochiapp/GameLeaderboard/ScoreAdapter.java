package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import it.uniba.di.sms1920.giochiapp.Leaderboard.TitleChildViewHolder;
import it.uniba.di.sms1920.giochiapp.R;

public class ScoreAdapter extends ExpandableRecyclerAdapter<ViewHolderTitle, TitleChildViewHolder> {

    //TODO cambiare il colore green

    LayoutInflater inflater;

    public ScoreAdapter(Context context, List<ParentObject> parentItemList){
        super(context, parentItemList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderTitle onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.parent, viewGroup, false);
        return new ViewHolderTitle(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindParentViewHolder(ViewHolderTitle viewHolderTitle, int i, Object o) {
        Parent parent = (Parent)o;
        viewHolderTitle._userName.setText(parent.getUserName());
        viewHolderTitle._score.setText(parent.getScore());

        int position = parent.getPosition();

        if(position == 0){
            viewHolderTitle.itemView.setBackgroundColor(Color.parseColor("#FFCA28"));
        }else if(position == 1){
            viewHolderTitle.itemView.setBackgroundColor(Color.parseColor("#B0BEC5"));
        }else if(position == 2){
            viewHolderTitle.itemView.setBackgroundColor(Color.parseColor("#CC6633"));
        }else {
            viewHolderTitle.itemView.setBackgroundColor(Color.WHITE);
        }

        if(parent.isCurrentUser()){
            viewHolderTitle._userName.setTextColor(Color.GREEN);
            viewHolderTitle._score.setTextColor(Color.GREEN);
        }else {
            viewHolderTitle._userName.setTextColor(Color.parseColor("#737373"));
            viewHolderTitle._score.setTextColor(Color.parseColor("#737373"));
        }
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {

    }


}
