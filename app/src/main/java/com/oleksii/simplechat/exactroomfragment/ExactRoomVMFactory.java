package com.oleksii.simplechat.exactroomfragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.nkzawa.socketio.client.Socket;

public class ExactRoomVMFactory implements ViewModelProvider.Factory {

    private Socket socket;
    private long roomId;
    private String username;

    public ExactRoomVMFactory(Socket socket, long roomId, String username) {
        this.socket = socket;
        this.roomId = roomId;
        this.username = username;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(ExactRoomViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new ExactRoomViewModel(socket, roomId, username);
    }
}
