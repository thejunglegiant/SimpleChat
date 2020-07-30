package com.oleksii.simplechat.adapters;

import android.content.Context;
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
import com.oleksii.simplechat.objects.Message;
import com.oleksii.simplechat.utils.Util;

import java.util.ArrayList;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {

    private ArrayList<Message> list;
    private Context context;

    public MessagesListAdapter(ArrayList<Message> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void submitAll(ArrayList<Message> list) {
        this.list = list;
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
        Message obj = list.get(position);

        if (obj.isSender()) {
            holder.constraintLayout.setBackground(context.getDrawable(R.drawable.user_message_box));
            holder.linearLayout.setGravity(Gravity.END);
            holder.senderName.setVisibility(View.GONE);
            holder.messageBody.setTextColor(context.getColor(R.color.colorWhite));
        } else {
            holder.constraintLayout.setBackground(context.getDrawable(R.drawable.stranger_message_box));
            holder.linearLayout.setGravity(Gravity.START);
            holder.senderName.setVisibility(View.VISIBLE);
            holder.senderLogo.addText(obj.getFirstname() + " " + obj.getLastname());
            holder.senderName.setText(obj.getFirstname());
            holder.messageBody.setTextColor(context.getColor(R.color.colorBlack));
        }

        if (position - 1 >= 0 && list.get(position - 1).getFirstname().equals(obj.getFirstname())
                || obj.isSender()) {
            holder.senderName.setVisibility(View.GONE);
        } else {
            holder.senderName.setVisibility(View.VISIBLE);
        }

        if (position + 1 < list.size() && list.get(position + 1).getFirstname().equals(obj.getFirstname())) {
            holder.senderLogo.setVisibility(View.INVISIBLE);
            holder.senderLogo.setDpHeight(0);
        } else {
            holder.senderLogo.setDpHeight(51);
            holder.senderLogo.setVisibility(View.VISIBLE);
        }

        if (obj.isSender())
            holder.senderLogo.setVisibility(View.GONE);

        holder.messageBody.setText(obj.getBody());
        holder.messageSendingTime.setText(Util.getTimeString(obj.getSendingTime(), false));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ConstraintLayout constraintLayout;
        LogoView senderLogo;
        TextView senderName, messageBody, messageSendingTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayout);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            senderLogo = itemView.findViewById(R.id.sender_logo);
            senderName = itemView.findViewById(R.id.sender_name);
            messageBody = itemView.findViewById(R.id.message_body);
            messageSendingTime = itemView.findViewById(R.id.sending_time);
        }

    }
}