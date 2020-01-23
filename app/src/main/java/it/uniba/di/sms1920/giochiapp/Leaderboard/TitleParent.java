package it.uniba.di.sms1920.giochiapp.Leaderboard;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class TitleParent implements ParentObject {

    private List<Object> mChildrenList;
    private String title;
    private int globalScore;
    private boolean isCurrentUser;
    private int position;
    private int image;
    private int trophyImage;


    public TitleParent(String title, int globalScore, boolean isCurrentUser, int position, int image, int trophyImage) {
        this.title = title;
        this.globalScore = globalScore;
        this.isCurrentUser = isCurrentUser;
        this.position = position;
        this.image = image;
        this.trophyImage = trophyImage;
    }

    public int getImage() {
        return image;
    }

    String getGlobalScore() {
        return String.valueOf(globalScore);
    }

    String getTitle() {
        return title;
    }

    boolean isCurrentUser() {
        return isCurrentUser;
    }

    int getPosition() {
        return position;
    }

    //ritorna la lista dei punteggi dei singoli giochi di un singolo utente
    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    //assegna i valori alla lista dei punteggio dei singoli giochi di un singolo utente
    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }
}

