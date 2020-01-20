package it.uniba.di.sms1920.giochiapp.Database;

import it.uniba.di.sms1920.giochiapp.User;

// interfaccia di un generico database necessario all'app
public interface IGameDatabase {

    // salva uno specifico utente nel database e ritorna l'id dell'utente
    String saveUser(String id, User user);

    // carica tutti gli utenti dal database
    void loadAllUsers(OnUserLoadedListener onUserLoadedListener);
    // carica uno specifico utente dal database
    void loadUser(String userId, OnUserLoadedListener onUserLoadedListener);

    //rimuove uno specifico utente dal databse
    void removeUser(String id);

    // inderfaccia di callback per le operazioni del database
    interface OnUserLoadedListener {
        void onUserLoaded(String id, User user);

        void onLoadCompleted();
    }
}

