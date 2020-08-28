package com.oleksii.simplechat.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.oleksii.simplechat.data.entities.SavedMessage;
import com.oleksii.simplechat.models.Message;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface SavedMessagesDao {

    @Insert
    Completable insert(Message message);

    @Query("DELETE FROM saved_messages_table")
    Completable deleteAll();

    @Query("SELECT * FROM saved_messages_table ORDER BY stime")
    LiveData<List<Message>> getData();
}
