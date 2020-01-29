package it.uniba.di.sms1920.giochiapp.Database;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.uniba.di.sms1920.giochiapp.User;
import it.uniba.di.sms1920.giochiapp.UsersManager;


// classe che fornisce un "ponte" tra la gestione di firebase e l'app
// per utilizzare le funzionalita di firebase nell'app
public class FirebaseWrapper implements IGameDatabase {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    public FirebaseWrapper() { }


    @Override
    public String saveUser(String id, User user) {
        // ottenimento della root del file json
        myRef = database.getReference();

        // se l'id passato è quello dell'utente di default
        if(id.equals(UsersManager.DEFAULT_ID)) {
            // generazione di un nuovo id per un nuovo utente
            id = myRef.push().getKey();
        }

        // prende la referenza del nuovo utente creato nel database
        myRef = database.getReference().child(id);
        myRef.setValue(user);

        return id;
    }

    @Override
    public void loadAllUsers(final OnUserLoadedListener onUserLoadedListener) {
        // ottenimento della root del file json
        myRef = database.getReference();

        // ascoltatore per il cambiamento di un valore applicato solo una volta,
        // in modo tale da poterne aggiunge altri dopo senza che siano cumulativi
        myRef.addListenerForSingleValueEvent( new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // cerca gli utenti all'interno dell'albero del file json
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    User value = data.getValue(User.class);
                    value.setId(data.getKey());

                    //caricamento del singolo utente
                    onUserLoadedListener.onUserLoaded(data.getKey(), value);
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
    public void loadUser(final String userId, final OnUserLoadedListener onUserLoadedListener) {
        // ottenimento della root del file json
        myRef = database.getReference();

        // ascoltatore per il cambiamento di un valore applicato solo una volta,
        // in modo tale da poterne aggiunge altri dopo senza che siano cumulativi
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // cerca l'utente da caricare
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if(data.getKey().equals(userId)) {

                        User value = data.getValue(User.class);
                        value.setId(data.getKey());

                        //utente trovato
                        onUserLoadedListener.onUserLoaded(data.getKey(), value);
                        break;
                    }
                }
                // fine caricamento, avviene anche se non viene trovato l'utente
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
        // ottenimento della referenza dell'utente nel database
        myRef = database.getReference(id);
        myRef.removeValue();
    }

}
