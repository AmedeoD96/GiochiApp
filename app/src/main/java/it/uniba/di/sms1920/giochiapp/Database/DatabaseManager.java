package it.uniba.di.sms1920.giochiapp.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.Network.NetworkChangeReceiver;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;


public class DatabaseManager {


    private static final String LOCAL_PREF = "LocalPref";


    private static DatabaseManager ourInstance;

    private IGameDatabase localDatabase;
    private IGameDatabase remoteDatabase;

    private boolean useLocalVsRemote = true;



    private DatabaseManager() {
        localDatabase = new SQLiteWrapper();
        remoteDatabase = new FirebaseWrapper();
    }

    public static DatabaseManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DatabaseManager();
        }
        return ourInstance;
    }

    public void init() {
        NetworkChangeReceiver.getInstance().registerCallback(new NetworkChangeReceiver.INetworkCallback() {
            @Override
            public void OnNetworkChange(boolean isNetworkPresent) {
                boolean precValue = useLocalVsRemote;
                useLocalVsRemote = isNetworkPresent;

                if(!precValue && useLocalVsRemote) {
                    OnGoingOffline();
                } else if(precValue && !useLocalVsRemote) {
                    OnGoingOnline();
                }
            }
        });
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


    public void loadAllUsers(final IGameDatabase.OnUserLoadedListener userLoadedListener) {
        if(useLocalVsRemote) {
            localDatabase.loadAllUsers(userLoadedListener);
        } else {
            remoteDatabase.loadAllUsers(userLoadedListener);
        }
    }

    public void loadUser(final String userId, final IGameDatabase.OnUserLoadedListener userLoadedListener) {
        Log.i("DATABASE_DEBUG", "load user: " + userId);

        localDatabase.loadUser(userId, new IGameDatabase.OnUserLoadedListener() {
            boolean hasLoadLocally;

            @Override
            public void onUserLoaded(String id, final User userLocal) {
                hasLoadLocally = true;

                Log.i("DATABASE_DEBUG", "using Remote: "+!useLocalVsRemote+" User local loaded: " + userLocal.toString());

                if(!useLocalVsRemote) {
                    remoteDatabase.loadUser(id, new IGameDatabase.OnUserLoadedListener() {
                        @Override
                        public void onUserLoaded(String id, User user) {
                            Log.i("DATABASE_DEBUG", "User local more updated of remote: " +userLocal.isMoreUpdatedThan(user)+ "\nLocal user: "+ userLocal.toString() + "\nRemote user" + user.toString());
                            if(userLocal.isMoreUpdatedThan(user)) {
                                userLoadedListener.onUserLoaded(id, userLocal);
                            } else {
                                userLoadedListener.onUserLoaded(id, user);
                            }
                        }

                        @Override
                        public void onLoadCompleted() {
                            Log.i("DATABASE_DEBUG", "user remote completed");

                            userLoadedListener.onLoadCompleted();
                        }
                    });
                }
            }

            @Override
            public void onLoadCompleted() {
                // If not has load the local user and is set to use the local user
                Log.i("DATABASE_DEBUG", "user local completed need continue: " + (hasLoadLocally && useLocalVsRemote) + " Use local vs remote: " + useLocalVsRemote);

                if(hasLoadLocally && useLocalVsRemote) {

                    userLoadedListener.onLoadCompleted();
                } else if(!hasLoadLocally) {

                    remoteDatabase.loadUser(userId, new IGameDatabase.OnUserLoadedListener() {
                        @Override
                        public void onUserLoaded(String id, User user) {
                            Log.i("DATABASE_DEBUG", "Remote user" + user.toString());
                            userLoadedListener.onUserLoaded(id, user);
                        }

                        @Override
                        public void onLoadCompleted() {
                            Log.i("DATABASE_DEBUG", "user remote completed");

                            userLoadedListener.onLoadCompleted();
                        }
                    });
                }
            }
        });
    }



    void OnGoingOffline() {

    }

    void OnGoingOnline() {

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

        String result = pref.getString(key, defalultValue);
        return result;
    }





    public void saveUsersIntoLocalDB(Map<String, User> users) {
        Log.i("DATABASE_DEBUG", "Cloning db");

        for (Map.Entry<String, User> user : users.entrySet()) {
            localDatabase.saveUser(user.getKey(), user.getValue());
        }
    }



}
