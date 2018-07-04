package com.tony.juetu.xmpp;

import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MucConfigFormManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by dev on 7/3/18.
 */

public class XmppChatManager implements IncomingChatMessageListener {

    public interface InComingMessageListener{
        void onNewMessage(Message msg);
    }

    private static final String TAG = XmppChatManager.class.getSimpleName();
    private static XmppChatManager sInstance;
    private ChatManager mChatManager;
    private MultiUserChatManager mMultiUserChatManager;
    private WeakReference<InComingMessageListener> listenerWeakReference;

    public static XmppChatManager getInstance() {
        if (sInstance == null)
        {
            synchronized (XmppChatManager.class)
            {
                if (sInstance == null)
                {
                    sInstance = new XmppChatManager();
                }
            }
        }
        return sInstance;
    }

    public void initManager(XMPPConnection connection)
    {
        if (mChatManager == null)
        {
            mChatManager = ChatManager.getInstanceFor(connection);
        }
        if (mMultiUserChatManager == null)
        {
            mMultiUserChatManager = MultiUserChatManager.getInstanceFor(connection);
        }
        mChatManager.addIncomingListener(this);
    }

    public void addNewMessageListener(InComingMessageListener listener)
    {
        listenerWeakReference = new WeakReference<InComingMessageListener>(listener);
    }

    public void sendMessage(String aMessage,String to)
    {
        EntityBareJid jid = null;
        try{
            jid = JidCreate.entityBareFrom(to);
            Chat chat = mChatManager.chatWith(jid);
            Message message = new Message(jid, Message.Type.chat);
            message.setBody(aMessage);
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

    public MultiUserChat createRoom(String roomName,String user,String password)throws SmackException.NoResponseException, XMPPException.XMPPErrorException,
            InterruptedException, MultiUserChatException.MucAlreadyJoinedException, SmackException.NotConnectedException,
            MultiUserChatException.NotAMucServiceException,XmppStringprepException,MultiUserChatException.MucConfigurationNotSupportedException
    {
        EntityBareJid jid = JidCreate.entityBareFrom(roomName);
        MultiUserChat multiUserChat = mMultiUserChatManager.getMultiUserChat(jid);
        MultiUserChat.MucCreateConfigFormHandle handle = multiUserChat.createOrJoin(Resourcepart.from(user));
        MucConfigFormManager manager = handle.getConfigFormManager();
        manager.makeMembersOnly();
        manager.makePasswordProtected();
        manager.setAndEnablePassword(password);
        manager.submitConfigurationForm();
        return multiUserChat;
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        Log.d(TAG,from.getDomain().toString());
        Log.d(TAG,message.getBody());
        Log.d(TAG,message.getTo().toString());
        Log.d(TAG,message.getFrom().toString());
        if (listenerWeakReference != null && listenerWeakReference.get() != null)
        {
            listenerWeakReference.get().onNewMessage(message);
        }
    }


}
