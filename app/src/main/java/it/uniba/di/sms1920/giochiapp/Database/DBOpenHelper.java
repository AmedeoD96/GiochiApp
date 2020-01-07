package it.uniba.di.sms1920.giochiapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.uniba.di.sms1920.giochiapp.User;

public class DBOpenHelper extends SQLiteOpenHelper {


    final static String TABLE_NAME_USERSCORES = "users_scores";
    public static final String _ID = "_id";
    public static final String NICKNAME = "nickname";

    public static final String SCORE_2048 = "score_2048";
    public static final String SCORE_HELICOPTER = "score_helicopter";
    public static final String SCORE_ALIENRUN = "score_alienrun";
    public static final String SCORE_TETRIS = "score_tetris";
    public static final String SCORE_FROGGER = "score_frogger";


    final public static String NOME_DB = "giochiapp_db";
    final public static Integer VERSION = 1;
    final public Context mContext;

    final static String[] columns = {_ID, NICKNAME, SCORE_2048, SCORE_HELICOPTER, SCORE_ALIENRUN, SCORE_TETRIS};

    final static String CREATE_CMD =
            "CREATE TABLE " + TABLE_NAME_USERSCORES + "("
                    + _ID + " TEXT PRIMARY KEY, " + NICKNAME + " TEXT , "
                    + SCORE_2048 + " INTEGER , "
                    + SCORE_ALIENRUN + " INTEGER , "
                    + SCORE_HELICOPTER + " INTEGER , "
                    + SCORE_TETRIS + " INTEGER , "
                    + SCORE_FROGGER + " INTEGER);";

    final static String CHECK_CONTAINS_CMD = "SELECT * FROM "+DBOpenHelper.TABLE_NAME_USERSCORES+" WHERE "+DBOpenHelper._ID+"='[x]';";
    final static String CHECK_KEY_ID_REPLACE = "[x]";

    public DBOpenHelper(Context context) {
        super(context, NOME_DB, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveScores(SQLiteDatabase db, String id, User user) {
        Cursor cursor_userscores = db.rawQuery(CHECK_CONTAINS_CMD.replace(CHECK_KEY_ID_REPLACE, id), null);

        if(cursor_userscores.getCount() > 0) {

            ContentValues values = new ContentValues();

            values.put(NICKNAME, user.name);
            values.put(SCORE_2048, String.valueOf(user.score2048));
            values.put(SCORE_HELICOPTER, String.valueOf(user.scoreHelicopter));
            values.put(SCORE_ALIENRUN, String.valueOf(user.scoreAlienrun));
            values.put(SCORE_TETRIS, String.valueOf(user.scoreTetris));
            values.put(SCORE_FROGGER, String.valueOf(user.scoreFrogger));

            db.update(DBOpenHelper.TABLE_NAME_USERSCORES, values,
                    _ID + "='" + id +"'", null);

        } else {
            ContentValues values = new ContentValues();

            values.put(_ID, id);
            values.put(NICKNAME, user.name);
            values.put(SCORE_2048, String.valueOf(user.score2048));
            values.put(SCORE_HELICOPTER, String.valueOf(user.scoreHelicopter));
            values.put(SCORE_ALIENRUN, String.valueOf(user.scoreAlienrun));
            values.put(SCORE_TETRIS, String.valueOf(user.scoreTetris));
            values.put(SCORE_FROGGER, String.valueOf(user.scoreFrogger));

            db.insert(DBOpenHelper.TABLE_NAME_USERSCORES, null, values);
            values.clear();
        }

        cursor_userscores.close();
    }


    public void loadScores(SQLiteDatabase db, IGameDatabase.OnUserLoadedListener userLoadedListener) {

        Cursor cursor_userscores = db.query(DBOpenHelper.TABLE_NAME_USERSCORES, DBOpenHelper.columns, null, new String[]{}, null, null, null);
        cursor_userscores.moveToFirst();

        for (int i = 0; i < cursor_userscores.getCount(); i++) {

            User user = new User();

            String id = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(_ID));
            user.name = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(NICKNAME));

            user.score2048 = cursor_userscores.getInt(cursor_userscores.getColumnIndexOrThrow(SCORE_2048));
            user.scoreHelicopter = cursor_userscores.getInt(cursor_userscores.getColumnIndexOrThrow(SCORE_HELICOPTER));
            user.scoreAlienrun = cursor_userscores.getInt(cursor_userscores.getColumnIndexOrThrow(SCORE_ALIENRUN));
            user.scoreTetris = cursor_userscores.getInt(cursor_userscores.getColumnIndexOrThrow(SCORE_TETRIS));
            user.scoreFrogger = cursor_userscores.getInt(cursor_userscores.getColumnIndexOrThrow(SCORE_FROGGER));

            cursor_userscores.moveToNext();

            userLoadedListener.onUserLoaded(id, user);
        }
        userLoadedListener.onLoadCompleted();

        cursor_userscores.close();
    }
}
