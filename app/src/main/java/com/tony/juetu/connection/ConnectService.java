package com.tony.juetu.connection;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tony.juetu.manager.DataManager;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.Collection;

import static com.tony.juetu.Common.Constant.ACTION_SEND_MESSAGE;
import static com.tony.juetu.Common.Constant.ACTION_UPDATE_PRESENCE;
import static com.tony.juetu.Common.Constant.NAME;
import static com.tony.juetu.Common.Constant.PASSWORD;
import static com.tony.juetu.Common.Constant.UPDATE;
import static com.tony.juetu.Common.Constant.ACTION_UPDATE_UI;
import static com.tony.juetu.connection.ConnectService.HandleType.HandleConnection;
import static com.tony.juetu.connection.ConnectService.HandleType.HandleIntent;

public class ConnectService extends Service implements ConnectResultListener{

    private final static String TAG = ConnectService.class.getSimpleName();
    private XmppConnection xmppConnection;
    private String mName = TAG;
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler mServiceHandler;
    private BroadcastReceiver uiThreadMessageReceiver;
    private Roster roster;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            HandleType type = HandleType.getType(msg.arg2);
            switch (type)
            {
                case HandleIntent:
                    onHandleIntent((Intent)msg.obj);
                    break;
                case HandleConnection:
                    prepareRoster((XMPPConnection)msg.obj);
                    break;
            }
        }
    }

    enum HandleType{
        HandleIntent(1),
        HandleConnection(2);

        int type;
        HandleType(int type) {
            this.type = type;
        }
        public static HandleType getType(int aType)
        {
            for (HandleType categoryType : HandleType.values())
            {
                if (categoryType.type == aType)
                {
                    return categoryType;
                }
            }

            return HandleIntent;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void onHandleIntent(@Nullable Intent intent){
        if (intent != null)
        {
            String name = intent.getStringExtra(NAME);
            String password = intent.getStringExtra(PASSWORD);
            xmppConnection = new XmppConnection(this,name,password);
            try {
                xmppConnection.connect(this);
            }catch (IOException e)
            {
                e.fillInStackTrace();
            }catch (SmackException e1)
            {
                e1.fillInStackTrace();
            }catch (XMPPException e)
            {
                e.fillInStackTrace();
            }
            catch (InterruptedException e)
            {
                e.fillInStackTrace();
            }

        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "=>onCreate");

        super.onCreate();
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
        registerBroadcastRec();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart(intent,startId);
        return START_STICKY;

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.arg2 = HandleIntent.type;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "=>onDestroy");

        if (xmppConnection != null)
        {
            xmppConnection.disconnect();
        }
        mServiceLooper.quit();
        super.onDestroy();
    }

    @Override
    public void Connected(org.jivesoftware.smack.XMPPConnection connection) {
        updateUIBroadcast(true);
        Message msg = mServiceHandler.obtainMessage();
        msg.arg2 = HandleConnection.type;
        msg.obj = connection;
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public void ConnectFail() {
        updateUIBroadcast(false);
    }

    private void updateUIBroadcast(boolean update)
    {
        Intent intent = new Intent();
        intent.putExtra(UPDATE,update);
        intent.setAction(ACTION_UPDATE_UI);
        sendBroadcast(intent);
    }

    private void updatePresenceBrocadcast()
    {
        Intent intent = new Intent();
        intent.setAction(ACTION_UPDATE_PRESENCE);
        sendBroadcast(intent);
    }

    private void registerBroadcastRec()
    {
        uiThreadMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction() != null && intent.getAction().equals(ACTION_SEND_MESSAGE))
                {
                    xmppConnection.sendMessage();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SEND_MESSAGE);
        registerReceiver(uiThreadMessageReceiver,filter);
    }

    private void prepareRoster(org.jivesoftware.smack.XMPPConnection connection)
    {
        try{
            roster = Roster.getInstanceFor(connection);
            if (!roster.isLoaded())
            {
                roster.reloadAndWait();
                Collection<RosterEntry> entries = roster.getEntries();

                for (RosterEntry entry : entries)
                {
                    System.out.println("Here: " + entry);
                }
            }
            roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
            DataManager.getInstance().addToPresencesManager(roster.getAvailablePresences(JidCreate.bareFrom(mName)));
            updatePresenceBrocadcast();
            roster.addSubscribeListener(new SubscribeListener() {
                @Override
                public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
                    DataManager.getInstance().addToPresencesManager(subscribeRequest);
                    updatePresenceBrocadcast();
                    return null;
                }
            });
        }catch (SmackException.NotConnectedException e)
        {
            e.printStackTrace();
        }catch (SmackException.NotLoggedInException e)
        {
            e.printStackTrace();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }catch (XmppStringprepException e)
        {
            e.printStackTrace();
        }
    }
}
