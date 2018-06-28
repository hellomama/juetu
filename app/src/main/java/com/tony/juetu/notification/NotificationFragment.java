package com.tony.juetu.notification;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tony.juetu.R;

import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by dev on 6/28/18.
 */

public class NotificationFragment extends SupportFragment implements NotificationView{

    public static NotificationFragment getInstance()
    {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }

    private NotificationAdapter adapter;
    private NotificationPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list,container,false);
        initView(view);
        return view;
    }

    private void initView(View aRoot)
    {
        presenter = new NotificationPresenter(this);
        presenter.initNotification();

        RecyclerView recyclerView = aRoot.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new NotificationAdapter(_mActivity);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void updateNotification(ArrayList<Presence> presences) {
        if (adapter != null)
        {
            adapter.updateData(presences);
        }
    }
}
