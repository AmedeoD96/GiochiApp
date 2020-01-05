package it.uniba.di.sms1920.giochiapp.Database;

import it.uniba.di.sms1920.giochiapp.User;

public class SQLliteWrapper implements IGameDatabase {

    @Override
    public String saveUser(String id, User user) {
        return "";
    }

    @Override
    public void loadUser(String id, OnUserLoadedListener onUserLoadedListener) {

    }

    @Override
    public void loadAllUsers(OnUserLoadedListener onUserLoadedListener) {

    }

}
