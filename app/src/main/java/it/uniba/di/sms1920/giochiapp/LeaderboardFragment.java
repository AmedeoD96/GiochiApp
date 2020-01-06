package it.uniba.di.sms1920.giochiapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class LeaderboardFragment extends Fragment {
    public TextView countTv;
    public Button countBtn;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        countTv = view.findViewById(R.id.count_tv);
        countTv.setText("Global Leaderboard");
        return view;
    }
}
