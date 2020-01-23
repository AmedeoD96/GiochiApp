package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {

    //TODO Cambiare green e gli altri colori di sfondo. Il WHITE è il colore di default

    LayoutInflater inflater;
    Context context = GlobalApplicationContext.getAppContext();
    private int lastPosition = 10;

    public MyAdapter(Context context, List<ParentObject> parentItemList) {
        super(context, parentItemList);
        //ottenimento dell'inflater
        inflater = LayoutInflater.from(context);

    }


    @Override
    public TitleParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        //Attua un inflate su una nuova view gerarchica da una risorsa xml specifica
        View view = inflater.inflate(R.layout.list_parent,viewGroup,false);
        return new TitleParentViewHolder(view);
    }

    @Override
    public TitleChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.list_child,viewGroup,false);
        return new TitleChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(final TitleParentViewHolder titleParentViewHolder, int i, Object o) {
        TitleParent title = (TitleParent)o;
        //assegna il testo della singola riga della leaderboard e l'immagine della freccia verso il basso
        titleParentViewHolder._textView.setText(title.getTitle());
        titleParentViewHolder._score.setText(title.getGlobalScore());
        titleParentViewHolder._image.setImageResource(R.drawable.ic_keyboard_arrow_down_float);
        titleParentViewHolder._trophyImage.setImageResource(R.drawable.trophy);
        titleParentViewHolder.itemView.setBackgroundColor(Color.parseColor("#4CAF50"));

        //in caso di utente corrente ci sarebbe il cambiamento del colore nel testo
        if(title.isCurrentUser()){
            titleParentViewHolder._textView.setTextColor(Color.parseColor("#081208"));
            titleParentViewHolder._score.setTextColor(Color.parseColor("#081208"));
        }else {
            //titleParentViewHolder._textView.setTextColor(Color.parseColor("#80e27e"));
            //titleParentViewHolder._score.setTextColor(Color.parseColor("#80e27e"));
            titleParentViewHolder._textView.setTextColor(Color.WHITE);
            titleParentViewHolder._score.setTextColor(Color.WHITE);
        }

        int position = title.getPosition();

        //le prime 3 posizioni
        if(position == 0){
            //titleParentViewHolder.itemView.setBackgroundColor(Color.parseColor("#C6A530"));
            titleParentViewHolder._trophyImage.setVisibility(View.VISIBLE);
            titleParentViewHolder._trophyImage.setColorFilter(Color.parseColor("#FFD700"));
        }else if(position == 1){
            //titleParentViewHolder.itemView.setBackgroundColor(Color.parseColor("#788287"));
            titleParentViewHolder._trophyImage.setVisibility(View.VISIBLE);
            titleParentViewHolder._trophyImage.setColorFilter(Color.parseColor("#788287"));
        }else if(position == 2){
            //titleParentViewHolder.itemView.setBackgroundColor(Color.parseColor("#794909"));
            titleParentViewHolder._trophyImage.setVisibility(View.VISIBLE);
            titleParentViewHolder._trophyImage.setColorFilter(Color.parseColor("#794909"));
        }else {
            titleParentViewHolder._trophyImage.setVisibility(View.GONE);
        }

        if (i > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scoll_animation);
            titleParentViewHolder.itemView.startAnimation(animation);
            lastPosition = i;
        }


    }

    @Override
    public void onBindChildViewHolder(final TitleChildViewHolder titleChildViewHolder, int i, Object o) {
        //assegnazione del testo nelle viewholder child
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
