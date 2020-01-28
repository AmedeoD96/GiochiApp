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

    private boolean useLocalVsRemote = true;

    // instanza del database locale
    private IGameDatabase localDatabase;
    // instanza del database remoto
    private IGameDatabase remoteDatabase;


    private DatabaseManager() {
        localDatabase = new SQLiteWrapper(); // viene utilizzato SQLite per la gestione del database locale
        remoteDatabase = new FirebaseWrapper(); // viene utilizzato il servizio di Firebase per il database remoto
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
                // al cambiamento della rete viene modificato il database utilizato
                useLocalVsRemote = isNetworkPresent;
            }
        });
    }

    // salvare uno specifico utente, assunto che l'app ha la sola necessita di salvare l'utente corrente
    public void saveUser(String id, User user) {
        if(useLocalVsRemote) {
            localDatabase.saveUser(id, user);
        } else {
            // prende l'identificatore generato dal database remoto assumendo che l'applicazione salva solo l'utente corrente
            String realID = remoteDatabase.saveUser(id, user);
            // imposta l'identificatore dell'utente corrente
            UsersManager.getInstance().setIdCurrentUser(realID);
            // salva l'utente corrente con il nuovo identificatore
            localDatabase.saveUser(realID, user);

            // rimuove l'utente di default dal database locale
            removeUserFromLocalDB(UsersManager.DEFAULT_ID);
        }
    }

    // caricamento di tutti gli utenti basato sul database attualmente in uso
    public void loadAllUsers(final IGameDatabase.OnUserLoadedListener userLoadedListener) {
        if(useLocalVsRemote) {
            localDatabase.loadAllUsers(userLoadedListener);
        } else {
            remoteDatabase.loadAllUsers(userLoadedListener);
        }
    }

    // caricamento di uno specifico utente
    public void loadUser(final String userId, final IGameDatabase.OnUserLoadedListener userLoadedListener) {
        Log.i("DATABASE_DEBUG", "load user: " + userId);

        // carica l'utente dal database locale
        localDatabase.loadUser(userId, new IGameDatabase.OnUserLoadedListener() {

            // variabile che definisce se è stato caricato l'utente dal database locale
            boolean hasLoadLocally;

            @Override
            public void onUserLoaded(String id, final User userLocal) {
                hasLoadLocally = true;

                Log.i("DATABASE_DEBUG", "using Remote: "+!useLocalVsRemote+" User local loaded: " + userLocal.toString());

                // se deve usare il database remoto scarica l'utente dal databse remoto
                if(!useLocalVsRemote) {
                    remoteDatabase.loadUser(id, new IGameDatabase.OnUserLoadedListener() {
                        @Override
                        public void onUserLoaded(String id, User user) {
                            Log.i("DATABASE_DEBUG", "User local more updated of remote: " +userLocal.isMoreUpdatedThan(user)+ "\nLocal user: "+ userLocal.toString() + "\nRemote user" + user.toString());

                            // contolla quale utente è più aggiornato se quello locale oppure quello remoto e lo passa alla callback
                            if(userLocal.isMoreUpdatedThan(user)) {
                                userLoadedListener.onUserLoaded(id, userLocal);
                            } else {
                                userLoadedListener.onUserLoaded(id, user);
                            }
                        }

                        @Override
                        public void onLoadCompleted() {
                            Log.i("DATABASE_DEBUG", "user remote completed");

                            // finisce il caricamento dell'utente
                            userLoadedListener.onLoadCompleted();
                        }
                    });
                }
            }

            @Override
            public void onLoadCompleted() {
                Log.i("DATABASE_DEBUG", "user local completed need continue: " + (hasLoadLocally && useLocalVsRemote) + " Use local vs remote: " + useLocalVsRemote);

                // se ha caricato l'utente dal database locale è deve usare il database locale
                if(hasLoadLocally && useLocalVsRemote) {

                    // finisce il caricamento dell'utente
                    userLoadedListener.onLoadCompleted();
                }
                // se non ha caricato l'utente corrente e deve usare il databse remoto
                else if(!hasLoadLocally && !useLocalVsRemote) {

                    // carica l'utente da remoto
                    remoteDatabase.loadUser(userId, new IGameDatabase.OnUserLoadedListener() {
                        @Override
                        public void onUserLoaded(String id, User user) {
                            Log.i("DATABASE_DEBUG", "Remote user" + user.toString());
                            userLoadedListener.onUserLoaded(id, user);
                        }

                        @Override
                        public void onLoadCompleted() {
                            Log.i("DATABASE_DEBUG", "user remote completed");

                            // finisce il caricamento dell'utente
                            userLoadedListener.onLoadCompleted();
                        }
                    });
                // se non ha caricato l'utente locale e deve usare il database locale
                } else {

                    // finisce il caricamento dell'utente
                    userLoadedListener.onLoadCompleted();
                }
            }
        });
    }



    // salva una stringa nelle shared preferences
    public void saveString(String key, String value) {
        Context context = GlobalApplicationContext.getAppContext();

        SharedPreferences pref = context.getSharedPreferences(LOCAL_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.apply();
    }

    // carica una stringa dalle shared preferences
    public String loadString(String key, String defalultValue) {
        Context context = GlobalApplicationContext.getAppContext();
        SharedPreferences pref = context.getSharedPreferences(LOCAL_PREF, Context.MODE_PRIVATE);

        String result = pref.getString(key, defalultValue);
        return result;
    }




    // salva gli utenti nel database locale controllando se ci sono state modifiche dall'ultimo salvatagio
    public void saveUsersIntoLocalDB(Map<String, User> precUsers, Map<String, User> users) {

        for (Map.Entry<String, User> user : users.entrySet()) {
            String currentUserKey = user.getKey();
            User currentUser = user.getValue();

            // se l'utente e presente nei precedenti utenti
            if(precUsers.containsKey(currentUserKey)) {
                User precUser = precUsers.get(currentUserKey);

                // controlla quale utente è più aggiornato e lo salva
                if(currentUser.isMoreUpdatedThan(precUser)) {

                    localDatabase.saveUser(currentUserKey, currentUser);
                }
            }
            // se l'utente non è presente nei precedenti lo salva nel database locale
            else {

                localDatabase.saveUser(currentUserKey, currentUser);
            }
        }
    }

    // rimuove un utente dal database locale
    public void removeUserFromLocalDB(String id) {
        Log.i("DATABASE_DEBUG", "Removing local user with id: " + id);
        localDatabase.removeUser(id);
    }



}
