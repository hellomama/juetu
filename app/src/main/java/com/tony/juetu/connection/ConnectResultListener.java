package com.tony.juetu.connection;

/**
 * Created by dev on 6/28/18.
 */

public interface ConnectResultListener {
    void Connected(org.jivesoftware.smack.XMPPConnection connection);
    void ConnectFail();
}
