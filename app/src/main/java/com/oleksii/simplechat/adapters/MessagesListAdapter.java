package com.oleksii.simplechat.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.models.Message;
import com.oleksii.simplechat.utils.Util;

import java.util.LinkedList;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {

    private LinkedList<Message> mMessagesList = new LinkedList<>();

    public MessagesListAdapter() { }

    public void submitAll(LinkedList<Message> list) {
        mMessagesList.clear();
        mMessagesList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mMessagesList.get(position),
                position - 1 >= 0 ? mMessagesList.get(position - 1) : null,
                position + 1 < mMessagesList.size() ? mMessagesList.get(position + 1) : null);
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ConstraintLayout constraintLayout;
        LogoView senderLogo;
        TextView senderName, messageBody, messageSendingTime;
        Context context;
        Drawable userBox, strangerBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            senderLogo = itemView.findViewById(R.id.sender_logo);
            senderName = itemView.findViewById(R.id.sender_name);
            messageBody = itemView.findViewById(R.id.message_body);
            messageSendingTime = itemView.findViewById(R.id.sending_time);
            context = itemView.getContext();

            userBox = context.getDrawable(R.drawable.user_message_box);
            strangerBox = context.getDrawable(R.drawable.stranger_message_box);
        }

        public void bind(Message message, Message prevMessage, Message nextMessage) {
            if (message.isSender()) {
                constraintLayout.setBackground(userBox);
                linearLayout.setGravity(Gravity.END);
                senderName.setVisibility(View.GONE);
                messageBody.setTextColor(context.getColor(R.color.colorWhite));
            } else {
                constraintLayout.setBackground(strangerBox);
                linearLayout.setGravity(Gravity.START);
                senderName.setVisibility(View.VISIBLE);
                senderLogo.addText(message.getFirstname() + " " + message.getLastname());
                senderName.setText(message.getFirstname());
                messageBody.setTextColor(context.getColor(R.color.colorBlack));
            }

            if (prevMessage != null && prevMessage.getFirstname().equals(message.getFirstname())
                    || message.isSender()) {
                senderName.setVisibility(View.GONE);
            } else {
                senderName.setVisibility(View.VISIBLE);
            }

            if (nextMessage != null && nextMessage.getFirstname().equals(message.getFirstname())) {
                senderLogo.setVisibility(View.INVISIBLE);
                senderLogo.setDpHeight(0);
            } else {
                senderLogo.setDpHeight(51);
                senderLogo.setVisibility(View.VISIBLE);
            }

            if (message.isSender())
                senderLogo.setVisibility(View.GONE);

            messageBody.setText(message.getBody());
            messageSendingTime.setText(Util.getTimeString(message.getSendingTime(), false));
        }
    }
}