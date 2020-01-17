package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ElementCreator {
    private static ElementCreator _elementCreator;
    private List<Parent> _parent;

    private ElementCreator(Context context){
        _parent = new ArrayList<>();
    }

    public static ElementCreator get(Context context){
        //se l'elementCreator non fosse stato istanziato
        if(_elementCreator == null){
            //allora verrebbe istanziato
            _elementCreator = new ElementCreator(context);
        }
        return _elementCreator;
    }

    //rimuove tutti gli elementi dalla lista
    public void clearTitles(){
        _parent.clear();
    }

    //aggiunge un parent che non sia già contenuto
    public void addElement(Parent parent){
        if(!_parent.contains(parent)){
            _parent.add(parent);
        }
    }
}
