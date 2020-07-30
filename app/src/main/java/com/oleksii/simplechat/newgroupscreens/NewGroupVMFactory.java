package com.oleksii.simplechat.newgroupscreens;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.nkzawa.socketio.client.Socket;
import com.oleksii.simplechat.roomslistfragment.RoomsListViewModel;

public class NewGroupVMFactory implements ViewModelProvider.Factory {

    Socket socket;

    public NewGroupVMFactory(Socket socket) {
        this.socket = socket;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(NewGroupViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new NewGroupViewModel(socket);
    }
}
