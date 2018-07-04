package com.tony.juetu.conversation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tony.juetu.R;
import com.tony.juetu.notification.NotificationAdapter;
import com.tony.juetu.xmpp.XmppChatManager;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

public class ContactFragment extends SupportFragment implements ContactView,ContactAdapter.OnClickListener,View.OnClickListener{
    public static ContactFragment getInstance()
    {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    private ContactAdapter adapter;
    private ContactPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list,container,false);
        initView(view);
        return view;
    }

    private void initView(View aRoot)
    {
        ImageView back = aRoot.findViewById(R.id.img_back);
        back.setVisibility(View.INVISIBLE);
        ImageView add = aRoot.findViewById(R.id.img_right);
        add.setOnClickListener(this);
        add.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = aRoot.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ContactAdapter(_mActivity);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);
        presenter = new ContactPresenter(this);
        presenter.initContact();
    }

    @Override
    public void updateContact(ArrayList<RosterEntry>entries) {
        if (adapter != null)
        {
            adapter.updateData(entries);
        }
    }

    @Override
    public void onClick(String toJid) {
        start(ChatFragment.getInstance(toJid));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_right)
        {
            new MaterialDialog.Builder(_mActivity)
                    .title(R.string.app_name)
                    .content(R.string.text_input_room_name)
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE)
                    .input(R.string.text_input_hint, R.string.text_input_prefill, new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            // Do something
                            try {
                                XmppChatManager.getInstance().createRoom(input.toString()+"@xmpp.jp", "tonyzhou", "123456");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).show();
        }
    }
}
