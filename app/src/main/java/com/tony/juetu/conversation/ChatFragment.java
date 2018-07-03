package com.tony.juetu.conversation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shrikanthravi.chatview.data.Message;
import com.shrikanthravi.chatview.widget.ChatView;
import com.tony.juetu.App;
import com.tony.juetu.common.Constant;
import com.tony.juetu.R;
import com.tony.juetu.manager.ChatTimeHelper;
import com.tony.juetu.utils.Utils;
import com.tony.juetu.xmpp.XmppChatManager;


import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/28/18.
 */

public class ChatFragment extends SupportFragment implements View.OnClickListener,XmppChatManager.InComingMessageListener {


    public static ChatFragment getInstance(String to)
    {
        ChatFragment fragment = new ChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_DATA,to);
        fragment.setArguments(bundle);
        return fragment;
    }

    private ChatView chatView;
    private String toJid;

    private void init()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            toJid = bundle.getString(Constant.EXTRA_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        init();
        initView(view);
        return view;
    }

    private void initView(View aView)
    {
        ImageView back = aView.findViewById(R.id.img_back);
        back.setOnClickListener(this);
        chatView = aView.findViewById(R.id.chatView);
        XmppChatManager.getInstance().addNewMessageListener(this);
        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String s) {
                Message message = new Message();
                message.setType(Message.RightSimpleMessage);
                message.setBody(s); //string
                message.setTime(ChatTimeHelper.getInstance().getNewChatTime(ChatTimeHelper.getCurrentTime()));
                chatView.addMessage(message);
                XmppChatManager.getInstance().sendMessage(s,toJid);
            }
        });
    }

    @Override
    public void onClick(View v) {
        pop();
    }

    @Override
    public void onNewMessage(final org.jivesoftware.smack.packet.Message msg) {
        if (msg != null)
        {
            if (Utils.getLocalName(msg.getFrom().toString()).equals(toJid))
            {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        message.setType(Message.LeftSimpleMessage);
                        message.setBody(msg.getBody());
                        message.setTime(ChatTimeHelper.getInstance().getNewChatTime(ChatTimeHelper.getCurrentTime()));
                        chatView.addMessage(message);
                    }
                };
                App.execute(runnable);

            }
        }
    }
}
