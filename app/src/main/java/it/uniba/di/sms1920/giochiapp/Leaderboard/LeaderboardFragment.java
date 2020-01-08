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
import java.util.List;
import java.util.Map;

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

    //TODO associare i punteggi all'utente che ha fatto quei punteggi
    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(context);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();

        Map<String, User> allUser = UsersManager.getInstance().getAllUsers();
        int i = 0;
        int j = 0;
        List<Object> childList = new ArrayList<>();


        for(Map.Entry<String, User> user : allUser.entrySet()) {
            childList.add(new TitleChild("Tetris", "2048", "Alien Run", "Rocket", "Frogger",
                    user.getValue().scoreTetris, user.getValue().score2048, user.getValue().scoreAlienrun, user.getValue().scoreHelicopter, user.getValue().scoreFrogger));
            titles.get(i).setChildObjectList(childList);
            parentObject.add(titles.get(i));
        }
        return parentObject;
    }
}
