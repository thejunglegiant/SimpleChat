package com.oleksii.simplechat.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.nkzawa.socketio.client.Socket;
import com.oleksii.simplechat.viewmodels.ExactRoomViewModel;

public class ExactRoomVMFactory implements ViewModelProvider.Factory {

    private long roomId;
    private String username;

    public ExactRoomVMFactory(long roomId, String username) {
        this.roomId = roomId;
        this.username = username;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(ExactRoomViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new ExactRoomViewModel(roomId, username);
    }
}
