package com.tony.juetu.connection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tony.juetu.manager.DataManager;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.Collection;

import static com.tony.juetu.Common.Constant.ACTION_SEND_MESSAGE;

public class XmppConnection implements ConnectionListener ,IncomingChatMessageListener {

    private final static String TAG = XmppConnection.class.getSimpleName();

    private Context mContext;
    private String mName,mPassword;
    private XMPPTCPConnection connection;
    private ChatManager mChatManager;
    private BroadcastReceiver uiThreadMessageReceiver;
    private ConnectResultListener listener;

    public XmppConnection(Context context,String name,String pwd) {
        mContext = context.getApplicationContext();
        mName = name;
        mPassword = pwd;
    }

    public void connect(ConnectResultListener listener)throws IOException,SmackException,XMPPException,InterruptedException
    {
        this.listener = listener;
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setXmppDomain("xmpp.jp");
        builder.setUsernameAndPassword(mName,mPassword);
        builder.setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible);
        builder.setResource("Work");

        connection = new XMPPTCPConnection(builder.build());
        connection.addConnectionListener(this);
        connection.connect();
        Log.d(TAG,"connecting from serser xmpp.jp");
        ReconnectionManager manager = ReconnectionManager.getInstanceFor(connection);
        ReconnectionManager.setEnabledPerDefault(true);
        manager.enableAutomaticReconnection();
        ChatManager.getInstanceFor(connection).addIncomingListener(this);
    }

    public void disconnect()
    {
        Log.d(TAG,"disconnecting from serser xmpp.jp");
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
        mContext.unregisterReceiver(uiThreadMessageReceiver);
    }

    public void sendMessage()
    {
        if (mChatManager == null)
        {
            mChatManager = ChatManager.getInstanceFor(connection);
        }

        EntityBareJid jid = null;
        try{
            jid = JidCreate.entityBareFrom("tonywu@xmpp.jp");
            Chat chat = mChatManager.chatWith(jid);
            Message message = new Message(jid, Message.Type.chat);
            message.setBody("hello xmpp");
            chat.send(message);
        }catch (XmppStringprepException e)
        {
            e.printStackTrace();
        }catch (SmackException.NotConnectedException e)
        {
            e.fillInStackTrace();
        }catch (InterruptedException e)
        {
            e.fillInStackTrace();
        }
    }

    public void sendPacket(@Nullable String to)
    {
        Presence presenceRes = new Presence(Presence.Type.subscribed);
        try {
            Jid jid = JidCreate.bareFrom(to);
            presenceRes.setTo(jid);
            connection.sendStanza(presenceRes);
        }catch (SmackException.NotConnectedException e)
        {
            e.fillInStackTrace();
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }catch (XmppStringprepException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d(TAG,"reconnectionSuccessful from serser xmpp.jp");

    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d(TAG,"reconnectingIn from serser xmpp.jp");

    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d(TAG,"reconnectionFailed from serser xmpp.jp");

    }

    @Override
    public void connected(org.jivesoftware.smack.XMPPConnection connection) {
        Log.d(TAG,"connected from serser xmpp.jp");
        try {
            this.connection.login();
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

    @Override
    public void authenticated(org.jivesoftware.smack.XMPPConnection connection, boolean resumed) {
        Log.d(TAG,"authenticated from serser xmpp.jp");
        if (listener != null)
        {
            listener.Connected(connection);
        }
    }

    @Override
    public void connectionClosed() {
        Log.d(TAG,"connectionClosed from serser xmpp.jp");

    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d(TAG,"connectionClosedOnError from serser xmpp.jp");
        if (listener != null)
        {
            listener.ConnectFail();
        }
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        Log.d(TAG,from.getDomain().toString());
        Log.d(TAG,message.getBody());
    }
}
