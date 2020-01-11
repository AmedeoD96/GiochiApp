package it.uniba.di.sms1920.giochiapp.GameLeaderboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import it.uniba.di.sms1920.giochiapp.MainActivity;
import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class SingleGameLeaderboard extends Fragment {
    //TODO apro leaderbord->chiudo dal tasto centrale l'app->apro un'altra leaderboard->app crasha
    //FIXME non crasha l'app perchè ho sostituito .commit() con .commitAllowingStateLoss() nella MainActivity ma non mostra più la leaderboard fino a qunado non si riavvia.
    Context context = GlobalApplicationContext.getAppContext();

    private RecyclerView recyclerView;

    public SingleGameLeaderboard(){
        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_leaderboard, container, false);
        recyclerView = view.findViewById(R.id.rvLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private List<ParentObject> initData(MainActivity.GameLeaderboard gameLeaderboard){

        ElementCreator elementCreator = ElementCreator.get(context);

        List<ParentObject> parentObject = new ArrayList<>();

        elementCreator.clearTitles();
        //TextView gameName = getActivity().findViewById(R.id.gameName);

        switch (gameLeaderboard){
            case TETRIS:
                Collection<User> allUserTetris = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_TETRIS, false);

                for(User user: allUserTetris){
                    Parent parent = new Parent(user.name, user.scoreTetris);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
                //gameName.setText("Tetris");
                break;
            case GAME2048:
                Collection<User> allUser2048 = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_2048, false);

                for (User user : allUser2048) {
                    Parent parent = new Parent(user.name, user.score2048);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
                //gameName.setText("2048");
                break;
            case ALIEN_RUN:
                Collection<User> allUserAlienRun = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_ALIENRUN, false);

                for (User user : allUserAlienRun) {
                    Parent parent = new Parent(user.name, user.scoreAlienrun);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
               // gameName.setText("Alien Run");
                break;
            case ROCKET:
                Collection<User> allUserRocket = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_HELICOPTER, false);

                for (User user : allUserRocket) {
                    Parent parent = new Parent(user.name, user.scoreHelicopter);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
               // gameName.setText("Rocket");
                break;
            case FROGGER:
                Collection<User> allUserFrogger = UsersManager.getInstance().getAllUserSort(UsersManager.OrderType.SCORE_FROGGER, false);

                for (User user : allUserFrogger) {
                    Parent parent = new Parent(user.name, user.scoreFrogger);
                    elementCreator.addElement(parent);
                    parentObject.add(parent);
                }
               // gameName.setText("Frogger");
                break;

        }


        return parentObject;
    }

    public void setRecyclerView(MainActivity.GameLeaderboard gameLeaderboard){
        ScoreAdapter scoreAdapter = new ScoreAdapter(context, initData(gameLeaderboard));
        scoreAdapter.setParentAndIconExpandOnClick(false);

        recyclerView.setAdapter(scoreAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i("ONRESUME", "on resume");
        initData(MainActivity.getInstance().gameLeaderboard);
    }
}
