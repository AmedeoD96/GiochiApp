package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class TetrisLeaderboard extends Fragment {
    //TODO modificare il layout di fragment_single_leaderboard
    Context context = GlobalApplicationContext.getAppContext();

    private RecyclerView recyclerView;

    public TetrisLeaderboard(){
        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_leaderboard, container, false);
        recyclerView = view.findViewById(R.id.rvLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ScoreAdapter scoreAdapter = new ScoreAdapter(context, initData());
        scoreAdapter.setParentAndIconExpandOnClick(false);

        recyclerView.setAdapter(scoreAdapter);

        return view;
    }

    private List<ParentObject> initData(){
        ElementCreator elementCreator = ElementCreator.get(context);
        List<ParentObject> parentObject = new ArrayList<>();

        elementCreator.clearTitles();

        Collection<User> allUser = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_TETRIS, false);

        for(User user: allUser){
            Parent parent = new Parent(user.name, user.scoreTetris);
            elementCreator.addElement(parent);
            parentObject.add(parent);
        }

        return parentObject;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ScoreAdapter scoreAdapter = new ScoreAdapter(context, initData());
        scoreAdapter.setParentAndIconExpandOnClick(false);

        recyclerView.setAdapter(scoreAdapter);

    }
}
