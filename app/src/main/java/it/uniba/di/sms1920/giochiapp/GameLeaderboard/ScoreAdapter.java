package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.Leaderboard.TitleChildViewHolder;
import it.uniba.di.sms1920.giochiapp.R;

/*Adapter della recycler view che è contenuta all'interno della scoreboard relativa al singolo gioco*/
public class ScoreAdapter extends ExpandableRecyclerAdapter<ViewHolderTitle, TitleChildViewHolder> {

    //componente atto a "gonfiare" una parte del layout
    private LayoutInflater inflater;
    private Context context = GlobalApplicationContext.getAppContext();

    private int lastPosition = -1; //Usata per l'animazione

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
        viewHolderTitle._trophyImage.setImageResource(R.drawable.trophy);
        viewHolderTitle.itemView.setBackgroundColor(Color.WHITE);

        if (i > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scoll_animation);
            viewHolderTitle.itemView.startAnimation(animation);
            lastPosition = i;
        }



        //si ottiene la posizione in classifica
        int position = parent.getPosition();

        //i 3 sul podio
        if(position == 0){
            viewHolderTitle._trophyImage.setVisibility(View.VISIBLE);
            viewHolderTitle._trophyImage.setColorFilter(context.getResources().getColor(R.color.trophyGold));
        }else if(position == 1){
            viewHolderTitle._trophyImage.setVisibility(View.VISIBLE);
            viewHolderTitle._trophyImage.setColorFilter(context.getResources().getColor(R.color.trophySilver));
        }else if(position == 2){
            viewHolderTitle._trophyImage.setVisibility(View.VISIBLE);
            viewHolderTitle._trophyImage.setColorFilter(context.getResources().getColor(R.color.trophyBronze));
        }else {
            //le altre posizioni
            viewHolderTitle._trophyImage.setVisibility(View.GONE);
        }

        //in caso di utente corrente si evidenzia il testo
        if(parent.isCurrentUser()){
            viewHolderTitle._userName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            viewHolderTitle._score.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else {
            viewHolderTitle._userName.setTextColor(context.getResources().getColor(R.color.textView));
            viewHolderTitle._score.setTextColor(context.getResources().getColor(R.color.textView));
        }
    }

    @Override
    public void onBindChildViewHolder(TitleChildViewHolder titleChildViewHolder, int i, Object o) {

    }


}
