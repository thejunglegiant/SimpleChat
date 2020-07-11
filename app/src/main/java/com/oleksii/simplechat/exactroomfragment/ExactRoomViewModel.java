package com.oleksii.simplechat.exactroomfragment;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.ChatApplication;
import com.oleksii.simplechat.objects.Message;
import com.oleksii.simplechat.objects.Message;
import com.oleksii.simplechat.utils.Constants;
import com.oleksii.simplechat.utils.IRest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExactRoomViewModel extends ViewModel {

    private String username;
    public Socket mSocket;
    private IRest IRest;
    private long roomId;
    private static final String TAG = "ExactRoomViewModel";
    MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();
    private ChatApplication app;

    public ExactRoomViewModel(ChatApplication app, long roomId, String username) {
        this.app = app;
        this.roomId = roomId;
        this.username = username;

        init();
    }

    private void init() {
        messages.setValue(new ArrayList<>());
        mSocket = app.getSocket();
        mSocket.on("onNewMessageReceived", onNewMessageReceived);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CHAT_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRest = retrofit.create(IRest.class);
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
                        data.getString("firstname"), data.getString("body"),
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
}
