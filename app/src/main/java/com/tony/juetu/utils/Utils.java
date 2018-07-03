package com.tony.juetu.utils;

import android.app.ActivityManager;
import android.content.Context;


/**
 * Created by dev on 7/2/18.
 */

public class Utils {

    public static boolean isServiceWork(Context mContext,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getLocalName(String aOrignal)
    {
        String[] re = aOrignal.split("/");
        if (re.length>1) {
            return re[0];
        }else {
            return  aOrignal;
        }
    }
}
