package com.tony.juetu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tony.juetu.utils.PreUtils;

/**
 * Created by bushi on 2018/6/5.
 */

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private static App sApp;
    private static final Handler sUIHandler = new Handler(Looper.getMainLooper());


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

    public static boolean execute(Runnable aRunnable)
    {
        boolean success = false;
        if (aRunnable != null)
        {
            success = sUIHandler.post(aRunnable);

            if (!success)
            {
                Log.w(TAG, "the Runnable was failure placed in to the wsMessage queue");
            }
        }

        return success;
    }
}
