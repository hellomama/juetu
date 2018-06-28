package com.tony.juetu.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tony.juetu.Common.Constant;
import com.tony.juetu.R;


import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/28/18.
 */

public class ChatFragment extends SupportFragment implements View.OnClickListener{


    public static ChatFragment getInstance()
    {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        initView(view);
        return view;
    }

    private void initView(View aView)
    {
        Button sendMsg = aView.findViewById(R.id.btn_chat);
        sendMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Constant.ACTION_SEND_MESSAGE);
        _mActivity.sendBroadcast(intent);
    }
}
