package com.oleksii.simplechat.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.models.NewRoom;
import com.oleksii.simplechat.models.User;
import com.oleksii.simplechat.data.net.ChatAPI;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class NewGroupViewModel extends ViewModel {

    private final String TAG = this.getClass().getName();
    private MutableLiveData<List<User>> availableUsers = new MutableLiveData<>();
    private MutableLiveData<List<User>> checkedUsers = new MutableLiveData<>();
    private String roomTitle = "";
    @Inject Socket mSocket;
    @Inject ChatAPI chatAPI;
    @Inject Retrofit retrofit;

    public NewGroupViewModel() {
        availableUsers.setValue(new LinkedList<>());
        checkedUsers.setValue(new LinkedList<>());

        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);

        init();
    }

    private void init() {
        chatAPI.getAvailableUsers(FirebaseAuth.getInstance().getUid())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new SingleObserver<List<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(List<User> users) {
                        availableUsers.postValue(users);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }
                });
    }

    public void addCheckedUser(User user) {
        List<User> tmp = checkedUsers.getValue();
        tmp.add(user);
        checkedUsers.setValue(tmp);
    }

    public void removeCheckedUser(User user) {
        List<User> tmp = checkedUsers.getValue();
        tmp.remove(user);
        checkedUsers.setValue(tmp);
    }

    public void createNewGroup() {
        mSocket.emit("onNewGroupCreated", new Gson()
                .toJson(new NewRoom(roomTitle, checkedUsers.getValue())));
    }

    public LiveData<List<User>> getAvailableUsers() {
        return availableUsers;
    }

    public LiveData<List<User>> getCheckedUsers() {
        return checkedUsers;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }
}
