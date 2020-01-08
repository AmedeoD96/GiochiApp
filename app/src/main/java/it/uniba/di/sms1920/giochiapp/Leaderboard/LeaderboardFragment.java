package it.uniba.di.sms1920.giochiapp.Leaderboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class LeaderboardFragment extends Fragment {
    Context context = GlobalApplicationContext.getAppContext();
    public TextView countTv;
    private RecyclerView recyclerView;


    public LeaderboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        recyclerView = view.findViewById(R.id.rvGame);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        MyAdapter mAdapter = new MyAdapter(context, initData());


        mAdapter.setParentClickableViewAnimationDefaultDuration();
        mAdapter.setParentAndIconExpandOnClick(true);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        countTv = view.findViewById(R.id.gameName);
        countTv.setText("Global Leaderboard");
        return view;
    }


    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(context);
        List<ParentObject> parentObject = new ArrayList<>();
        titleCreator.clearTitles();

        Collection<User> allUser = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.TOTAL_SCORE, false);

        for (User user : allUser) {
            TitleParent title = new TitleParent(user.name, user.getTotalScore());

            List<Object> childList = new ArrayList<>();

            childList.add(new TitleChild(
                            "Tetris",
                            "2048",
                            "Alien Run",
                            "Rocket",
                            "Frogger",
                            user.scoreTetris,
                            user.score2048,
                            user.scoreAlienrun,
                            user.scoreHelicopter,
                            user.scoreFrogger
                    )
            );

            title.setChildObjectList(childList);
            titleCreator.addTitle(title);
            parentObject.add(title);
        }

        return parentObject;
    }

}
