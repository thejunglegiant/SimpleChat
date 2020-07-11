package com.oleksii.simplechat.exactroomfragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oleksii.simplechat.ChatApplication;

public class ExactRoomVMFactory implements ViewModelProvider.Factory {

    private ChatApplication app;
    private long roomId;
    private String username;

    public ExactRoomVMFactory(ChatApplication app, long roomId, String username) {
        this.app = app;
        this.roomId = roomId;
        this.username = username;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(ExactRoomViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new ExactRoomViewModel(app, roomId, username);
    }
}
