package com.tony.juetu.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tony.juetu.App;
import com.tony.juetu.Common.Constant;
import com.tony.juetu.manager.DataManager;

public class ContactPresenter {

    private ContactView view;
    private Context app;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null)
            {
                String action = intent.getAction();
                if (action != null && action.equals(Constant.ACTION_UPDATE_ROSTER_ENTRY));
                {
                    updateContact();
                }
            }
        }
    };


    public ContactPresenter(ContactView view) {
        this.view = view;
        app = App.getAppContext();
    }

    public void initContact()
    {
        view.updateContact(DataManager.getInstance().getRosterEntries());
    }

    private void updateContact()
    {
        view.updateContact(DataManager.getInstance().getRosterEntries());
    }
}
