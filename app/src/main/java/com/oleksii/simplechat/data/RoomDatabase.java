package com.oleksii.simplechat.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.oleksii.simplechat.data.converters.DateConverter;
import com.oleksii.simplechat.data.converters.TimestampConverter;
import com.oleksii.simplechat.data.entities.SavedMessage;
import com.oleksii.simplechat.models.Message;

@Database(entities = { Message.class }, version = 1, exportSchema = false)
@TypeConverters({ DateConverter.class, TimestampConverter.class })
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract SavedMessagesDao messagesDao();
    private static volatile RoomDatabase INSTANCE;

//    private static final int NUMBER_OF_THREADS = 4;
//    static final ExecutorService databaseWriteExecutor =
//            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabase.class, "saved_messages_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
