package it.uniba.di.sms1920.giochiapp.NewHome;


public class Game {
    private String name;
    private int highScore;
    private int image;

    public Game (String name, int highScore, int image){
        this.name = name;
        this.highScore = highScore;
        this.image = image;
    }

    //Costruttore da usare per la leaderboard
    public Game (String name, int highScore){
        this.name = name;
        this.highScore = highScore;
    }

    public String getName() {
        return name;
    }

    public int getHighScore() {
        return highScore;
    }

    public int getImage() {
        return image;
    }

    public void setHighScore(int maxhishscore) {
        highScore = maxhishscore;
    }

}
