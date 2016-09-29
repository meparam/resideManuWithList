package app.num.residemenu;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

/**
 * Created by ADMIN on 11-06-2016.
 */
public class MyApllication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());

    }
}
