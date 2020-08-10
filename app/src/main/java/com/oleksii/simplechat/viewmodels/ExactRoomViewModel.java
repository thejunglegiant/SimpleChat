package com.oleksii.simplechat.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.models.Message;
import com.oleksii.simplechat.utils.Constants;
import com.oleksii.simplechat.utils.IRest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExactRoomViewModel extends ViewModel {

    private static final String TAG = "ExactRoomViewModel";
    public MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();
    private long roomId;
    private String username;
    @Inject Socket mSocket;
    @Inject Retrofit retrofit;

    public ExactRoomViewModel(long roomId, String username) {
        this.roomId = roomId;
        this.username = username;

        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);

        init();
    }

    private void init() {
        messages.setValue(new ArrayList<>());
        mSocket.on("onNewMessageReceived", onNewMessageReceived);

        IRest IRest = retrofit.create(com.oleksii.simplechat.utils.IRest.class);
        Call<ArrayList<Message>> call = IRest.getAllRoomMessages(
                FirebaseAuth.getInstance().getUid() + "/" + roomId + "/getMessages"
        );

        call.enqueue(new Callback<ArrayList<Message>>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                if (response.isSuccessful() && response.body() != null)
                    messages.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

    }

    private Emitter.Listener onNewMessageReceived = args -> {
        JSONObject data = (JSONObject) args[0];
        try {
            if (data.getInt("roomId") == roomId) {
                Message message = new Message(data.getString("firstname").equals(this.username),
                        data.getString("firstname"), data.getString("lastname"), data.getString("body"),
                        new Timestamp(data.getLong("stime")));
                this.addMessage(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public void addMessage(Message message) {
        ArrayList<Message> tmp = messages.getValue();
        tmp.add(message);
        messages.postValue(tmp);
    }

    public void sendMessage(String message) {
        Message newMessage = new Message(FirebaseAuth.getInstance().getUid(),
                roomId, message);
        mSocket.emit("onNewMessageSent", new Gson().toJson(newMessage));
    }
}
