package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;

    public TitleCreator(Context context) {
        _titleParents = new ArrayList<>();
    }

    public static TitleCreator get(Context context) {
        if(_titleCreator == null) {
            _titleCreator = new TitleCreator(context);
        }
        return _titleCreator;
    }

    public void clearTitles() {
        _titleParents.clear();
    }

    public void addTitle(TitleParent titleParent) {
        if(!_titleParents.contains(titleParent)) {
            _titleParents.add(titleParent);
        }
    }

}

