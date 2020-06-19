package com.oleksii.simplechat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.objects.ChatObject;

import java.util.ArrayList;

public class RoomsListAdapter extends RecyclerView.Adapter<RoomsListAdapter.ViewHolder> {

    private ArrayList<ChatObject> list;
    private Context context;

    public RoomsListAdapter(ArrayList<ChatObject> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void submitAll(ArrayList<ChatObject> list) {
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
        ChatObject obj = list.get(position);

        holder.logoView.addText(obj.title);
        holder.title.setText(obj.title);
        holder.lastMessage.setText(obj.lastMessage.getBody());
        holder.lastMessageTime.setText(obj.lastMessage.getTime().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, lastMessage, lastMessageTime;
        LogoView logoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.chat_title);
            lastMessage = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.date_time);
            logoView = itemView.findViewById(R.id.chat_logo);
        }

    }
}