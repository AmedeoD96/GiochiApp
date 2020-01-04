package it.uniba.di.sms1920.giochiapp.EndlessRun;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;


public class MainActivityrun extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        DisplayMetrics ds = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ds);
        Constants.SCREEN_WIDTH = ds.widthPixels;
        Constants.SCREEN_HEIGHT = ds.heightPixels;


        setContentView(new GamePanel(this));
    }
}
