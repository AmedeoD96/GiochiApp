package it.uniba.di.sms1920.giochiapp.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.User;


public class SQLiteWrapper extends SQLiteOpenHelper implements IGameDatabase {

    // nome database e versione
    final public static String NOME_DB = "giochiapp_db";
    final public static Integer VERSION = 2;

    // nome tabella
    final static String TABLE_NAME_USERSCORES = "users_scores";

    // nomi delle colonne del database
    public static final String _ID = "_id";
    public static final String NICKNAME = "nickname";

    public static final String SCORE_2048 = "score_2048";
    public static final String SCORE_HELICOPTER = "score_helicopter";
    public static final String SCORE_ALIENRUN = "score_alienrun";
    public static final String SCORE_TETRIS = "score_tetris";
    public static final String SCORE_FROGGER = "score_frogger";
    public static final String UPDATE_COUNTER = "update_counter";

    // array delle colonne del database
    final static String[] columns = {_ID, NICKNAME, SCORE_2048, SCORE_HELICOPTER, SCORE_ALIENRUN, SCORE_TETRIS, SCORE_FROGGER, UPDATE_COUNTER};

    // query di creazione della tabella
    final static String CREATE_CMD =
            "CREATE TABLE " + TABLE_NAME_USERSCORES + "("
                    + _ID + " TEXT PRIMARY KEY, " + NICKNAME + " TEXT, "
                    + SCORE_2048 + " INTEGER, "
                    + SCORE_ALIENRUN + " INTEGER, "
                    + SCORE_HELICOPTER + " INTEGER, "
                    + SCORE_TETRIS + " INTEGER, "
                    + SCORE_FROGGER + " INTEGER, "
                    + UPDATE_COUNTER + " INTEGER);";

    // query di richiesta di uno specifico utente
    final static String CHECK_CONTAINS_CMD = "SELECT * FROM "+TABLE_NAME_USERSCORES+" WHERE "+_ID+"='[x]';";
    // carattere da sostituire all'id dell'utente da cercare
    final static String CHECK_KEY_ID_REPLACE = "[x]";

    SQLiteDatabase mDBDatabase;

    public SQLiteWrapper() {
        super(GlobalApplicationContext.getAppContext(), NOME_DB, null, VERSION);

        mDBDatabase = getWritableDatabase();
    }

    @Override
    public String saveUser(String id, User user) {
        // ottenimento del cursore per lo specifico utente
        Cursor cursor_userscores = GetUserCursor(id);

        // se gia presente vengono aggiornati i valori
        if(cursor_userscores.getCount() > 0) {
            ContentValues values = new ContentValues();

            values.put(NICKNAME, user.name);
            values.put(SCORE_2048, user.score2048);
            values.put(SCORE_HELICOPTER, user.scoreHelicopter);
            values.put(SCORE_ALIENRUN, user.scoreAlienrun);
            values.put(SCORE_TETRIS, user.scoreTetris);
            values.put(SCORE_FROGGER, user.scoreFrogger);
            values.put(UPDATE_COUNTER, user.getUpdatesCounter());

            mDBDatabase.update(TABLE_NAME_USERSCORES, values, _ID + "='" + id +"'", null);

        }
        // se non presente viene aggiunto al database
        else {
            ContentValues values = new ContentValues();

            values.put(_ID, id);
            values.put(NICKNAME, user.name);
            values.put(SCORE_2048, user.score2048);
            values.put(SCORE_HELICOPTER, user.scoreHelicopter);
            values.put(SCORE_ALIENRUN, user.scoreAlienrun);
            values.put(SCORE_TETRIS, user.scoreTetris);
            values.put(SCORE_FROGGER, user.scoreFrogger);
            values.put(UPDATE_COUNTER, user.getUpdatesCounter());

            mDBDatabase.insert(TABLE_NAME_USERSCORES, null, values);
            values.clear();
        }

        // chiusura del cursore
        cursor_userscores.close();
        return id;
    }

    @Override
    public void loadAllUsers(OnUserLoadedListener onUserLoadedListener) {
        // ottenimento cursore per tutti gli utenti del databse
        Cursor cursor_userscores = mDBDatabase.query(TABLE_NAME_USERSCORES, columns, null, new String[]{}, null, null, null);
        // spostamento del cursore al primo elemento
        cursor_userscores.moveToFirst();

        for (int i = 0; i < cursor_userscores.getCount(); i++) {

            // ottenimento dell'utente dalla posizione attuale del cursore
            Pair<String, User> user = GetUser(cursor_userscores);
            cursor_userscores.moveToNext();

            // chiamata della callback di caricamento del singolo utente
            if(onUserLoadedListener != null) {
                onUserLoadedListener.onUserLoaded(user.first, user.second);
            }
        }

        // chiamata della callback di fine caricamento
        if(onUserLoadedListener != null) {
            onUserLoadedListener.onLoadCompleted();
        }

        // chiusura del cursore
        cursor_userscores.close();
    }

    @Override
    public void loadUser(String userId, OnUserLoadedListener onUserLoadedListener) {
        // ottenimento cursore per l'utente
        Cursor userCursor = GetUserCursor(userId);
        // spostamento del cursore al primo elemento
        userCursor.moveToFirst();

        // ottenimento dell'utente dalla posizione attuale del cursore
        Pair<String, User> user = GetUser(userCursor);

        // chiamata della callback di caricamento del singolo utente
        if(user.first != null) {
            onUserLoadedListener.onUserLoaded(user.first, user.second);
        }
        // chiamata della callback di fine caricamento
        onUserLoadedListener.onLoadCompleted();
    }

    @Override
    public void removeUser(String id) {
        // rimozione di uno specifico utente dal database
        mDBDatabase.delete(TABLE_NAME_USERSCORES, _ID + "='" + id +"'", null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }




    // crezione del cursore per un utente specifico
    Cursor GetUserCursor(String userId) {
        return mDBDatabase.rawQuery(CHECK_CONTAINS_CMD.replace(CHECK_KEY_ID_REPLACE, userId), null);
    }

    // ottiene l'utente dato un cusore
    Pair<String, User> GetUser(Cursor cursor) {
        User user = new User();
        String id = null;

        if(cursor.getCount() > 0) {
            id = cursor.getString(cursor.getColumnIndex(_ID));
            user.setId(id);

            user.name = cursor.getString(cursor.getColumnIndex(NICKNAME));

            user.score2048 = cursor.getInt(cursor.getColumnIndex(SCORE_2048));
            user.scoreHelicopter = cursor.getInt(cursor.getColumnIndex(SCORE_HELICOPTER));
            user.scoreAlienrun = cursor.getInt(cursor.getColumnIndex(SCORE_ALIENRUN));
            user.scoreTetris = cursor.getInt(cursor.getColumnIndex(SCORE_TETRIS));
            user.scoreFrogger = cursor.getInt(cursor.getColumnIndex(SCORE_FROGGER));
            user.setUpdatesCounter(cursor.getInt(cursor.getColumnIndex(UPDATE_COUNTER)));
        }
        return new Pair<>(id, user);
    }

}
