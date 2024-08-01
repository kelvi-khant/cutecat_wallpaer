package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {
    Context mContext;
    String title;
    boolean isClicked;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static SharedPreferencesManager instance;

    public static SharedPreferencesManager getInstance(Context activity) {
        if (instance == null) {
            synchronized (SharedPreferencesManager.class) {
                if (instance == null) {
                    instance = new SharedPreferencesManager(activity);
                }
            }
        }
        return instance;
    }


    public SharedPreferencesManager(Context activity) {
        super();
        mContext = activity;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        editor = sharedPreferences.edit();
    }


    public void savePreferences(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }


    public void delete_preference() {
        editor.clear();
        editor.commit();
        editor.apply();
    }
    public void savePreferences(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String loadSavedPreferences(String title, String defaultText) {
        String value = sharedPreferences.getString(title, defaultText);

        return value;

    }

    public int loadSavedPreferences(String title, int defaultText) {
        int value = sharedPreferences.getInt(title, defaultText);
        return value;

    }

    public boolean loadSavedPreferences(String title, boolean defaultText) {
        boolean value = sharedPreferences.getBoolean(title, defaultText);
        return value;

    }

    public  void clearSharedPreferences(Context context) {
        boolean onBordingScreenShow = SharedPreferencesManager.getInstance(context).loadSavedPreferences("OnBordingScreenShow",true);
        editor.clear();
        SharedPreferencesManager.getInstance(context).savePreferences("OnBordingScreenShow",onBordingScreenShow);

    }

}
