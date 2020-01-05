package it.uniba.di.sms1920.giochiapp.Database;

import it.uniba.di.sms1920.giochiapp.User;


public interface IGameDatabase {

    String saveUser(String id, User user);

    void loadUser(String id, OnUserLoadedListener onUserLoadedListener);
    void loadAllUsers(OnUserLoadedListener onUserLoadedListener);


    interface OnUserLoadedListener {
        void onUserLoaded(String id, User user);

        void onLoadCompleted();
    }
}

