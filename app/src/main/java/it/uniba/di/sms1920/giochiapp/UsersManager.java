package it.uniba.di.sms1920.giochiapp;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import it.uniba.di.sms1920.giochiapp.Database.DatabaseManager;
import it.uniba.di.sms1920.giochiapp.Database.IGameDatabase;

public class UsersManager {

    public static final Map<String, User> allUsers = new HashMap<>();
    static String idCurrentUser;
    static boolean userFind;

    public static void populateUsers(Context context, final String currentUserId) {
        allUsers.clear();
        userFind = false;

        DatabaseManager.getInstance().loadAllUsers(context, new IGameDatabase.OnUserLoadedListener() {
                @Override
                public void onUserLoaded(String id, User user) {
                    allUsers.put(id, user);

                    if(currentUserId.equals(id)) {
                        idCurrentUser = id;
                        userFind = true;
                    }
                }

                @Override
                public void onLoadCompleted() { }
        });
    }

    public static User GetCurrentUser() {
        return allUsers.get(idCurrentUser);
    }
}
