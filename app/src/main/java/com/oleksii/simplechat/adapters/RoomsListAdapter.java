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
import com.oleksii.simplechat.utils.DateUtil;

import java.util.LinkedList;
import java.util.List;

public class RoomsListAdapter extends RecyclerView.Adapter<RoomsListAdapter.ViewHolder> {

    private List<ListRoom> mRoomsList = new LinkedList<>();

    public RoomsListAdapter() { }

    public void submitAll(List<ListRoom> list) {
        mRoomsList.clear();
        mRoomsList.addAll(list);
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
        holder.bind(mRoomsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mRoomsList.size();
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

        void bind(ListRoom listRoom) {
            logoView.addText(listRoom.getTitle());
            title.setText(listRoom.getTitle());
            if (listRoom.getLastMessage() == null || listRoom.getLastActivity() == null) {
                whoSent.setVisibility(View.GONE);
                lastMessageTime.setVisibility(View.GONE);
                lastMessage.setText(R.string.no_messages_yet);
            } else {
                whoSent.setVisibility(View.VISIBLE);
                lastMessageTime.setVisibility(View.VISIBLE);
                whoSent.setText(listRoom.getFirstname() + ": ");
                lastMessage.setText(listRoom.getLastMessage());
                lastMessageTime.setText(DateUtil.getDateString(listRoom.getLastActivity(), false));
            }

            mainLayout.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("roomId", listRoom.getId());
                bundle.putString("roomTitle", listRoom.getTitle());
                Navigation.findNavController(v).navigate(
                        R.id.action_chatsListFragment_to_exactRoomFragment, bundle);
            });
        }

    }
}