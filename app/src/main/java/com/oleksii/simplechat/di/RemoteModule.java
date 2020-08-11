package com.oleksii.simplechat.di;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oleksii.simplechat.constants.NetworkConstants;

import java.net.URISyntaxException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RemoteModule {

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(NetworkConstants.CHAT_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    Socket provideSocket() {
        try {
            Socket socket = IO.socket(NetworkConstants.CHAT_SERVER_URL);
            socket.connect();
            return socket;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
