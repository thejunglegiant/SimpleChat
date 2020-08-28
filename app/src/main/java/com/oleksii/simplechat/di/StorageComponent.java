package com.oleksii.simplechat.di;

import com.oleksii.simplechat.BaseApplication;
import com.oleksii.simplechat.data.RoomDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { LocalDataModule.class })
public interface StorageComponent {

    RoomDatabase getDatabase();

    void inject(BaseApplication application);
}
