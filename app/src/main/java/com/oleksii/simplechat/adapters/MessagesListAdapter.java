package com.oleksii.simplechat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.delegates.ClickDelegate;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.models.Message;
import com.oleksii.simplechat.utils.DateUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessagesList = new LinkedList<>();
    private List<Message> mCheckedMessagesList = new LinkedList<>();
    private ClickDelegate delegate;

    public void attachDelegate(ClickDelegate delegate) {
        this.delegate = delegate;
    }

    public MessagesListAdapter() { }

    public void submitAll(List<Message> list) {
        mMessagesList.clear();
        mMessagesList.addAll(list);
        notifyDataSetChanged();
    }

    public void unCheckAllMessages() {
        for (Message item : mCheckedMessagesList) {
            notifyItemChanged(mMessagesList.indexOf(item));
        }
        mCheckedMessagesList.clear();
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
                        .inflate(R.layout.message_item, parent, false), delegate);
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
                ((RegularViewHolder) holder).bind(mCheckedMessagesList, mMessagesList.get(position),
                        position - 1 >= 0 ? mMessagesList.get(position - 1) : null,
                        position + 1 < mMessagesList.size() ? mMessagesList.get(position + 1) : null);
                break;
            case 1:
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
        ClickDelegate delegate;

        public RegularViewHolder(@NonNull View itemView, ClickDelegate delegate) {
            super(itemView);


            linearLayout = itemView.findViewById(R.id.linearLayout);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            header = itemView.findViewById(R.id.date_header);
            senderLogo = itemView.findViewById(R.id.sender_logo);
            senderName = itemView.findViewById(R.id.sender_name);
            messageBody = itemView.findViewById(R.id.message_body);
            messageSendingTime = itemView.findViewById(R.id.sending_time);
            context = itemView.getContext();
            this.delegate = delegate;

            userBox = ContextCompat.getDrawable(context, R.drawable.user_message_box);
            strangerBox = ContextCompat.getDrawable(context, R.drawable.stranger_message_box);
        }

        @SuppressLint("ClickableViewAccessibility")
        public void bind(List<Message> checkedMessages,
                         Message message, Message prevMessage, Message nextMessage) {
            if (!checkedMessages.contains(message)) {
                linearLayout.setBackgroundColor(Color.TRANSPARENT);
            }

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
                senderLogo.setText(message.getFirstname() + " " + message.getLastname());
                senderName.setText(message.getFirstname());
                messageBody.setTextColor(context.getColor(R.color.colorBlack));
            }

            if (prevMessage != null && prevMessage.getUserId().equals(message.getUserId())
                    || message.isSender()) {
                senderName.setVisibility(View.GONE);
            } else {
                senderName.setVisibility(View.VISIBLE);
            }

            if (nextMessage != null && nextMessage.getUserId().equals(message.getUserId())
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

            linearLayout.setOnTouchListener(new View.OnTouchListener() {
                private Timer timer = new Timer();
                private long LONG_PRESS_TIMEOUT = 700;
                private boolean wasLong = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                wasLong = true;
                            }
                        }, LONG_PRESS_TIMEOUT);
                        return true;
                    }

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        timer.cancel();
                        if (wasLong) {
                            if (checkedMessages.size() < 1) {
                                linearLayout.setBackgroundColor(context.getColor(
                                        R.color.colorTransparentPrimary));
                                checkedMessages.add(message);
                                delegate.onLongClickEvent(message);
                            }
                        } else {
                            if (checkedMessages.size() > 0 && !checkedMessages.contains(message)) {
                                linearLayout.setBackgroundColor(context.getColor(
                                        R.color.colorTransparentPrimary));
                                checkedMessages.add(message);
                                delegate.onShortClickEvent(message);
                            } else if (checkedMessages.contains(message)) {
                                linearLayout.setBackgroundColor(Color.TRANSPARENT);
                                checkedMessages.remove(message);
                                delegate.onSecondShortClickEvent(message);
                            }
                        }
                        timer = new Timer();
                        wasLong = false;
                        return true;
                    }

                    return false;
                }
            });
        }
    }
}