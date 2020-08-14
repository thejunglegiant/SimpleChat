package com.oleksii.simplechat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.delegates.TickDelegate;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.models.User;
import com.oleksii.simplechat.utils.DateUtil;

import java.util.LinkedList;
import java.util.List;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> {

    private List<User> mUsersList = new LinkedList<>();
    private List<User> mCheckedUsers = new LinkedList<>();
    private TickDelegate delegate;
    private Boolean withTickIndicators = false;

    public void attachDelegate(TickDelegate delegate) {
        this.delegate = delegate;
    }

    public void enableTickIndicators(Boolean withTickIndicators) {
        this.withTickIndicators = withTickIndicators;
    }

    public void setCheckedUsers(List<User> list) {
        mCheckedUsers.clear();
        mCheckedUsers.addAll(list);
    }

    public UsersListAdapter() { }

    public void submitAll(List<User> list) {
        mUsersList.clear();
        mUsersList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v, delegate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mUsersList.get(position), withTickIndicators, mCheckedUsers);
    }

    @Override
    public int getItemCount() {
        return mUsersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, lastSeenTime;
        LogoView logoView;
        ImageView tickIndicator;
        ConstraintLayout actualLayout;
        TickDelegate delegate;

        public ViewHolder(@NonNull View itemView, TickDelegate delegate) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            lastSeenTime = itemView.findViewById(R.id.last_seen_text);
            logoView = itemView.findViewById(R.id.user_logo);
            tickIndicator = itemView.findViewById(R.id.tick_indicator);
            actualLayout = itemView.findViewById(R.id.actual_layout);

            this.delegate = delegate;
        }

        public void bind(User user, Boolean withTickIndicators, List<User> checkedUsers) {
            String nameStr = user.getFirstname() + " " + user.getLastname();
            String sessionStr = DateUtil.getDateString(user.getLastSession(), true);

            logoView.addText(nameStr);
            userName.setText(nameStr);
            lastSeenTime.setText(sessionStr);
            if (withTickIndicators) {
                // If the client wants to edit the list of users
                if (checkedUsers.contains(user))
                    tickIndicator.setVisibility(View.VISIBLE);
                else
                    tickIndicator.setVisibility(View.GONE);

                actualLayout.setOnClickListener(v -> {
                    if (tickIndicator.getVisibility() == View.VISIBLE) {
                        delegate.unCheckUser(user);
                        tickIndicator.setVisibility(View.GONE);
                    } else {
                        delegate.checkUser(user);
                        tickIndicator.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
    }
}