package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class Parent implements ParentObject {

    private String userName;
    private int score;
    //utili a fini di leaderboard
    private boolean isCurrentUser;
    private int position;



    public Parent (String userName, int score, boolean isCurrentUser, int position){
        this.userName = userName;
        this.score = score;
        this.isCurrentUser = isCurrentUser;
        this.position = position;
    }

    public String getScore(){
        return String.valueOf(score);
    }

    String getUserName(){
        return userName;
    }

    boolean isCurrentUser() {
        return isCurrentUser;
    }

    int getPosition() {
        return position;
    }

    @Override
    public List<Object> getChildObjectList() {
        return null;
    }

    @Override
    public void setChildObjectList(List<Object> list) { }
}
