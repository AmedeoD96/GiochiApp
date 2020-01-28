package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;


public class GameHelper {

    static final String GAME_LEADERBOADR_EXTRA_GAME = "game";

    // Enum che rapresenta tutti i giochi
    public enum Games {
        TETRIS,
        GAME_2048,
        ENDLESS,
        HELICOPTER,
        FROGGER
    }

    // Permette di giocare ad uno specifico gioco
    static void playGame(Context context, Games game) {

        Intent intent = null;
        switch (game) {

            case TETRIS:
                intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Tetris.Tetris.class);
                break;
            case GAME_2048:
                intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Game2048.MainActivity2048.class);
                break;
            case ENDLESS:
                intent = new Intent(context, it.uniba.di.sms1920.giochiapp.EndlessRun.MainActivityrun.class);
                break;
            case HELICOPTER:
                intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Helicopter.Game.class);
                break;
            case FROGGER:
                intent = new Intent(context, it.uniba.di.sms1920.giochiapp.Frogger.MainActivity.class);
                break;
        }
            context.startActivity(intent);
    }

    // Mostra una leaderbard specifica in base al gioco passato
    static void showGameLeaderboard(Context context, Games game) {
        Intent intent = new Intent(context, GameScoreboard.class);
        intent.putExtra(GAME_LEADERBOADR_EXTRA_GAME, game.toString());
        context.startActivity(intent);
    }


    static String getGameName(Games game) {
        Context context = GlobalApplicationContext.getAppContext();
        Resources resources = context.getResources();

        String gameName = "";
        switch (game) {

            case TETRIS:
                gameName = resources.getString(R.string.tetris);
                break;
            case GAME_2048:
                gameName = resources.getString(R.string.header);
                break;
            case ENDLESS:
                gameName = resources.getString(R.string.alienRun);
                break;
            case HELICOPTER:
                gameName = resources.getString(R.string.rocket);
                break;
            case FROGGER:
                gameName = resources.getString(R.string.frogger);
                break;
        }
        return gameName;
    }

}
