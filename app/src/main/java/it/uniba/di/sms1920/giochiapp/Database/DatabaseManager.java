package it.uniba.di.sms1920.giochiapp.Database;

import android.content.Context;

import it.uniba.di.sms1920.giochiapp.User;


public class DatabaseManager {

    public static String BASIC_ID = "-";
    private static DatabaseManager ourInstance;

    private IGameDatabase localDatabase;
    private IGameDatabase remoteDatabase;

    private boolean useLocalVsRemote = true;

    private DatabaseManager() {
        localDatabase = new SQLliteWrapper();
        remoteDatabase = new FirebaseWrapper();
    }

    public static DatabaseManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DatabaseManager();
        }
        return ourInstance;
    }


    public void saveUser(Context context, String id, User user) {
        if(useLocalVsRemote) {
            localDatabase.saveUser(id, user);
        } else {
            localDatabase.saveUser(id, user);
            remoteDatabase.saveUser(id, user);
        }
    }

    public void loadUser(Context context, String id, IGameDatabase.OnUserLoadedListener userLoadedListener) {
        if(useLocalVsRemote) {
            localDatabase.loadUser(id, userLoadedListener);
        } else {
            remoteDatabase.loadUser(id, userLoadedListener);
        }
    }


    public void loadAllUsers(Context context, IGameDatabase.OnUserLoadedListener userLoadedListener) {
        if(useLocalVsRemote) {
            localDatabase.loadAllUsers(userLoadedListener);
        } else {
            remoteDatabase.loadAllUsers(userLoadedListener);
        }
    }


    public void setUseLocalVsRemote(boolean useLocalVsRemote) {
        this.useLocalVsRemote = useLocalVsRemote;
    }
}
