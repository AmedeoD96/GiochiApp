package it.uniba.di.sms1920.giochiapp.Leaderboard;

public class TitleChild {

    private String tetris;
    private String game2084;
    private String alienRun;
    private String rocket;
    private String frogger;

    private int scoreTetris;
    private int score2048;
    private int alienRunScore;
    private int rocketScore;
    private int froggerScore;

    //TODO mettere nel costruttore i punteggi
    public TitleChild(String tetris, String game2084, String alienRun, String rocket, String frogger) {
        this.tetris = tetris;
        this.game2084 = game2084;
        this.alienRun = alienRun;
        this.rocket = rocket;
        this.frogger = frogger;

        this.scoreTetris = scoreTetris;
        this.score2048 = score2048;
        this.alienRunScore = alienRunScore;
        this.rocketScore = rocketScore;
        this.froggerScore = froggerScore;
    }

    public String getScoreTetris() {
        return String.valueOf(scoreTetris);
    }

    public String getScore2048() {
        return String.valueOf(score2048);
    }

    public String getAlienRunScore() {
        return String.valueOf(alienRunScore);
    }

    public String getRocketScore() {
        return String.valueOf(rocketScore);
    }

    public String getFroggerScore() {
        return String.valueOf(froggerScore);
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
