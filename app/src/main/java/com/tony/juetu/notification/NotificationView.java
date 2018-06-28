package com.tony.juetu.notification;

import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;

/**
 * Created by dev on 6/28/18.
 */

public interface NotificationView {
    void updateNotification(ArrayList<Presence> presences);
}
