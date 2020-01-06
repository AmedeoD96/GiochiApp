package it.uniba.di.sms1920.giochiapp.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;


public class DatabaseManager {


    private static final String LOCAL_PREF = "LocalPref";


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



    public void saveUser(String id, User user) {
        if(useLocalVsRemote) {
            localDatabase.saveUser(id, user);
        } else {
            String realID = remoteDatabase.saveUser(id, user);
            UsersManager.getInstance().setIdCurrentUser(realID);
            localDatabase.saveUser(realID, user);
        }
    }


    public void loadAllUsers(IGameDatabase.OnUserLoadedListener userLoadedListener) {
        Log.i("DatabaseDEBUG", "localVsRemote: " + useLocalVsRemote);
        if(useLocalVsRemote) {
            localDatabase.loadAllUsers(userLoadedListener);
        } else {
            remoteDatabase.loadAllUsers(userLoadedListener);
        }
    }


    public void setUseLocalVsRemote(boolean useLocalVsRemote) {
        Log.i("DatabaseDEBUG", "use local: " + useLocalVsRemote);
        UsersManager.getInstance().populateIfEmpty();

        boolean precValue = this.useLocalVsRemote;
        this.useLocalVsRemote = useLocalVsRemote;

        if(!precValue && useLocalVsRemote) {
            cloneRemoteDBIntoLocalDB();
        } else if(precValue && !useLocalVsRemote) {

        }

    }


    public void saveString(String key, String value) {
        Context context = GlobalApplicationContext.getAppContext();

        SharedPreferences pref = context.getSharedPreferences(LOCAL_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.apply();
    }

    public String loadString(String key, String defalultValue) {
        Context context = GlobalApplicationContext.getAppContext();

        SharedPreferences pref = context.getSharedPreferences(LOCAL_PREF, Context.MODE_PRIVATE);
        return pref.getString(key, defalultValue);
    }





    private void cloneRemoteDBIntoLocalDB() {
        Map<String, User> users = UsersManager.getInstance().getAllUsers();

        for (Map.Entry<String, User> user : users.entrySet()) {
            localDatabase.saveUser(user.getKey(), user.getValue());
        }
    }



}
