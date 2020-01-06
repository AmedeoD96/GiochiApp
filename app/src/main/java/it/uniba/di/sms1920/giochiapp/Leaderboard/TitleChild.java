package it.uniba.di.sms1920.giochiapp.Leaderboard;

public class TitleChild {

    private String tetris;
    private String game2084;
    private String alienRun;
    private String rocket;
    private String frogger;

    public TitleChild(String tetris, String game2084, String alienRun, String rocket, String frogger) {
        this.tetris = tetris;
        this.game2084 = game2084;
        this.alienRun = alienRun;
        this.rocket = rocket;
        this.frogger = frogger;
    }

    public String getOption1() {
        return tetris;
    }


    public String getOption2() {
        return game2084;
    }

    public String getTetris() {
        return tetris;
    }

    public String getGame2084() {
        return game2084;
    }

    public String getAlienRun() {
        return alienRun;
    }

    public String getRocket() {
        return rocket;
    }

    public String getFrogger() {
        return frogger;
    }
}
