package com.oleksii.simplechat;

import android.app.Application;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.objects.Message;
import com.oleksii.simplechat.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.Timestamp;

public class ChatApplication extends Application {

    private static final String TAG = "ChatApplication";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSocket.connect();
        mSocket.emit("onUidSent", FirebaseAuth.getInstance().getUid());
        mSocket.on("reconnect", onReconnectEvent);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mSocket.disconnect();
    }

    private Emitter.Listener onReconnectEvent = args -> {
        mSocket.emit("onUidSent", FirebaseAuth.getInstance().getUid());
    };
}
