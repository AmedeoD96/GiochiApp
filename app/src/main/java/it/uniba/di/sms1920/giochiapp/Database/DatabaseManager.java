package it.uniba.di.sms1920.giochiapp.Database;

import android.content.Context;
import android.content.SharedPreferences;

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
        localDatabase = new SQLiteWrapper();
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


    public void loadAllUsers(final IGameDatabase.OnUserLoadedListener userLoadedListener) {
        if(useLocalVsRemote) {
            localDatabase.loadAllUsers(userLoadedListener);
        } else {
            localDatabase.loadAllUsers(userLoadedListener);
            remoteDatabase.loadAllUsers(userLoadedListener);
        }
    }

    public void loadUser(String userId, final IGameDatabase.OnUserLoadedListener userLoadedListener) {
        //Log.i("DATABASE_DEBUG", "load user: " + userId);

        localDatabase.loadUser(userId, new IGameDatabase.OnUserLoadedListener() {
            boolean hasLoadLocally;

            @Override
            public void onUserLoaded(String id, final User userLocal) {
                hasLoadLocally = true;

                //Log.i("DATABASE_DEBUG", "User local loaded: " + userLocal.toString());

                if(!useLocalVsRemote) {
                    remoteDatabase.loadUser(id, new IGameDatabase.OnUserLoadedListener() {
                        @Override
                        public void onUserLoaded(String id, User user) {
                            //Log.i("DATABASE_DEBUG", "User local more updated of remote: " +userLocal.isMoreUpdatedThan(user)+ "\nLocal user: "+ userLocal.toString() + "\nRemote user" + user.toString());
                            if(userLocal.isMoreUpdatedThan(user)) {
                                userLoadedListener.onUserLoaded(id, userLocal);
                            } else {
                                userLoadedListener.onUserLoaded(id, user);
                            }
                        }

                        @Override
                        public void onLoadCompleted() {
                            //Log.i("DATABASE_DEBUG", "user remote completed");

                            userLoadedListener.onLoadCompleted();
                        }
                    });
                }
            }

            @Override
            public void onLoadCompleted() {
                // If not has load the local user and is set to use the local user
                //Log.i("DATABASE_DEBUG", "user local completed need continue: " + (!hasLoadLocally && useLocalVsRemote) + " Use local vs remote: " + useLocalVsRemote);

                if(!hasLoadLocally && useLocalVsRemote) {

                    userLoadedListener.onLoadCompleted();
                }
            }
        });
    }

    public void setUseLocalVsRemote(boolean useLocalVsRemote) {
        boolean precValue = this.useLocalVsRemote;
        this.useLocalVsRemote = useLocalVsRemote;

        if(!precValue && useLocalVsRemote) {
            OnGoingOffline();
        } else if(precValue && !useLocalVsRemote) {
            OnGoingOnline();
        }
    }


    void OnGoingOffline() {
        cloneRemoteDBIntoLocalDB();
    }

    void OnGoingOnline() {
        UsersManager.getInstance().populateUsers();
        cloneRemoteDBIntoLocalDB();
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





    private void cloneRemoteDBIntoLocalDB() {
        Map<String, User> users = UsersManager.getInstance().getAllUsers();

        for (Map.Entry<String, User> user : users.entrySet()) {
            localDatabase.saveUser(user.getKey(), user.getValue());
        }
    }



}
