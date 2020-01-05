package it.uniba.di.sms1920.giochiapp;

import android.content.SharedPreferences;

public class Game {
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

    public int getHighScore() {
        return highScore;
    }

    public int getImage() {
        return image;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}
