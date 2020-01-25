package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;

public class MyAdapter extends ExpandableRecyclerAdapter<TitleParentViewHolder,TitleChildViewHolder> {

    private final float STRAIGHT_ROT = 0;
    private final float FLIP_ROT = 180;

    private LayoutInflater inflater;
    private Context context = GlobalApplicationContext.getAppContext();
    private int lastPosition = 10;

    // Contenitore delle singole righe della global leaderboard
    private Map<Integer, TitleParentViewHolder> usersTitles = new HashMap<>();

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
        //Set attributi degli elementi della riga
        titleParentViewHolder._textView.setText(title.getTitle());
        titleParentViewHolder._score.setText(title.getGlobalScore());
        titleParentViewHolder._image.setImageResource(R.drawable.ic_keyboard_arrow_down_float);
        titleParentViewHolder._trophyImage.setImageResource(R.drawable.trophy);
        titleParentViewHolder.itemView.setBackgroundColor(Color.WHITE);

        //in caso di utente corrente ci sarebbe il cambiamento del colore nel testo
        if(title.isCurrentUser()){
            titleParentViewHolder._textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            titleParentViewHolder._score.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }else {
            titleParentViewHolder._textView.setTextColor(context.getResources().getColor(R.color.textView));
            titleParentViewHolder._score.setTextColor(context.getResources().getColor(R.color.textView));
        }

        int position = title.getPosition();

        //Set icona trofeo solo ai primi tre elementi della recyclerView con i rispettivi colori
        if(position == 0){
            titleParentViewHolder._trophyImage.setVisibility(View.VISIBLE);
            titleParentViewHolder._trophyImage.setColorFilter(context.getResources().getColor(R.color.trophyGold));
        }else if(position == 1){
            titleParentViewHolder._trophyImage.setVisibility(View.VISIBLE);
            titleParentViewHolder._trophyImage.setColorFilter(context.getResources().getColor(R.color.trophySilver));
        }else if(position == 2){
            titleParentViewHolder._trophyImage.setVisibility(View.VISIBLE);
            titleParentViewHolder._trophyImage.setColorFilter(context.getResources().getColor(R.color.trophyBronze));
        }else {
            //Nasconde l'icona del trofeo
            titleParentViewHolder._trophyImage.setVisibility(View.GONE);
        }

        if (i > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scoll_animation);
            titleParentViewHolder.itemView.startAnimation(animation);
            lastPosition = i;
        }

        // riempimento delle righe degli utenti
        usersTitles.put(i, titleParentViewHolder);
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


    // metodo chiamato quando viene premuta una riga
    @Override
    public void onParentItemClickListener(int position) {
        super.onParentItemClickListener(position);

        TitleParentViewHolder title = usersTitles.get(position);
        if(title != null) {

            // ruota l'immagine se non ruotata, altrimenti la ruota
            if(title._image.getRotation() == STRAIGHT_ROT) {

                title._image.setRotation(FLIP_ROT);
            } else {

                title._image.setRotation(STRAIGHT_ROT);
            }
        }
    }
}
