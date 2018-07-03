package com.tony.juetu.manager;

import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

/**
 * Created by dev on 7/3/18.
 */

public class HistoryMessage {
    private EntityBareJid jid;
    private Message message;

    public EntityBareJid getJid() {
        return jid;
    }

    public void setJid(EntityBareJid jid) {
        this.jid = jid;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
