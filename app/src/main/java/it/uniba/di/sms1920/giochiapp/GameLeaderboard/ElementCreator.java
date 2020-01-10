package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ElementCreator {
    static ElementCreator _elementCreator;
    List<Parent> _parent;

    public ElementCreator(Context context){
        _parent = new ArrayList<>();
    }

    public static ElementCreator get(Context context){
        if(_elementCreator == null){
            _elementCreator = new ElementCreator(context);
        }
        return _elementCreator;
    }

    public void clearTitles(){
        _parent.clear();
    }

    public void addElement(Parent parent){
        if(!_parent.contains(parent)){
            _parent.add(parent);
        }
    }
}
