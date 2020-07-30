package com.oleksii.simplechat.roomslistfragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.nkzawa.socketio.client.Socket;

public class RoomListVMFactory implements ViewModelProvider.Factory {

    Socket socket;

    public RoomListVMFactory(Socket socket) {
        this.socket = socket;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(RoomsListViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new RoomsListViewModel(socket);
    }
}
