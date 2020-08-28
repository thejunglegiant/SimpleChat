package com.oleksii.simplechat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.oleksii.simplechat.constants.NetworkConstants;
import com.oleksii.simplechat.di.DaggerStorageComponent;
import com.oleksii.simplechat.di.LocalDataModule;
import com.oleksii.simplechat.di.StorageComponent;

import io.reactivex.schedulers.Schedulers;

public class MyApplication extends Application implements BaseApplication {

    StorageComponent storageComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

//        Intent intent = new Intent(this, EventsService.class);
//        startService(intent);

        storageComponent = DaggerStorageComponent.builder()
                .localDataModule(new LocalDataModule(this))
                .build();

        // Just for test
//        storageComponent.getDatabase().messagesDao().deleteAll()
//                .subscribeOn(Schedulers.newThread())
//                .subscribe();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NetworkConstants.NEW_MESSAGES_CHANNEL_ID,
                    name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public StorageComponent getApplicationComponent() {
        return storageComponent;
    }
}
