package com.oleksii.simplechat.newgroupscreens;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.oleksii.simplechat.objects.NewRoom;
import com.oleksii.simplechat.objects.User;
import com.oleksii.simplechat.utils.Constants;
import com.oleksii.simplechat.utils.IRest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewGroupViewModel extends ViewModel {

    private final String TAG = this.getClass().getName();
    private Socket mSocket;
    public MutableLiveData<ArrayList<User>> availableUsers = new MutableLiveData<>();
    public MutableLiveData<ArrayList<User>> checkedUsers = new MutableLiveData<>();

    public NewGroupViewModel(Socket socket) {
        this.mSocket = socket;
        checkedUsers.setValue(new ArrayList<>());
    }

    protected void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CHAT_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRest userRest = retrofit.create(IRest.class);
        Call<ArrayList<User>> call = userRest.getAvailableUsers(
                FirebaseAuth.getInstance().getUid() + "/getUsers");

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ArrayList<User> users = response.body();
                availableUsers.setValue(users);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    public void addCheckedUser(User user) {
        ArrayList<User> tmp = checkedUsers.getValue();
        tmp.add(user);
        checkedUsers.setValue(tmp);
    }

    public void removeCheckedUser(User user) {
        ArrayList<User> tmp = checkedUsers.getValue();
        tmp.remove(user);
        checkedUsers.setValue(tmp);
    }

    public ArrayList<User> getCheckedUsers() {
        return checkedUsers.getValue();
    }

    protected void createNewGroup(String title) {
        mSocket.emit("onNewGroupCreated", new Gson()
                .toJson(new NewRoom(title, checkedUsers.getValue())));
    }

    protected void clear() {
        this.availableUsers.setValue(new ArrayList<>());
        this.checkedUsers.setValue(new ArrayList<>());
    }

}
