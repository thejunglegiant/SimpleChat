package com.oleksii.simplechat;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.oleksii.simplechat.utils.Constants;

import java.net.URISyntaxException;

public class ChatApplication extends Application {
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
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mSocket.disconnect();
    }
}
