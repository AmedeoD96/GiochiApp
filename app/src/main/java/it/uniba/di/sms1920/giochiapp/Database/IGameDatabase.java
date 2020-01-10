package it.uniba.di.sms1920.giochiapp.Database;

import it.uniba.di.sms1920.giochiapp.User;


public interface IGameDatabase {

    String saveUser(String id, User user);

    void loadAllUsers(OnUserLoadedListener onUserLoadedListener);
    void loadUser(String userId, OnUserLoadedListener onUserLoadedListener);


    interface OnUserLoadedListener {
        void onUserLoaded(String id, User user);

        void onLoadCompleted();
    }
}

