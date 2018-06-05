package com.tony.juetu;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tony.juetu.utils.PreUtils;

/**
 * Created by bushi on 2018/6/5.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPrefUtils();
    }

    private void initPrefUtils()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreUtils.getInstance().setPrefs(prefs);
    }
}
