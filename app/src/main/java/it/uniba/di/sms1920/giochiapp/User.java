package it.uniba.di.sms1920.giochiapp;

import java.util.Objects;

import it.uniba.di.sms1920.giochiapp.NewHome.GameHelper;

public class User {


    private String id;
    public String name;

    public int scoreTetris = 0;
    public int scoreFrogger = 0;
    public int scoreHelicopter = 0;
    public int scoreAlienrun = 0;
    public int score2048 = 0;
    private int totalScore = 0;
    public int numberMissilesSurpassed = 0;

    public int getNumberMissilesSurpassed() {
        return numberMissilesSurpassed;
    }

    public void setNumberMissilesSurpassed(int numberMissilesSurpassed) {
        this.numberMissilesSurpassed = numberMissilesSurpassed;
    }

    // Variabile necessaria alla decisione dell'utente da tenere, se tenere l'utente nel database locale oppure quello in remoto
    private int updatesCounter = 0;
    UserListener userChangeCallback;


    public User() {
        updatesCounter = 0;
    }

    public User(String id) {
        this.id = id;
        updatesCounter = 0;
    }

    public void setOnChangeCallback(UserListener userListener) {
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
        OnValueChange();
    }

    public void setScoreTetris(int scoreTetris) {
        this.scoreTetris = scoreTetris;
        OnValueChange();
    }

    public void setScoreFrogger(int scoreFrogger) {
        this.scoreFrogger = scoreFrogger;
        OnValueChange();
    }

    public void setScoreHelicopter(int scoreHelicopter) {
        this.scoreHelicopter = scoreHelicopter;
        OnValueChange();
    }

    public void setScoreAlienrun(int scoreAlienrun) {
        this.scoreAlienrun = scoreAlienrun;
        OnValueChange();
    }

    public void setScore2048(int score2048) {
        this.score2048 = score2048;
        OnValueChange();
    }


    public int getScore(GameHelper.Games game) {
        switch (game) {

            case TETRIS:
                return scoreTetris;

            case GAME_2048:
                return score2048;

            case ENDLESS:
                return scoreAlienrun;

            case HELICOPTER:
                return scoreHelicopter;

            case FROGGER:
                return scoreFrogger;
        }
        return 0;
    }


    void OnValueChange() {
        if(userChangeCallback != null) {
            userChangeCallback.onValueChange();
        }
    }

    public int getUpdatesCounter() {
        return updatesCounter;
    }

    // imposta il valore di aggiornamento dell'utente
    public void setUpdatesCounter(int updatesCounter) {
        this.updatesCounter = updatesCounter;
    }

    // incrementa l'aggiornamento di 1
    public void updateUser() {
        updatesCounter++;
    }

    // informa se l'utente è aggiornato maggiormente di quello passato
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

