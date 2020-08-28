package com.oleksii.simplechat.factories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oleksii.simplechat.data.SavedMessagesDao;
import com.oleksii.simplechat.viewmodels.ExactRoomViewModel;

public class ExactRoomVMFactory implements ViewModelProvider.Factory {

    private long roomId;
    private String firstname;
    private String lastname;
    private Context context;
    private SavedMessagesDao dao;

    public ExactRoomVMFactory(long roomId, String firstname, String lastname, Context context,
                              SavedMessagesDao dao) {
        this.roomId = roomId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.context = context;
        this.dao = dao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(ExactRoomViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new ExactRoomViewModel(roomId, firstname, lastname, context, dao);
    }
}
