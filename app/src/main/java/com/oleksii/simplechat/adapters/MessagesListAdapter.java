package com.oleksii.simplechat.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.models.Message;
import com.oleksii.simplechat.utils.DateUtil;
import com.oleksii.simplechat.viewmodels.ExactRoomViewModel;

import java.util.LinkedList;
import java.util.List;

public class MessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessagesList = new LinkedList<>();

    public MessagesListAdapter() { }

    public void submitAll(List<Message> list) {
        mMessagesList.clear();
        mMessagesList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessagesList.get(position).getViewType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new RegularViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_item, parent, false));
            case 1:
                return new RoomActionViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_action_item, parent, false));
            default:
                // Temporary
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                // Temporary
                ((RegularViewHolder) holder).bind(mMessagesList.get(position),
                        position - 1 >= 0 ? mMessagesList.get(position - 1) : null,
                        position + 1 < mMessagesList.size() ? mMessagesList.get(position + 1) : null);
                break;
            case 1:
                // Temporary (need interfaces)
                ((RoomActionViewHolder) holder).bind(mMessagesList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    public static class RoomActionViewHolder extends RecyclerView.ViewHolder {
        TextView actionText;
        Context context;

        public RoomActionViewHolder(@NonNull View itemView) {
            super(itemView);

            actionText = itemView.findViewById(R.id.action_text);
            context = itemView.getContext();
        }

        public void bind(Message item) {
            String str = item.getFirstname() + " " + item.getLastname() + " "
                    + context.getString(R.string.left_the_group);
            actionText.setText(str);
        }
    }

    public static class RegularViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        ConstraintLayout constraintLayout;
        LogoView senderLogo;
        TextView header, senderName, messageBody, messageSendingTime;
        Context context;
        Drawable userBox, strangerBox;

        public RegularViewHolder(@NonNull View itemView) {
            super(itemView);


            linearLayout = itemView.findViewById(R.id.linearLayout);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            header = itemView.findViewById(R.id.date_header);
            senderLogo = itemView.findViewById(R.id.sender_logo);
            senderName = itemView.findViewById(R.id.sender_name);
            messageBody = itemView.findViewById(R.id.message_body);
            messageSendingTime = itemView.findViewById(R.id.sending_time);
            context = itemView.getContext();

            userBox = ContextCompat.getDrawable(context, R.drawable.user_message_box);
            strangerBox = ContextCompat.getDrawable(context, R.drawable.stranger_message_box);
        }

        public void bind(Message message, Message prevMessage, Message nextMessage) {
            if (prevMessage == null || DateUtil.isNewDay(prevMessage.getSendingTime(), message.getSendingTime())) {
                header.setText(DateUtil.getDayMonthString(message.getSendingTime()));
                header.setVisibility(View.VISIBLE);
            } else {
                header.setVisibility(View.GONE);
            }

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

            if (nextMessage != null && nextMessage.getFirstname().equals(message.getFirstname())
                    && nextMessage.getViewType() == 0) {
                senderLogo.setVisibility(View.INVISIBLE);
                senderLogo.setDpHeight(0);
            } else {
                senderLogo.setDpHeight(51);
                senderLogo.setVisibility(View.VISIBLE);
            }

            if (message.isSender())
                senderLogo.setVisibility(View.GONE);

            messageBody.setText(message.getBody());
            messageSendingTime.setText(DateUtil.getTimeString(message.getSendingTime()));
        }
    }
}