package com.oleksii.simplechat.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.models.NewRoom;
import com.oleksii.simplechat.models.User;
import com.oleksii.simplechat.utils.IRest;

import java.util.LinkedList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewGroupViewModel extends ViewModel {

    private final String TAG = this.getClass().getName();
    @Inject Socket mSocket;
    @Inject Retrofit retrofit;
    public MutableLiveData<LinkedList<User>> availableUsers = new MutableLiveData<>();
    public MutableLiveData<LinkedList<User>> checkedUsers = new MutableLiveData<>();

    public NewGroupViewModel() {
        checkedUsers.setValue(new LinkedList<>());

        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);

        init();
    }

    private void init() {
        IRest userRest = retrofit.create(IRest.class);
        Call<LinkedList<User>> call = userRest.getAvailableUsers(
                FirebaseAuth.getInstance().getUid() + "/getUsers");

        call.enqueue(new Callback<LinkedList<User>>() {
            @Override
            public void onResponse(Call<LinkedList<User>> call, Response<LinkedList<User>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                LinkedList<User> users = response.body();
                availableUsers.setValue(users);
            }

            @Override
            public void onFailure(Call<LinkedList<User>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void addCheckedUser(User user) {
        LinkedList<User> tmp = checkedUsers.getValue();
        tmp.add(user);
        checkedUsers.setValue(tmp);
    }

    public void removeCheckedUser(User user) {
        LinkedList<User> tmp = checkedUsers.getValue();
        tmp.remove(user);
        checkedUsers.setValue(tmp);
    }

    public LinkedList<User> getCheckedUsers() {
        return checkedUsers.getValue();
    }

    public void createNewGroup(String title) {
        mSocket.emit("onNewGroupCreated", new Gson()
                .toJson(new NewRoom(title, checkedUsers.getValue())));
    }
}
