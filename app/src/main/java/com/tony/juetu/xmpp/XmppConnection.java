package com.tony.juetu.xmpp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;


import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;


public class XmppConnection implements ConnectionListener {

    private final static String TAG = XmppConnection.class.getSimpleName();

    private Context mContext;
    private String mName,mPassword;
    private XMPPTCPConnection connection;
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
        builder.setResource("掘土");

        connection = new XMPPTCPConnection(builder.build());
        connection.addConnectionListener(this);
        connection.connect();
        Log.d(TAG,"connecting from serser xmpp.jp");
        ReconnectionManager manager = ReconnectionManager.getInstanceFor(connection);
        ReconnectionManager.setEnabledPerDefault(true);
        manager.enableAutomaticReconnection();
    }

    public void disconnect()
    {
        Log.d(TAG,"disconnecting from serser xmpp.jp");
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
    }

    public boolean isConnectedAndAuth()
    {
        return (connection != null && connection.isConnected() && connection.isAuthenticated());
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
}
