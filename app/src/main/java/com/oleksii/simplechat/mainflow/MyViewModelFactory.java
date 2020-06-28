package com.oleksii.simplechat.mainflow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oleksii.simplechat.ChatApplication;

public class MyViewModelFactory implements ViewModelProvider.Factory {

    ChatApplication app;

    public MyViewModelFactory(ChatApplication app) {
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(RoomsListViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new RoomsListViewModel(app);
    }
}
