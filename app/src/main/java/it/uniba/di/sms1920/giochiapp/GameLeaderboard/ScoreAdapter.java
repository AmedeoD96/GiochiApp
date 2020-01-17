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

/*Adapter della recycler view che è contenuta all'interno della scoreboard relativa al singolo gioco*/
public class ScoreAdapter extends ExpandableRecyclerAdapter<ViewHolderTitle, TitleChildViewHolder> {

    //TODO cambiare il colore green

    //componente atto a "gonfiare" una parte del layout
    private LayoutInflater inflater;

    public ScoreAdapter(Context context, List<ParentObject> parentItemList){
        super(context, parentItemList);
        //ottiene il layoutInflater dal contestp
        inflater = LayoutInflater.from(context);
    }

    //inserisce durante l'oncreate il layout che si trova nella classe ViewHolderTitle
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
        //assegna il testo della singola riga della leaderboard
        viewHolderTitle._userName.setText(parent.getUserName());
        viewHolderTitle._score.setText(parent.getScore());

        //si ottiene la posizione in classifica
        int position = parent.getPosition();

        //i 3 sul podio
        if(position == 0){
            viewHolderTitle.itemView.setBackgroundColor(Color.parseColor("#FFCA28"));
        }else if(position == 1){
            viewHolderTitle.itemView.setBackgroundColor(Color.parseColor("#B0BEC5"));
        }else if(position == 2){
            viewHolderTitle.itemView.setBackgroundColor(Color.parseColor("#CC6633"));
        }else {
            //le altre posizioni
            viewHolderTitle.itemView.setBackgroundColor(Color.WHITE);
        }

        //in caso di utente corrente si evidenzia il testo
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
