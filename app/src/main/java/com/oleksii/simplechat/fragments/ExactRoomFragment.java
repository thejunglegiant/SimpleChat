package com.oleksii.simplechat.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oleksii.simplechat.activities.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.MessagesListAdapter;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.factories.ExactRoomVMFactory;
import com.oleksii.simplechat.viewmodels.ExactRoomViewModel;

import java.util.ArrayList;

public class ExactRoomFragment extends Fragment {

    private long roomId;
    private String roomTitle;
    private MainActivity parentActivity;

    public ExactRoomFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            roomId = getArguments().getLong("roomId");
            roomTitle = getArguments().getString("roomTitle");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exact_room, container, false);

        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v)
                .navigate(R.id.action_exactRoomFragment_to_chatsListFragment));
        TextView titleText = rootView.findViewById(R.id.room_title);
        titleText.setText(roomTitle);
        LogoView roomLogo = rootView.findViewById(R.id.room_logo);
        roomLogo.addText(roomTitle);

        RecyclerView messagesList = rootView.findViewById(R.id.messages_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);
        messagesList.setLayoutManager(layoutManager);
        MessagesListAdapter adapter = new MessagesListAdapter(new ArrayList<>(), getContext());
        messagesList.setAdapter(adapter);

        ExactRoomVMFactory factory = new ExactRoomVMFactory(roomId, parentActivity.getFirstname(),
                parentActivity.getLastname(), getContext());
        ExactRoomViewModel viewModel = new ViewModelProvider(this, factory)
                .get(ExactRoomViewModel.class);
        viewModel.messages.observe(getViewLifecycleOwner(), list -> {
            adapter.submitAll(list);
            messagesList.scrollToPosition(adapter.getItemCount() - 1);
        });

        ImageButton sendButton = rootView.findViewById(R.id.send_button);
        EditText messageEditText = rootView.findViewById(R.id.message_box);
        Animation appearAnim = AnimationUtils.loadAnimation(getContext(), R.anim.send_button_appear);
        Animation disappearAnim = AnimationUtils.loadAnimation(getContext(), R.anim.send_button_disappear);
        disappearAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sendButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && sendButton.getVisibility() == View.GONE) {
                    sendButton.startAnimation(appearAnim);
                    sendButton.setVisibility(View.VISIBLE);
                } else if (s.length() < 1) {
                    sendButton.startAnimation(disappearAnim);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        sendButton.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                viewModel.sendMessage(message);
                messageEditText.setText("");
            }
        });

        return rootView;
    }
}