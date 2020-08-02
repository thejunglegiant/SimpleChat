package com.oleksii.simplechat.exactroomfragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.MainActivity;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.adapters.MessagesListAdapter;
import com.oleksii.simplechat.customviews.LogoView;
import com.oleksii.simplechat.models.Message;

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

        EditText messageBox = rootView.findViewById(R.id.message_box);

        RecyclerView messagesList = rootView.findViewById(R.id.messages_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        layoutManager.setSmoothScrollbarEnabled(false);
        messagesList.setLayoutManager(layoutManager);
        MessagesListAdapter adapter = new MessagesListAdapter(new ArrayList<>(), getContext());
        messagesList.setAdapter(adapter);

        ExactRoomVMFactory factory = new ExactRoomVMFactory(parentActivity.getSocket(), roomId, parentActivity.getName());
        ExactRoomViewModel viewModel = new ViewModelProvider(this, factory).get(ExactRoomViewModel.class);
        viewModel.messages.observe(getViewLifecycleOwner(), list -> {
            adapter.submitAll(list);
            messagesList.scrollToPosition(adapter.getItemCount() - 1);
        });

        Button sendButton = rootView.findViewById(R.id.send_button);
        sendButton.setOnClickListener(v -> {
            if (!messageBox.getText().toString().trim().isEmpty()) {
                Message message = new Message(FirebaseAuth.getInstance().getUid(),
                        roomId, messageBox.getText().toString());
                parentActivity.getSocket().emit("onNewMessageSent", new Gson().toJson(message));
                messageBox.setText("");
            } else {
                Snackbar.make(v, "Write something first!", Snackbar.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}