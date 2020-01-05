package it.uniba.di.sms1920.giochiapp;

public class User {

    public String name;

    public int scoreTetris = 0;
    public int scorePimball = 0;
    public int scoreHelicopter = 0;
    public int scoreAlienrun = 0;
    public int score2048 = 0;


    public User() {

    }

    public User(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", scoreTetris=" + scoreTetris +
                ", scorePimball=" + scorePimball +
                ", scoreHelicopter=" + scoreHelicopter +
                ", scoreAlienrun=" + scoreAlienrun +
                ", score2048=" + score2048 +
                '}';
    }
}
