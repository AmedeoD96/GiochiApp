package it.uniba.di.sms1920.giochiapp.Leaderboard;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

public class TitleParent implements ParentObject {

    private List<Object> mChildrenList;
    private String title;
    private int globalScore;

    public TitleParent(String title, int globalScore) {
        this.title = title;
        this.globalScore = globalScore;
    }

    public String getGlobalScore() {
        return String.valueOf(globalScore);
    }

    public String getTitle() {
        return title;
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

