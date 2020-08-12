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
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.viewmodels.NewGroupViewModel;
import com.oleksii.simplechat.models.User;
import com.oleksii.simplechat.utils.Util;

import java.util.LinkedList;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> {

    private LinkedList<User> mUsersList = new LinkedList<>();
    private NewGroupViewModel viewModel;
    private Boolean withTickIndicators;

    public UsersListAdapter(NewGroupViewModel viewModel) {
        this.viewModel = viewModel;
        this.withTickIndicators = true;
    }

    public UsersListAdapter(NewGroupViewModel viewModel, Boolean withTickIndicators) {
        this.viewModel = viewModel;
        this.withTickIndicators = withTickIndicators;
    }

    public void submitAll(LinkedList<User> list) {
        mUsersList.clear();
        mUsersList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mUsersList.get(position), withTickIndicators, viewModel);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.user_name);
            lastSeenTime = itemView.findViewById(R.id.last_seen_text);
            logoView = itemView.findViewById(R.id.user_logo);
            tickIndicator = itemView.findViewById(R.id.tick_indicator);
            actualLayout = itemView.findViewById(R.id.actual_layout);
        }

        public void bind(User user, Boolean withTickIndicators, NewGroupViewModel viewModel) {
            String nameStr = user.getFirstname() + " " + user.getLastname();
            String sessionStr = Util.getTimeString(user.getLastSession(), true);

            logoView.addText(nameStr);
            userName.setText(nameStr);
            lastSeenTime.setText(sessionStr);
            if (withTickIndicators) {
                actualLayout.setOnClickListener(v -> {
                    if (tickIndicator.getVisibility() == View.VISIBLE) {
                        tickIndicator.setVisibility(View.GONE);
                        viewModel.removeCheckedUser(user);
                    } else {
                        tickIndicator.setVisibility(View.VISIBLE);
                        viewModel.addCheckedUser(user);
                    }
                });

                if (viewModel.getCheckedUsers().contains(user)) {
                    tickIndicator.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}