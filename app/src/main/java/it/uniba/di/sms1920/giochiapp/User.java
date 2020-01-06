package it.uniba.di.sms1920.giochiapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class User {

    public String name;

    public int scoreTetris = 0;
    public int scoreFrogger = 0;
    public int scoreHelicopter = 0;
    public int scoreAlienrun = 0;
    public int score2048 = 0;

    private List<UserListener> callbacks = new ArrayList<>();

    public User() {

    }

    public void registerCallback(UserListener userListener) {
        callbacks.add(userListener);
    }

    public void unregistCallback(UserListener userListener) {
        callbacks.remove(userListener);
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
        for (UserListener callback: callbacks) {
            callback.OnValueChange();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return scoreTetris == user.scoreTetris &&
                scoreFrogger == user.scoreFrogger &&
                scoreHelicopter == user.scoreHelicopter &&
                scoreAlienrun == user.scoreAlienrun &&
                score2048 == user.score2048 &&
                name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, scoreTetris, scoreFrogger, scoreHelicopter, scoreAlienrun, score2048);
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", scoreTetris=" + scoreTetris +
                ", scoreFrogger=" + scoreFrogger +
                ", scoreHelicopter=" + scoreHelicopter +
                ", scoreAlienrun=" + scoreAlienrun +
                ", score2048=" + score2048 +
                '}';
    }


    public interface UserListener {
        void OnValueChange();
    }

}

