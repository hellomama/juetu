package com.tony.juetu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tony.juetu.utils.PreUtils;

/**
 * Created by bushi on 2018/6/5.
 */

public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        initPrefUtils();
        sApp = this;
    }

    private void initPrefUtils()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        PreUtils.getInstance().setPrefs(prefs);
    }

    synchronized public static App getApplication() {
        return sApp;
    }

    synchronized public static Context getAppContext() {
        Context context = sApp.getApplicationContext();
        if (context == null)
        {
            Log.w("App", "the context is null");
        }

        return context;
    }

}
