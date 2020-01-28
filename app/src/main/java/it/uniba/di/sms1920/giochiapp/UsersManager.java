package it.uniba.di.sms1920.giochiapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.uniba.di.sms1920.giochiapp.Database.DatabaseManager;
import it.uniba.di.sms1920.giochiapp.Database.IGameDatabase;
import it.uniba.di.sms1920.giochiapp.Network.NetworkChangeReceiver;


public class UsersManager {

    public static final String DEFAULT_ID = "-";
    public static final String DEFAULT_NAME = "guest";

    private static final String USER_KEY = "currentUserId";

    private final Map<String, User> precUsers = new HashMap<>();
    private final Map<String, User> allUsers = new HashMap<>();
    private String idCurrentUser;

    // instanza singleton
    private static UsersManager instance;

    // creazione dell'ascoltatore dei cambiamenti dell'utente corrente
    private final User.UserListener userListener = new User.UserListener() {
        @Override
        public void onValueChange() {
            // al cambiamento di un valore di uno user viene salvato l'utente corrente
            saveCurrentUser();
        }
    };



    private UsersManager() { }

    public static UsersManager getInstance() {
        if(instance == null) {
            instance = new UsersManager();
        }
        return instance;
    }


    public void init() {
        // aggiunta dell'ascoltatore per il cambio del collegamento ad internet
        NetworkChangeReceiver.getInstance().registerCallback(new NetworkChangeReceiver.INetworkCallback() {
            @Override
            public void OnNetworkChange(boolean isNetworkPresent) {
                //quando la rete cambia vengono ricaricati tutti gli utenti
                reloadUsers(null);
            }
        });
        //ricarica degli utenti all'inizio
        reloadUsers(null);
    }

    public void reloadUsers(IUsersLoadedCallback usersLoadedCallback) {
        precUsers.putAll(allUsers);
        internalLoadUsers(usersLoadedCallback);
    }


    public void getAllUsers(IUsersLoadedCallback usersLoadedCallback) {
        // ritorna tutti gli utenti ricaricando
        removeDefaultUser();
        reloadUsers(usersLoadedCallback);
    }


    public User getCurrentUser() {
        User resultUser;

        // ritorna l'utente corrente se presente, se non presente ritorna l'utente di default
        if(allUsers.containsKey(idCurrentUser)) {

            resultUser = allUsers.get(idCurrentUser);

        } else {

            // Creazione dell'utente di default
            resultUser = new User(idCurrentUser);
            resultUser.name = DEFAULT_NAME;
            allUsers.put(idCurrentUser, resultUser);
        }

        return resultUser;
    }


    public void setIdCurrentUser(String idCurrentUser) {
        Log.i("USER_DEBUG", "Trying to set new user id: " + idCurrentUser);

        // se l'id dell'utente attuale è quello di default viene rimosso e sostituito con l'utente corrente con l'id corretto
        if(this.idCurrentUser.equals(DEFAULT_ID) ) {

            Log.i("USER_DEBUG", "Removing current id from database " + this.idCurrentUser);
            DatabaseManager.getInstance().removeUserFromLocalDB(this.idCurrentUser);

            if( allUsers.containsValue(this.idCurrentUser)) {
                // Sostituisce la chiave dell'utente corrente da quella di default a la chiave passata
                User currentUser = allUsers.get(this.idCurrentUser);
                allUsers.remove(this.idCurrentUser);
                allUsers.put(idCurrentUser, currentUser);
            }
        }

        this.idCurrentUser = idCurrentUser;
        saveCurrentUserID();
    }


    public void saveCurrentUser() {
        Log.i("USER_DEBUG", "Save current user");
        User user = getCurrentUser();
        user.updateUser();
        DatabaseManager.getInstance().saveUser(idCurrentUser, user);
    }

    // ritorna la collection di tutti gli utenti ordinati tramite l'orderType passato
    public Collection<User> getAllUserSort(OrderType orderType, final boolean ascendentVsDescentend) {
        Collection<User> collection = allUsers.values();
        List<User> list = new ArrayList<>(collection);

        switch (orderType) {
            case TOTAL_SCORE:
                Collections.sort(list, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return compareInt(o1.getTotalScore(), o2.getTotalScore(), ascendentVsDescentend);
                    }
                });
                break;
            case SCORE_HELICOPTER:
                Collections.sort(list, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return compareInt(o1.scoreHelicopter, o2.scoreHelicopter, ascendentVsDescentend);
                    }
                });
                break;
            case SCORE_ALIENRUN:
                Collections.sort(list, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return compareInt(o1.scoreAlienrun, o2.scoreAlienrun, ascendentVsDescentend);
                    }
                });
                break;
            case SCORE_2048:
                Collections.sort(list, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return compareInt(o1.score2048, o2.score2048, ascendentVsDescentend);
                    }
                });
                break;
            case SCORE_TETRIS:
                Collections.sort(list, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return compareInt(o1.scoreTetris, o2.scoreTetris, ascendentVsDescentend);
                    }
                });
                break;
            case SCORE_FROGGER:
                Collections.sort(list, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return compareInt(o1.scoreFrogger, o2.scoreFrogger, ascendentVsDescentend);
                    }
                });
                break;
        }

        return list;
    }

    // metodo di comparazione di due interi
    int compareInt(int a, int b, boolean ascendentVsDescendent) {
        if(ascendentVsDescendent) {
            if(a > b) {
                return 1;
            } else if(a < b) {
                return -1;
            } else {
                return 0;
            }
        } else {
            if(a < b) {
                return 1;
            } else if(a > b) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    // tipologie di ordinamento possibili
    public enum OrderType {
        TOTAL_SCORE,
        SCORE_HELICOPTER,
        SCORE_ALIENRUN,
        SCORE_2048,
        SCORE_TETRIS,
        SCORE_FROGGER
    }


    private void saveCurrentUserID() {
        DatabaseManager.getInstance().saveString(USER_KEY, idCurrentUser);
    }

    private String loadCurrentUserID() {
        idCurrentUser = DatabaseManager.getInstance().loadString(USER_KEY, DEFAULT_ID);
        return idCurrentUser;
    }


    private void internalLoadUsers(final IUsersLoadedCallback usersLoadedCallback) {
        Log.i("DATABASE_DEBUG", "reload users");

        final DatabaseManager db = DatabaseManager.getInstance();
        // caricamento dell'id dell'utente corrente
        String id = loadCurrentUserID();

        removeDefaultUser();

        // caricamento dell'utente corrente
        db.loadUser(id, new IGameDatabase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(String id, User user) {
                Log.i("USER_DEBUG", "user id: " +id+ " user: " + user);

                // quando l'utente corrente viene caricato viene salvato
                allUsers.put(id, user);
                db.saveUser(id, user);
            }

            @Override
            public void onLoadCompleted() {
                // al completamento dell'utente corrente carica tutti gli altri utenti
                db.loadAllUsers(new IGameDatabase.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(String id, User user) {
                        //al caricamento di ogni utente viene aggiunto alla mappa degli utenti
                        allUsers.put(id, user);
                    }

                    @Override
                    public void onLoadCompleted() {
                        final User user = getCurrentUser();

                        Log.i("USER_DEBUG", "On load completed current user id: " + idCurrentUser + "user: " + user);

                        // registra la callback della modifica dell'utente corrente
                        user.setOnChangeCallback(userListener);
                        // duplica il database remoto nel database locale
                        DatabaseManager.getInstance().saveUsersIntoLocalDB(precUsers, allUsers);

                        // chiamata della callback della fine del caricamento degli utenti
                        if(usersLoadedCallback != null) {
                            usersLoadedCallback.OnAllUsersLoaded(allUsers);
                        }
                    }
                });
            }
        });
    }


    void removeDefaultUser() {
        // remove l'utente di default quando l'utente id corrente non è il default ed è contenuto nella mappa degli utenti locali
        if(!idCurrentUser.equals(DEFAULT_ID) && allUsers.containsKey(DEFAULT_ID)) {
            allUsers.remove(DEFAULT_ID);
        }
    }


    // interfaccia per la callback del caricamento degli utenti
    public interface IUsersLoadedCallback {
        void OnAllUsersLoaded(Map<String, User> users);
    }

}
