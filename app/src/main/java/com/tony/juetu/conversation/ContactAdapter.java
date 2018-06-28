package com.tony.juetu.conversation;

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
import com.tony.juetu.notification.NotificationAdapter;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.Jid;

import java.util.ArrayList;

import static com.tony.juetu.Common.Constant.ACTION_SEND_MESSAGE;
import static com.tony.juetu.Common.Constant.ACTION_SEND_SUBSCRIBE;
import static com.tony.juetu.Common.Constant.EXTRA_DATA;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<RosterEntry> entries = new ArrayList<>();

    public ContactAdapter(Context aContext) {
        mContext = aContext;
        inflater = LayoutInflater.from(mContext);
    }

    public void updateData(ArrayList<RosterEntry>aEntries)
    {
        if (entries != null)
        {
            entries.clear();
            entries.addAll(aEntries);
            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public ContactAdapter.ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notification,parent,false);
        return new ContactAdapter.ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactHolder holder, int position) {
        final RosterEntry entry = entries.get(position);
        Jid jid = entry.getJid();
        holder.name.setText(jid.toString());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_SEND_MESSAGE);
                    intent.putExtra(EXTRA_DATA,entry.getJid().toString());
                    v.getContext().sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class ContactHolder extends RecyclerView.ViewHolder
    {
        ImageView avatar;
        TextView name,status,message,time;
        View parent;
        public ContactHolder(View itemView) {
            super(itemView);
            parent = itemView;
            avatar = itemView.findViewById(R.id.img_avatar);
            name = itemView.findViewById(R.id.text_name);
            status = itemView.findViewById(R.id.text_status);
            message = itemView.findViewById(R.id.text_message);
            time = itemView.findViewById(R.id.text_time);
        }
    }

    public interface onClickListener{
        void onClick();
    }
}
