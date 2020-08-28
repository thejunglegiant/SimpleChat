package com.oleksii.simplechat.data.repository;

import android.app.Application;
import android.util.Log;

import com.oleksii.simplechat.data.RoomDatabase;
import com.oleksii.simplechat.data.SavedMessagesDao;
import com.oleksii.simplechat.models.Message;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class MessagesRepository {
    private SavedMessagesDao messagesDao;

    public MessagesRepository(Application application) {
        RoomDatabase roomDatabase = RoomDatabase.getDatabase(application.getApplicationContext());
        messagesDao = roomDatabase.messagesDao();
//        messagesDao.getData()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<Message>>() {
//                    @Override
//                    public void accept(List<Message> messages) throws Exception {
//                        Log.i(MessagesRepository.class.getName(), messages.get(0).getFirstname());
//                    }
//                });
    }
}
