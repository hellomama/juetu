package com.tony.juetu.connection;

import android.content.Context;
import android.util.Log;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class XmppConnection implements ConnectionListener {

    private final static String TAG = XmppConnection.class.getSimpleName();

    private Context mContext;
    private String mName,mPassword;
    private XMPPTCPConnection connection;

    public XmppConnection(Context context,String name,String pwd) {
        mContext = context.getApplicationContext();
        mName = name;
        mPassword = pwd;
    }

    public void connect()throws IOException,SmackException,XMPPException,InterruptedException
    {
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setXmppDomain("xmpp.jp");
        builder.setUsernameAndPassword(mName,mPassword);
        builder.setResource("xmpp");

        connection = new XMPPTCPConnection(builder.build());
        connection.addConnectionListener(this);
        connection.connect();
        connection.login();

        ReconnectionManager manager = ReconnectionManager.getInstanceFor(connection);
        ReconnectionManager.setEnabledPerDefault(true);
        manager.enableAutomaticReconnection();
    }

    public void disconnect()
    {
        Log.d(TAG,"Disconnecting from serser xmpp.jp");
        if (connection != null) {
            connection.disconnect();
        }
    }

    @Override
    public void reconnectionSuccessful() {

    }

    @Override
    public void reconnectingIn(int seconds) {

    }

    @Override
    public void reconnectionFailed(Exception e) {

    }

    @Override
    public void connected(org.jivesoftware.smack.XMPPConnection connection) {

    }

    @Override
    public void authenticated(org.jivesoftware.smack.XMPPConnection connection, boolean resumed) {

    }

    @Override
    public void connectionClosed() {

    }

    @Override
    public void connectionClosedOnError(Exception e) {

    }
}
