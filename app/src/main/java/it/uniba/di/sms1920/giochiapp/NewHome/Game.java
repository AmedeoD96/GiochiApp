package it.uniba.di.sms1920.giochiapp.NewHome;


public class Game {
    /*Classe usata per creare un nuovo gioco*/
    private String name;
    private int highScore;
    private int image;

    public Game (String name, int highScore, int image){
        this.name = name;
        this.highScore = highScore;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    int getHighScore() {
        return highScore;
    }

    int getImage() {
        return image;
    }

    void setHighScore(int maxhishscore) {
        highScore = maxhishscore;
    }

}
