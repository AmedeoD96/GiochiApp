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

import it.uniba.di.sms1920.giochiapp.GlobalApplicationContext;
import it.uniba.di.sms1920.giochiapp.Home.GameAdapter;
import it.uniba.di.sms1920.giochiapp.R;

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
        countTv = view.findViewById(R.id.count_tv);
        countTv.setText("Global Leaderboard");
        return view;
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(context);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();
        for(TitleParent title:titles)
        {
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Add to contacts","Send message"));
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;

    }
}
