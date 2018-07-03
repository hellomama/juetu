package com.tony.juetu.notification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tony.juetu.R;

import org.jivesoftware.smack.packet.Presence;
import org.jxmpp.jid.Jid;

import java.util.ArrayList;

import static com.tony.juetu.common.Constant.ACTION_SEND_SUBSCRIBE;
import static com.tony.juetu.common.Constant.EXTRA_DATA;

/**
 * Created by dev on 6/28/18.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<Presence> presences = new ArrayList<>();

    public NotificationAdapter(Context aContext) {
        mContext = aContext;
        inflater = LayoutInflater.from(mContext);
    }

    public void updateData(ArrayList<Presence> presence)
    {
        if (presence != null)
        {
            presences.clear();
            presences.addAll(presence);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification,parent,false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        final Presence presence = presences.get(position);
        Jid jid = presence.getFrom();
        holder.name.setText(jid.toString());
        holder.message.setText(presence.getType().name());
        holder.status.setText(presence.getStatus());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presence.getType().equals(Presence.Type.subscribe))
                {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_SEND_SUBSCRIBE);
                    intent.putExtra(EXTRA_DATA,presence.getFrom().toString());
                    v.getContext().sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return presences.size();
    }

    static class NotificationHolder extends RecyclerView.ViewHolder
    {
        ImageView avatar;
        TextView name,status,message,time;
        View parent;
        public NotificationHolder(View itemView) {
            super(itemView);
            parent = itemView;
            avatar = itemView.findViewById(R.id.img_avatar);
            name = itemView.findViewById(R.id.text_name);
            status = itemView.findViewById(R.id.text_status);
            message = itemView.findViewById(R.id.text_message);
            time = itemView.findViewById(R.id.text_time);
        }
    }
}
