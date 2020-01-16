package it.uniba.di.sms1920.giochiapp;

import java.util.Objects;

public class User {


    private String id;
    public String name;

    public int scoreTetris = 0;
    public int scoreFrogger = 0;
    public int scoreHelicopter = 0;
    public int scoreAlienrun = 0;
    public int score2048 = 0;
    public int totalScore = 0;

    private int updatesCounter = 0;
    UserListener userChangeCallback;


    public User() {
        updatesCounter = 0;
    }

    public User(String id) {
        this.id = id;
        updatesCounter = 0;
    }

    public void setRegisterCallback(UserListener userListener) {
        userChangeCallback = userListener;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalScore() {
        totalScore = scoreTetris + scoreFrogger + scoreHelicopter + scoreAlienrun + score2048;
        return totalScore;
    }

    public void setName(String name) {
        this.name = name;
        CallOnValueChange();
    }

    public void setScoreTetris(int scoreTetris) {
        this.scoreTetris = scoreTetris;
        CallOnValueChange();
    }

    public void setScoreFrogger(int scoreFrogger) {
        this.scoreFrogger = scoreFrogger;
        CallOnValueChange();
    }

    public void setScoreHelicopter(int scoreHelicopter) {
        this.scoreHelicopter = scoreHelicopter;
        CallOnValueChange();
    }

    public void setScoreAlienrun(int scoreAlienrun) {
        this.scoreAlienrun = scoreAlienrun;
        CallOnValueChange();
    }

    public void setScore2048(int score2048) {
        this.score2048 = score2048;
        CallOnValueChange();
    }


    void CallOnValueChange() {
        if(userChangeCallback != null) {
            userChangeCallback.onValueChange();
        }
    }

    public int getUpdatesCounter() {
        return updatesCounter;
    }

    public void setUpdatesCounter(int updatesCounter) {
        this.updatesCounter = updatesCounter;
    }

    public void updateUser() {
        updatesCounter++;
    }

    public boolean isMoreUpdatedThan(User user) {
        return  updatesCounter > user.updatesCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, scoreTetris, scoreFrogger, scoreHelicopter, scoreAlienrun, score2048, totalScore);
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", scoreTetris=" + scoreTetris +
                ", scoreFrogger=" + scoreFrogger +
                ", scoreHelicopter=" + scoreHelicopter +
                ", scoreAlienrun=" + scoreAlienrun +
                ", score2048=" + score2048 +
                ", totalScore=" + totalScore +
                ", updatesCounter=" + updatesCounter +
                '}';
    }

    public interface UserListener {
        void onValueChange();
    }

}

