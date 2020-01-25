package it.uniba.di.sms1920.giochiapp.NewHome;

import android.content.Context;
import android.content.Intent;


public class GameHelper {

    public enum Games {
        TETRIS,
        GAME_2048,
        ENDLESS,
        HELICOPTER,
        FROGGER
    }


    public static void showGame(Context context, Games game) {

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

        if(intent != null) {
            context.startActivity(intent);
        }
    }

    public static void showGameLeaderboard(Context context, Games game) {
        Intent intent = new Intent(context, GameScoreboard.class);
        intent.putExtra("game", game.toString());
        context.startActivity(intent);
    }


}
