package it.uniba.di.sms1920.giochiapp;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.giochiapp.Database.DatabaseManager;
import it.uniba.di.sms1920.giochiapp.Database.IGameDatabase;


public class UsersManager {



    public static final String DEFAULT_ID = "-";
    public static final String DEFAULT_NAME = "guest";


    private static final String USER_KEY = "currentUserId";

    private final Map<String, User> allUsers = new HashMap<>();
    private String idCurrentUser;

    private static UsersManager instance;





    private UsersManager() {
        populateIfEmpty();
    }

    public static UsersManager getInstance() {
        if(instance == null) {
            instance = new UsersManager();
        }
        return instance;
    }



    public void populateIfEmpty() {
        if(allUsers.isEmpty()) {
            populateUsers(null);
        }
    }

    public void populateUsers(final IUserManagerCallback userManagerCallback) {
        allUsers.clear();

        loadCurrentUserID();

        DatabaseManager.getInstance().loadAllUsers(new IGameDatabase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(String id, User user) {
                allUsers.put(id, user);
            }

            @Override
            public void onLoadCompleted() {
                final User user = getCurrentUser();

                user.registerCallback(new User.UserListener() {
                    @Override
                    public void OnValueChange() {
                        saveCurrentUser();
                    }
                });

                if(userManagerCallback != null) {
                    userManagerCallback.OnLoadingComplete();
                }
            }
        });
    }


    public Map<String, User> getAllUsers() {
        populateIfEmpty();

        return allUsers;
    }

    public User getCurrentUser() {
        User resultUser;

        Log.i("USER_DEBUG", "Id: " +idCurrentUser+ " is contained: " + allUsers.containsKey(idCurrentUser));
        if(allUsers.containsKey(idCurrentUser)) {

            resultUser = allUsers.get(idCurrentUser);
        } else {

            resultUser = new User();
            resultUser.name = DEFAULT_NAME;
            allUsers.put(idCurrentUser, resultUser);
        }

        Log.i("USER_DEBUG", resultUser.toString());
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
        populateIfEmpty();
        
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
        User user = getCurrentUser();
        DatabaseManager.getInstance().saveUser(idCurrentUser, user);
    }




    private void saveCurrentUserID() {
        DatabaseManager.getInstance().saveString(USER_KEY, idCurrentUser);
    }

    private void loadCurrentUserID() {
        idCurrentUser = DatabaseManager.getInstance().loadString(USER_KEY, DEFAULT_ID);
    }



    public interface IUserManagerCallback {
        void OnLoadingComplete();
    }

}
