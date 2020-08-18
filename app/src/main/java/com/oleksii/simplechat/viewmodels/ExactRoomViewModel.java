package com.oleksii.simplechat.viewmodels;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.constants.NetworkConstants;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.models.ExactRoom;
import com.oleksii.simplechat.models.Message;
import com.oleksii.simplechat.utils.ChatAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ExactRoomViewModel extends ViewModel {

    private static final String TAG = "ExactRoomViewModel";
    private MutableLiveData<List<Message>> messagesList = new MutableLiveData<>();
    private MutableLiveData<Integer> membersCount = new MutableLiveData<>();
    private MutableLiveData<String> typingUser = new MutableLiveData<>();
    private long roomId;
    private String firstname;
    private String lastname;
    @Inject Gson gson;
    @Inject Socket mSocket;
    @Inject ChatAPI chatAPI;
    @Inject Retrofit retrofit;
    // Temporary
    NotificationManager notificationManager;

    @Override
    protected void onCleared() {
        super.onCleared();
        mSocket.off(NetworkConstants.NEW_MESSAGE_RECEIVED_EVENT_ID, onNewMessageReceived);
    }

    public ExactRoomViewModel(long roomId, String firstname, String lastname, Context context) {
        this.roomId = roomId;
        this.firstname = firstname;
        this.lastname = lastname;
        notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.cancel((int) roomId);

        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);

        init();
    }

    private void init() {
        messagesList.setValue(new LinkedList<>());

        mSocket.on(NetworkConstants.NEW_MESSAGE_RECEIVED_EVENT_ID, onNewMessageReceived);
        mSocket.on(NetworkConstants.SOMEONE_LEFT_GROUP_EVENT_ID, onSomeoneLeftGroup);
        mSocket.on(NetworkConstants.TYPING_EVENT_ID, onSomeonesTyping);

        chatAPI.getExactRoomInfo(FirebaseAuth.getInstance().getUid(), roomId)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new SingleObserver<ExactRoom>() {
                    @Override
                    public void onSubscribe(Disposable d) {  }

                    @Override
                    public void onSuccess(ExactRoom exactRoom) {
                        membersCount.postValue(exactRoom.getMembers());
                        messagesList.postValue(exactRoom.getMessages());
                    }

                    @Override
                    public void onError(Throwable e) { Log.e(TAG, e.getMessage()); }
                });

    }

    private Emitter.Listener onNewMessageReceived = args -> {
        JSONObject data = (JSONObject) args[0];
        try {
            if (data.getInt("roomId") == roomId) {
                Message message = new Message(data.getString("firstname").equals(this.firstname)
                        && data.getString("lastname").equals(this.lastname),
                        data.getString("firstname"), data.getString("lastname"), data.getString("body"),
                        new Timestamp(data.getLong("stime")), data.getInt("viewtype"));
                this.addMessage(message);

                notificationManager.cancel((int) roomId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    private Emitter.Listener onSomeoneLeftGroup = args -> {
        JSONObject data = (JSONObject) args[0];
        try {
            Message message = new Message(data.getString("firstname"), data.getString("lastname"),
                    new Timestamp(data.getLong("stime")), data.getInt("viewtype"));
            this.addMessage(message);
            this.setMembersCount(data.getInt("members"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    private Emitter.Listener onSomeonesTyping = args -> {
        JSONObject data = (JSONObject) args[0];
        try {
            if (roomId == data.getInt("roomId"))
                this.setTypingUser(data.getString("firstname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    public void addMessage(Message message) {
        List<Message> tmp = messagesList.getValue();
        tmp.add(message);
        messagesList.postValue(tmp);
    }

    public LiveData<List<Message>> getMessagesList() {
        return this.messagesList;
    }

    public LiveData<Integer> getMembersCount() {
        return this.membersCount;
    }

    private void setMembersCount(int members) {
        this.membersCount.postValue(members);
    }

    public LiveData<String> getTypingUser() {
        return typingUser;
    }

    private void setTypingUser(String typingUser) {
        this.typingUser.postValue(typingUser);
    }

    public void sendMessage(String message) {
        Message newMessage = new Message(FirebaseAuth.getInstance().getUid(),
                roomId, message);
        mSocket.emit(NetworkConstants.NEW_MESSAGE_SENT_EVENT_ID, gson.toJson(newMessage));
    }

    public void leaveGroup() {
        Message leaveMessage = new Message(FirebaseAuth.getInstance().getUid(),
                roomId);
        mSocket.emit(NetworkConstants.LEAVE_GROUP_EVENT_ID, gson.toJson(leaveMessage));
    }

    public void typingEvent() {
        mSocket.emit("typing", roomId);
    }
}
