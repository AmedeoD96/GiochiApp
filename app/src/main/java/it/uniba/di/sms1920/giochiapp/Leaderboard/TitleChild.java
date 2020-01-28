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


    //operazioni di get e set sulle stringhe riferite ai punteggi dei singoli giochi
    public TitleChild(String tetris, String game2084, String alienRun, String rocket, String frogger, int scoreTetris, int score2048, int alienRunScore, int rocketScore, int froggerScore) {
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

    String getScoreTetris() {
        return String.valueOf(scoreTetris);
    }

    public String getScore2048() {
        return String.valueOf(score2048);
    }

    String getAlienRunScore() {
        return String.valueOf(alienRunScore);
    }

    String getRocketScore() {
        return String.valueOf(rocketScore);
    }

    String getFroggerScore() {
        return String.valueOf(froggerScore);
    }

    String getGame2084() {
        return game2084;
    }

    public String getTetris() {
        return tetris;
    }

    String getAlienRun() {
        return alienRun;
    }

    String getRocket() {
        return rocket;
    }

    public String getFrogger() {
        return frogger;
    }
}
