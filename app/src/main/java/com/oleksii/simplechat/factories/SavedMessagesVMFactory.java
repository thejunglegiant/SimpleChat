package com.oleksii.simplechat.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.oleksii.simplechat.data.SavedMessagesDao;
import com.oleksii.simplechat.viewmodels.SavedMessagesViewModel;

public class SavedMessagesVMFactory implements ViewModelProvider.Factory {

    private SavedMessagesDao dao;

    public SavedMessagesVMFactory(SavedMessagesDao dao) {
        this.dao = dao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.isAssignableFrom(SavedMessagesViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

        return (T) new SavedMessagesViewModel(dao);
    }
}
