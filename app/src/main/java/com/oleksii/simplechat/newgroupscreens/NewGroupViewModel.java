package com.oleksii.simplechat.newgroupscreens;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
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

    private static final String TAG = "NewGroupViewModel";
    private IRest userRest;
    public MutableLiveData<ArrayList<User>> availableUsers = new MutableLiveData<>();
    public MutableLiveData<ArrayList<User>> checkedUsers = new MutableLiveData<>();

    public NewGroupViewModel() {
        checkedUsers.setValue(new ArrayList<>());
    }

    protected void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CHAT_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userRest = retrofit.create(IRest.class);
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
        Call<ArrayList<NewRoom>> call = userRest.createNewGroup(
                FirebaseAuth.getInstance().getUid() + "/createGroup",
                new NewRoom(title, checkedUsers.getValue()));

        call.enqueue(new Callback<ArrayList<NewRoom>>() {
            @Override
            public void onResponse(Call<ArrayList<NewRoom>> call, Response<ArrayList<NewRoom>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Something went wrong with creating new group request!");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NewRoom>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }

    protected void clear() {
        this.availableUsers.setValue(new ArrayList<>());
        this.checkedUsers.setValue(new ArrayList<>());
    }

}
