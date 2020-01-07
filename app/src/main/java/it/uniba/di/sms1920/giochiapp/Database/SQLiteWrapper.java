package it.uniba.di.sms1920.giochiapp.Database;

import android.database.sqlite.SQLiteDatabase;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.User;


public class SQLiteWrapper implements IGameDatabase {


    DBOpenHelper mDBHelper;
    SQLiteDatabase mDBDatabase;


    @Override
    public String saveUser(String id, User user) {
        GetDB();
        mDBHelper.saveScores(mDBDatabase, id, user);

        return id;
    }

    @Override
    public void loadAllUsers(OnUserLoadedListener onUserLoadedListener) {
        GetDB();

        mDBHelper.loadScores(mDBDatabase, onUserLoadedListener);
    }


    void GetDB() {
        if(mDBHelper == null) {
            mDBHelper = new DBOpenHelper(GlobalApplicationContext.getAppContext());
            mDBDatabase = mDBHelper.getWritableDatabase();
        }
    }

}
