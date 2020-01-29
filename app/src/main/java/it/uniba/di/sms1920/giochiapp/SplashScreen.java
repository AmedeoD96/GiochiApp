package it.uniba.di.sms1920.giochiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import it.uniba.di.sms1920.giochiapp.NewHome.GameList;

public class SplashScreen extends AppCompatActivity {

    static long SPLASH_SCREEN_DURATION = 1000;

    boolean splashScreenShowed = true;

    // Definisce se gli utenti sono stati caricati
    boolean usersLoaded = false;
    // Definisce se lo splash screen è stato mostrato per un tempo sufficiente
    boolean screenTimeEnded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        // carica tutti gli utenti è prova a mostrare l'applicazione
        UsersManager.getInstance().reloadUsers(new UsersManager.IUsersLoadedCallback() {
            @Override
            public void OnAllUsersLoaded(Map<String, User> users) {
                if(splashScreenShowed) {
                    splashScreenShowed = false;
                    usersLoaded = true;

                    tryShowApp();
                }
            }
        });

        // attende un tempo dopodiche prova a mostrare l'applicazione
        Thread thread = new Thread() {
            @Override
            public void run() {
                try{

                    sleep(SPLASH_SCREEN_DURATION);
                    screenTimeEnded = true;

                    tryShowApp();

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }


    void tryShowApp() {
        // Mostra l'applicazione solo se sono stati caricati tutti gli utenti e se è passato abbastanza tempo
        if(usersLoaded && screenTimeEnded) {
            Intent intent = new Intent(getApplicationContext(), GameList.class);
            startActivity(intent);
            finish();
        }
    }

}
