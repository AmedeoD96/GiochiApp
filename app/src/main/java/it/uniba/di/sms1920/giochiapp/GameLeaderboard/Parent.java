package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class Parent implements ParentObject {

    private String userName;
    private int score;
    private boolean isCurrentUser;



    public Parent (String userName, int score, boolean isCurrentUser){
        this.userName = userName;
        this.score = score;
        this.isCurrentUser = isCurrentUser;
    }

    public String getScore(){
        return String.valueOf(score);
    }

    public String getUserName(){
        return userName;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    @Override
    public List<Object> getChildObjectList() {
        return null;
    }

    @Override
    public void setChildObjectList(List<Object> list) {

    }
}
