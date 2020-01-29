package it.uniba.di.sms1920.giochiapp.Leaderboard;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    private static TitleCreator _titleCreator;
    private List<TitleParent> _titleParents;

    private TitleCreator() {
        _titleParents = new ArrayList<>();
    }

    //se non fosse stato creato verrebbe istanziato
    public static TitleCreator get() {
        if(_titleCreator == null) {
            _titleCreator = new TitleCreator();
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

