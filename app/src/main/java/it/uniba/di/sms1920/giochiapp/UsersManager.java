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

    private static UsersManager instance;

    private User.UserListener userListener = new User.UserListener() {
        @Override
        public void onValueChange() {
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
        NetworkChangeReceiver.getInstance().registerCallback(new NetworkChangeReceiver.INetworkCallback() {
            @Override
            public void OnNetworkChange(boolean isNetworkPresent) {
                reloadUsers(null);
            }
        });
        reloadUsers(null);
    }


    public void populateUsers(IUsersLoadedCallback usersLoadedCallback) {
        allUsers.clear();
        precUsers.clear();
        internalLoadUsers(usersLoadedCallback);
    }

    public void reloadUsers(IUsersLoadedCallback usersLoadedCallback) {
        precUsers.putAll(allUsers);
        internalLoadUsers(usersLoadedCallback);
    }


    public void getAllUsers(IUsersLoadedCallback usersLoadedCallback) {
        reloadUsers(usersLoadedCallback);
    }


    public User getCurrentUser() {
        User resultUser;
        if(allUsers.containsKey(idCurrentUser)) {

            resultUser = allUsers.get(idCurrentUser);
        } else {

            resultUser = new User();
            resultUser.name = DEFAULT_NAME;
            allUsers.put(idCurrentUser, resultUser);
        }

        Log.i("USER_DEBUG", "Id: " + idCurrentUser + " User: " + resultUser.toString());
        return resultUser;
    }

    public void setIdCurrentUser(String idCurrentUser) {
        if(this.idCurrentUser.equals(DEFAULT_ID) && allUsers.containsValue(this.idCurrentUser)) {

            // Remove the user with the default key to put the user with the correct key
            User currentUser = allUsers.get(this.idCurrentUser);
            allUsers.remove(this.idCurrentUser);

            allUsers.put(idCurrentUser, currentUser);
        }

        this.idCurrentUser = idCurrentUser;
        saveCurrentUserID();
    }
    
    public String getUserID(User user) {
        if(allUsers.containsValue(user)) {
            for (Map.Entry<String, User> entry : allUsers.entrySet()) {
                if(entry.getValue().equals(user)) {
                    return entry.getKey();
                }
            }
        }
        return DEFAULT_ID;
    }


    public void saveCurrentUser() {
        Log.i("USER_DEBUG", "Save current user");
        User user = getCurrentUser();
        user.updateUser();
        DatabaseManager.getInstance().saveUser(idCurrentUser, user);
    }


    public Collection<User> getAllUserSort(OrderType orderType, final boolean ascendentVsDescentend) {
        Collection<User> collection = allUsers.values();
        List<User> list = new ArrayList(collection);

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
        String id = loadCurrentUserID();

        db.loadUser(id, new IGameDatabase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(String id, User user) {
                Log.i("USER_DEBUG", "user id: " +id+ " user: " + user);

                allUsers.put(id, user);
                db.saveUser(id, user);
            }

            @Override
            public void onLoadCompleted() {
                db.loadAllUsers(new IGameDatabase.OnUserLoadedListener() {
                    @Override
                    public void onUserLoaded(String id, User user) {
                        allUsers.put(id, user);
                    }

                    @Override
                    public void onLoadCompleted() {
                        final User user = getCurrentUser();

                        Log.i("USER_DEBUG", "On load completed current user id: " + idCurrentUser + "user: " + user);

                        user.setRegisterCallback(userListener);
                        DatabaseManager.getInstance().saveUsersIntoLocalDB(precUsers, allUsers);

                        if(usersLoadedCallback != null) {
                            usersLoadedCallback.OnAllUsersLoaded(allUsers);
                        }
                    }
                });
            }
        });
    }




    public interface IUsersLoadedCallback {
        void OnAllUsersLoaded(Map<String, User> users);
    }

}
