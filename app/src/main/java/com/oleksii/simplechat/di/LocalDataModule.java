package com.oleksii.simplechat.di;

import android.app.Application;

import androidx.room.Room;

import com.oleksii.simplechat.data.repository.MessagesRepository;
import com.oleksii.simplechat.data.RoomDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalDataModule {

    Application application;

    public LocalDataModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    RoomDatabase provideRoomDatabase() {
        return Room.databaseBuilder(
                application.getApplicationContext(),
                RoomDatabase.class,
                "Simple.db")
                .build();
    }

    @Provides
    @Singleton
    MessagesRepository provideMessagesRepository() {
        return new MessagesRepository(application);
    }
}
