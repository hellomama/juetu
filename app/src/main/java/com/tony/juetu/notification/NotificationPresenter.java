package com.tony.juetu.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tony.juetu.App;
import com.tony.juetu.Common.Constant;
import com.tony.juetu.manager.DataManager;

/**
 * Created by dev on 6/28/18.
 */

public class NotificationPresenter {

    private NotificationView view;
    private Context app;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null)
            {
                String action = intent.getAction();
                if (action != null && action.equals(Constant.ACTION_UPDATE_PRESENCE));
                {
                    updateNotification();
                }
            }
        }
    };

    public NotificationPresenter(NotificationView view) {
        this.view = view;
        app = App.getAppContext();
    }

    public void initNotification()
    {
        app.registerReceiver(receiver,new IntentFilter(Constant.ACTION_UPDATE_PRESENCE));
    }

    private void updateNotification()
    {
        if (view != null)
        {
            view.updateNotification(DataManager.getInstance().getPresences());
        }
    }
}
