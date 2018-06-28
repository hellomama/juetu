package com.tony.juetu.conversation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tony.juetu.R;
import com.tony.juetu.notification.NotificationAdapter;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

public class ContactFragment extends SupportFragment implements ContactView{
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
        RecyclerView recyclerView = aRoot.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ContactAdapter(_mActivity);
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
}
