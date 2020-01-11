package it.uniba.di.sms1920.giochiapp.Setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

import it.uniba.di.sms1920.giochiapp.R;
import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;

public class Setting extends Fragment {
    private TextView welcome;
    private EditText name;
    private ToggleButton button;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        welcome = view.findViewById(R.id.welcome);
        name = view.findViewById(R.id.etName);
        button = view.findViewById(R.id.toggleButton);
        name.setEnabled(false);

        final String wellcomeInitialString = welcome.getText().toString();

        UsersManager.getInstance().addOnUserLoadedCallback(new UsersManager.IUsersLoadedCallback() {
            @Override
            public void OnAllUsersLoaded(Map<String, User> users) {
                User user = UsersManager.getInstance().getCurrentUser();

                welcome.setText(wellcomeInitialString + user.name);
                name.setText(user.name);
            }
        });

        button.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.isChecked()){
                    name.setEnabled(true);
                }else{
                    name.setEnabled(false);

                    if(!name.getText().toString().equals("")) {
                        UsersManager.getInstance().getCurrentUser().setName(name.getText().toString());
                        welcome.setText("Welcome Back: " + name.getText().toString());
                    }
                }
            }
        });



        return view;
    }


}
