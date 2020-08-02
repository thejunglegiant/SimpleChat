package com.oleksii.simplechat.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.models.ListRoom;
import com.oleksii.simplechat.utils.Util;

import java.util.ArrayList;

public class RoomsListAdapter extends RecyclerView.Adapter<RoomsListAdapter.ViewHolder> {

    private ArrayList<ListRoom> list;

    public RoomsListAdapter(ArrayList<ListRoom> list) {
        this.list = list;
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
            holder.whoSent.setVisibility(View.GONE);
            holder.lastMessageTime.setVisibility(View.GONE);
            holder.lastMessage.setText(R.string.no_messages_yet);
        } else {
            holder.whoSent.setVisibility(View.VISIBLE);
            holder.lastMessageTime.setVisibility(View.VISIBLE);
            holder.whoSent.setText(obj.getFirstname() + ": ");
            holder.lastMessage.setText(obj.getLastMessage());
            holder.lastMessageTime.setText(Util.getTimeString(obj.getLastActivity(), false));
        }

        holder.mainLayout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("roomId", obj.getId());
            bundle.putString("roomTitle", obj.getTitle());
            Navigation.findNavController(v).navigate(
                    R.id.action_chatsListFragment_to_exactRoomFragment, bundle
            );
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
            logoView = itemView.findViewById(R.id.room_logo);
            mainLayout = itemView.findViewById(R.id.main_layout);
        }

    }
}