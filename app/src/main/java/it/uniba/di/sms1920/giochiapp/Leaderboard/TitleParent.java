package it.uniba.di.sms1920.giochiapp.Leaderboard;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class TitleParent implements ParentObject {

    private List<Object> mChildrenList;
    private String title;
    private int globalScore;
    private boolean isCurrentUser;



    public TitleParent(String title, int globalScore, boolean isCurrentUser) {
        this.title = title;
        this.globalScore = globalScore;
        this.isCurrentUser = isCurrentUser;
    }

    public String getGlobalScore() {
        return String.valueOf(globalScore);
    }

    public String getTitle() {
        return title;
    }

    public boolean isCurrentUser() {
        return isCurrentUser;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }
}

