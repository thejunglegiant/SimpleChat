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

import java.util.ArrayList;

public class PeopleListAdapter extends RecyclerView.Adapter<PeopleListAdapter.ViewHolder> {

    private ArrayList<User> list;
    private NewGroupViewModel viewModel;
    private Boolean withTickIndicators;

    public PeopleListAdapter(ArrayList<User> list, NewGroupViewModel viewModel) {
        this.list = list;
        this.viewModel = viewModel;
        this.withTickIndicators = true;
    }

    public PeopleListAdapter(ArrayList<User> list, NewGroupViewModel viewModel,
                             Boolean withTickIndicators) {
        this.list = list;
        this.viewModel = viewModel;
        this.withTickIndicators = withTickIndicators;
    }

    public void submitAll(ArrayList<User> list) {
        this.list = list;
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
        User obj = list.get(position);
        String nameStr = obj.getFirstname() + " " + obj.getLastname();
        String sessionStr = Util.getTimeString(obj.getLastSession(), true);

        holder.logoView.addText(nameStr);
        holder.userName.setText(nameStr);
        holder.lastSeenTime.setText(sessionStr);
        if (withTickIndicators) {
            holder.actualLayout.setOnClickListener(v -> {
                if (holder.tickIndicator.getVisibility() == View.VISIBLE) {
                    holder.tickIndicator.setVisibility(View.GONE);
                    viewModel.removeCheckedUser(obj);
                } else {
                    holder.tickIndicator.setVisibility(View.VISIBLE);
                    viewModel.addCheckedUser(obj);
                }
            });

            if (viewModel.getCheckedUsers().contains(obj)) {
                holder.tickIndicator.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
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

    }
}