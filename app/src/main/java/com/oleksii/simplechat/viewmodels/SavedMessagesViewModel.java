package com.oleksii.simplechat.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.data.SavedMessagesDao;
import com.oleksii.simplechat.data.entities.SavedMessage;
import com.oleksii.simplechat.models.Message;

import java.util.Date;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SavedMessagesViewModel extends ViewModel {

    private static final String TAG = SavedMessagesViewModel.class.getName();
    private LiveData<List<Message>> messagesList;
    private SavedMessagesDao dao;

    public SavedMessagesViewModel(SavedMessagesDao dao) {
        this.dao = dao;

        init();
    }

    private void init() {
        updateMessagesData();
    }

    private void updateMessagesData() {
        messagesList = dao.getData();
    }

    public LiveData<List<Message>> getMessagesList() {
        return this.messagesList;
    }

    public void saveMessage(String message) {
        dao.insert(new Message(true, FirebaseAuth.getInstance().getUid(), message))
                .subscribeOn(Schedulers.newThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "New SavedMessage was added!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }
                });
    }
}
