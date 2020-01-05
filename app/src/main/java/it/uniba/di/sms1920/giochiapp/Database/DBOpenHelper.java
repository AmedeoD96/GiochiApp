package it.uniba.di.sms1920.giochiapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {


    final static String TABLE_NAME_USERSCORES = "users_scores";
    public static final String _ID = "_id";
    public static final String NICKNAME = "nickname";

    public static final String SCORE_2048 = "score_2048";
    public static final String SCORE_HELICOPTER = "score_helicopter";
    public static final String SCORE_ALIENRUN = "score_alienrun";
    public static final String SCORE_TETRIS = "score_tetris";


    final public static String NOME_DB = "giochiapp_db";
    final public static Integer VERSION = 1;
    final public Context mContext;

    final static String[] columns = {_ID, NICKNAME, SCORE_2048, SCORE_HELICOPTER, SCORE_ALIENRUN, SCORE_TETRIS};

    final public static String CREATE_CMD =
            "CREATE TABLE " + TABLE_NAME_USERSCORES + "("
                    + _ID + " INTEGER PRIMARY KEY, "
                    + NICKNAME + " TEXT , " + SCORE_2048 + " TEXT , " + SCORE_ALIENRUN + " TEXT , "
                    + SCORE_HELICOPTER + " TEXT , " + SCORE_TETRIS + " TEXT );";


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

    public void saveScores(SQLiteDatabase db, String nickname, Integer id, Integer score_2048, Integer score_helicopter, Integer score_alienrun, Integer score_tetris) {

        ContentValues values = new ContentValues();

        values.put(_ID, id);
        values.put(NICKNAME, nickname);
        values.put(SCORE_2048, score_2048.toString());
        values.put(SCORE_HELICOPTER, score_helicopter.toString());
        values.put(SCORE_ALIENRUN, score_alienrun.toString());
        values.put(SCORE_TETRIS, score_tetris.toString());

        db.insert(DBOpenHelper.TABLE_NAME_USERSCORES, null, values);
        values.clear();


    }


    public void loadScores(SQLiteDatabase db) {

        Cursor cursor_userscores = db.query(DBOpenHelper.TABLE_NAME_USERSCORES, DBOpenHelper.columns, null, new String[]{}, null, null, null);

        cursor_userscores.moveToFirst();

        for (int i = 0; i < cursor_userscores.getCount(); i++) {


            String id = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(_ID));
            String nickname = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(NICKNAME));
            String score_2048 = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(SCORE_2048));
            String score_helicopter = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(SCORE_HELICOPTER));
            String score_alienrun = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(SCORE_ALIENRUN));
            String score_tetris = cursor_userscores.getString(cursor_userscores.getColumnIndexOrThrow(SCORE_TETRIS));
            cursor_userscores.moveToNext();
            Log.i("info", "id "+id+ " nickname "+nickname+ " score 2048 "+ score_2048+ " score helicopter "+ score_helicopter+ " score alienrun "+ score_alienrun+ " score tetris "+ score_tetris);
        }
    }
}
