package com.oleksii.simplechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.objects.ListRoom;
import com.oleksii.simplechat.utils.Util;

import java.util.ArrayList;

public class RoomsListAdapter extends RecyclerView.Adapter<RoomsListAdapter.ViewHolder> {

    private ArrayList<ListRoom> list;
    private Context context;

    public RoomsListAdapter(ArrayList<ListRoom> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void submitAll(ArrayList<ListRoom> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListRoom obj = list.get(position);

        holder.logoView.addText(obj.getTitle());
        holder.title.setText(obj.getTitle());
        if (obj.getLastMessage() == null || obj.getLastActivity() == null) {
            holder.lastMessage.setText(R.string.no_messages_yet);
        } else {
            holder.whoSent.setText(obj.getFirstname() + ": ");
            holder.lastMessage.setText(obj.getLastMessage());
            holder.lastMessageTime.setText(Util.getTimeString(obj.getLastActivity(), false));
        }

        holder.mainLayout.setOnClickListener(v -> {
            // TODO
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, whoSent, lastMessage, lastMessageTime;
        LogoView logoView;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.user_name);
            whoSent = itemView.findViewById(R.id.who_sent);
            lastMessage = itemView.findViewById(R.id.last_seen_text);
            lastMessageTime = itemView.findViewById(R.id.date_time);
            logoView = itemView.findViewById(R.id.chat_logo);
            mainLayout = itemView.findViewById(R.id.main_layout);
        }

    }
}