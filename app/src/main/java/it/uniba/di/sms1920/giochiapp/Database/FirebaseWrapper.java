package it.uniba.di.sms1920.giochiapp.Database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.sms1920.giochiapp.User;


public class FirebaseWrapper implements IGameDatabase {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    public FirebaseWrapper() { }


    @Override
    public String saveUser(String id, User user) {
        if(id.equals(DatabaseManager.BASIC_ID)) {
            id = myRef.push().getKey();
        }

        myRef = database.getReference().child(id);
        myRef.setValue(user);

        return id;
    }

    @Override
    public void loadUser(String id, final OnUserLoadedListener onUserLoadedListener) {
        myRef = database.getReference().child(id);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);
                onUserLoadedListener.onUserLoaded(dataSnapshot.getKey(), user);
                onUserLoadedListener.onLoadCompleted();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.i("FirebaseTest", "failed to reed database");
            }
        });
    }

    @Override
    public void loadAllUsers(final OnUserLoadedListener onUserLoadedListener) {
        myRef = database.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("FirebaseTest", "data read: "+dataSnapshot.toString());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Log.i("FirebaseTest", "single data read: "+data.toString());

                    User value = data.getValue(User.class);
                    onUserLoadedListener.onUserLoaded(data.getKey(), value);
                    Log.i("FirebaseTest", "user read: "+value.toString());
                }
                onUserLoadedListener.onLoadCompleted();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //Log.i("FirebaseTest", "failed to reed database");
            }
        });
    }

}
