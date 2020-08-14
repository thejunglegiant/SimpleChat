package com.oleksii.simplechat.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.R;
import com.oleksii.simplechat.activities.MainActivity;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.models.User;
import com.oleksii.simplechat.constants.NetworkConstants;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class EventsService extends Service {

    @Inject Socket mSocket;
    private PendingIntent onListScreenPending;
    private static String firstname, lastname;
    private final String TAG = EventsService.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();

        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        onListScreenPending = PendingIntent.getActivity(this, 0, intent, 0);

        mSocket.emit(NetworkConstants.SYNC_EVENT_ID, new Gson()
                .toJson(new User(FirebaseAuth.getInstance().getUid(), firstname, lastname)));
        mSocket.on(NetworkConstants.SYNCED_EVENT_ID, synced);
        mSocket.on(NetworkConstants.RECONNECT_EVENT_ID, onReconnectEvent);
        mSocket.on(NetworkConstants.NEW_MESSAGE_EVENT_ID, onNewMessageReceived);
    }

    private Emitter.Listener synced = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            try {
                firstname = data.getString("firstname");
                lastname = data.getString("lastname");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReconnectEvent = args -> {
        mSocket.emit(NetworkConstants.SYNC_EVENT_ID, new Gson()
                .toJson(new User(FirebaseAuth.getInstance().getUid(), firstname, lastname)));
    };

    private Emitter.Listener onNewMessageReceived = args -> {
        JSONObject data = (JSONObject) args[0];
        try {
            if (!EventsService.firstname.equals(data.getString("firstname"))
                    && !EventsService.lastname.equals(data.getString("lastname"))) {
                String sender = data.getString("firstname") + " " + data.getString("lastname");

                Notification notification = new NotificationCompat.Builder(this,
                        NetworkConstants.NEW_MESSAGES_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(data.getString("roomTitle"))
                        .setContentText(sender + ": " + data.getString("body"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(onListScreenPending)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(data.getInt("roomId"), notification);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
