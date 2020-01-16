package it.uniba.di.sms1920.giochiapp.Database;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;


public class FirebaseWrapper implements IGameDatabase {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    public FirebaseWrapper() { }


    @Override
    public String saveUser(String id, User user) {
        myRef = database.getReference();

        if(id.equals(UsersManager.DEFAULT_ID)) {
            id = myRef.push().getKey();
        }

        myRef = database.getReference().child(id);
        myRef.setValue(user);

        return id;
    }

    @Override
    public void loadAllUsers(final OnUserLoadedListener onUserLoadedListener) {
        myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User value = data.getValue(User.class);
                    onUserLoadedListener.onUserLoaded(data.getKey(), value);
                }
                Log.i("FIREBASE_TEST", "firebase load completed");

                onUserLoadedListener.onLoadCompleted();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.i("FIREBASE_TEST", "firebase load completed error");
                onUserLoadedListener.onLoadCompleted();
            }
        });
    }

    @Override
    public void loadUser(final String userId, final OnUserLoadedListener onUserLoadedListener) {
        myRef = database.getReference();

        Log.i("FirebaseTest", "trying load user: "+userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("FirebaseTest", "Load user");

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Log.i("FirebaseTest", "User key " +data.getKey()+ " == User searched " +userId+ " = " + (data.getKey().equals(userId)));
                    if(data.getKey().equals(userId)) {

                        User value = data.getValue(User.class);
                        onUserLoadedListener.onUserLoaded(data.getKey(), value);
                        break;
                    }
                }
                onUserLoadedListener.onLoadCompleted();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                onUserLoadedListener.onLoadCompleted();
            }
        });
    }

    @Override
    public void removeUser(String id) {
        myRef = database.getReference(id);
        myRef.removeValue();
    }

}
