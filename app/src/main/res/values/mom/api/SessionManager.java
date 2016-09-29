package vp.mom.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // LogCat tag
    private static String TAG = vp.mom.api.SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    public static final String PREF_NAME = "MOMSession";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setBooleanSessionData(String KEY,boolean isLoggedIn) {

        editor.putBoolean(KEY, isLoggedIn);

        // commit changes
        editor.commit();

     ///   Log.d(TAG, "User login session modified!");
    }
    public boolean getBooleanSessionData(String KEY){
        return pref.getBoolean(KEY, false);
    }
    public void setStringSessionData(String KEY,String data) {

        editor.putString(KEY, data);

        // commit changes
        editor.commit();

      //  Log.d(TAG, "User login session modified!");
    }


    public int getIntegerSessionData(String KEY){
        return pref.getInt(KEY, 0);
    }

    public void setIntegerSessionData(String KEY,int data) {

        editor.putInt(KEY, data);

        // commit changes
        editor.commit();

      //  Log.d(TAG, "User login session modified!");
    }


    public String getStringSessionData(String KEY){
        return pref.getString(KEY, "");
    }

}
