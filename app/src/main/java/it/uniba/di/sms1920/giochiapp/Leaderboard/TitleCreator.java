package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class TitleCreator {
    static TitleCreator _titleCreator;
    List<TitleParent> _titleParents;
    Map<String, User> users= UsersManager.getInstance().getAllUsers();

    public TitleCreator(Context context) {
        _titleParents = new ArrayList<>();

        for(Map.Entry<String, User> entry : users.entrySet()){
            TitleParent title = new TitleParent(entry.getValue().name, entry.getValue().getTotalScore());
            _titleParents.add(title);
        }
    }

    public static TitleCreator get(Context context) {
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context);
        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }
}

