package com.tony.juetu.xmpp;

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

import com.tony.juetu.common.Constant;
import com.tony.juetu.manager.DataManager;
import com.tony.juetu.utils.PreUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.Collection;

import static com.tony.juetu.xmpp.ConnectService.HandleType.HandleConnection;
import static com.tony.juetu.xmpp.ConnectService.HandleType.HandleIntent;

public class ConnectService extends Service implements ConnectResultListener{

    private final static String TAG = ConnectService.class.getSimpleName();
    private XmppConnection xmppConnection;
    private String mName,mPassword;
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
            mName = intent.getStringExtra(Constant.NAME);
            mPassword = intent.getStringExtra(Constant.PASSWORD);
            xmppConnection = new XmppConnection(this,mName,mPassword);
            connect();

        }
    }

    private void connect()
    {
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

    private void sendMessage(Intent intent)
    {
        Message msg = mServiceHandler.obtainMessage();
        msg.arg2 = HandleIntent.type;
        msg.obj = intent;
        mServiceHandler.sendMessage(msg);
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

        if (uiThreadMessageReceiver != null)
        {
            unregisterReceiver(uiThreadMessageReceiver);
        }
        mServiceLooper.quit();
        super.onDestroy();
    }

    @Override
    public void Connected(org.jivesoftware.smack.XMPPConnection connection) {
        saveAccount();
        XmppChatManager.getInstance().initManager(connection);
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

    private void saveAccount()
    {
        PreUtils.getInstance().saveAccount(mName,mPassword);
    }

    private void updateUIBroadcast(boolean update)
    {
        Intent intent = new Intent();
        intent.putExtra(Constant.UPDATE,update);
        intent.setAction(Constant.ACTION_UPDATE_UI);
        sendBroadcast(intent);
    }

    private void updatePresenceBroadcast()
    {
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_UPDATE_PRESENCE);
        sendBroadcast(intent);
    }

    private void updateRosterBroadcast()
    {
        Intent i = new Intent();
        i.setAction(Constant.ACTION_UPDATE_ROSTER_ENTRY);
        sendBroadcast(i);
    }

    private void registerBroadcastRec()
    {
        uiThreadMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG,"receive broadcast");
                if (intent != null)
                {
                    String action = intent.getAction();
                    if (action != null) {
                        switch (action)
                        {
                            case Constant.ACTION_SEND_MESSAGE:
//                                xmppConnection.sendMessage();
                                break;
                            case Constant.ACTION_SEND_SUBSCRIBE:
                                String to = intent.getStringExtra(Constant.EXTRA_DATA);
                                xmppConnection.sendPacket(to);
                                addFriend(roster,to);
                                break;
                            case Constant.ACTION_SEND_RECONNECT:
                                if (xmppConnection != null && xmppConnection.isConnectedAndAuth())
                                {
                                    Log.d(TAG,"connection is not null");
                                    if (!xmppConnection.isConnectedAndAuth()) {
                                        Log.d(TAG, "start connect");
                                        connect();
                                    }
                                }else {
                                    Log.d(TAG,"connection is null");
                                    sendMessage(intent);
                                }
                                break;
                        }
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_SEND_MESSAGE);
        filter.addAction(Constant.ACTION_SEND_SUBSCRIBE);
        filter.addAction(Constant.ACTION_SEND_RECONNECT);
        registerReceiver(uiThreadMessageReceiver,filter);
        Log.d(TAG,"register receiver");
    }

    private void prepareRoster(org.jivesoftware.smack.XMPPConnection connection)
    {
        try{
            roster = Roster.getInstanceFor(connection);
            if (!roster.isLoaded())
            {
                roster.reloadAndWait();
            }
            Collection<RosterEntry> entries = roster.getEntries();

            for (RosterEntry entry : entries)
            {
                DataManager.getInstance().addRosterEntry(entry);
                System.out.println("Here: " + entry);
            }

            roster.setSubscriptionMode(Roster.SubscriptionMode.manual);
            updateRosterBroadcast();
            DataManager.getInstance().addToPresencesManager(roster.getAvailablePresences(JidCreate.bareFrom(mName)));
            updatePresenceBroadcast();
            roster.addRosterListener(new RosterListener() {
                @Override
                public void entriesAdded(Collection<Jid> addresses) {

                }

                @Override
                public void entriesUpdated(Collection<Jid> addresses) {

                }

                @Override
                public void entriesDeleted(Collection<Jid> addresses) {

                }

                @Override
                public void presenceChanged(Presence presence) {

                }
            });
            roster.addSubscribeListener(new SubscribeListener() {
                @Override
                public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {
                    DataManager.getInstance().addToPresencesManager(subscribeRequest);
                    updatePresenceBroadcast();
                    return null;
                }
            });
            connection.addAsyncStanzaListener(new StanzaListener() {
                @Override
                public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
                    if (packet instanceof Presence)
                    {

                    }
                }
            },new StanzaTypeFilter(Presence.class));
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

    public void addFriend(Roster roster,String to){

        try {
            BareJid jid = JidCreate.bareFrom(to);
            roster.createEntry(jid,to, new String[]{"Friends"});
            Log.d(TAG,"添加好友成功！！");
        } catch (XmppStringprepException e) {
            e.printStackTrace();
            Log.d(TAG,"添加好友失败！！");
        }catch (SmackException.NotLoggedInException e)
        {
            e.printStackTrace();
        }catch (SmackException.NoResponseException e2)
        {
            e2.printStackTrace();
        }catch (XMPPException.XMPPErrorException e)
        {
            e.printStackTrace();
        }catch (SmackException.NotConnectedException e)
        {
            e.printStackTrace();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
