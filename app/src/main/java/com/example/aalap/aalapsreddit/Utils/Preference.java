package com.example.aalap.aalapsreddit.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aalap on 2017-10-06.
 */

public class Preference {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String TAG = "Preference";
    public static final String MOD_HASH="modhash";
    public static final String COOKIE = "cookie";

    public Preference(Context context) {
        sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setModHash(String modHash){
        editor.putString(MOD_HASH, modHash);
        editor.commit();
    }

    public void setCookie(String cookie){
        editor.putString(COOKIE, cookie);
        editor.commit();
    }

    public String getMogHash(){
        return sharedPreferences.getString(MOD_HASH, "");
    }
    public String getCookie(){
        return sharedPreferences.getString(COOKIE, "");
    }
}
