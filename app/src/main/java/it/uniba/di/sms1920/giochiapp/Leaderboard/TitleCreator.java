package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    private static TitleCreator _titleCreator;
    private List<TitleParent> _titleParents;

    private TitleCreator(Context context) {
        _titleParents = new ArrayList<>();
    }

    //se non fosse stato creato verrebbe istanziato
    public static TitleCreator get(Context context) {
        if(_titleCreator == null) {
            _titleCreator = new TitleCreator(context);
        }
        return _titleCreator;
    }

    //rimuove tutti gli elementi dalla lista
    public void clearTitles() {
        _titleParents.clear();
    }

    //aggiunge un TitleParent che non sia già contenuto
    public void addTitle(TitleParent titleParent) {
        if(!_titleParents.contains(titleParent)) {
            _titleParents.add(titleParent);
        }
    }

}

