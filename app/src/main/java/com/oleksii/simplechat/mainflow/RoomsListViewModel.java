package com.oleksii.simplechat.mainflow;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.ChatApplication;
import com.oleksii.simplechat.objects.ListRoom;
import com.oleksii.simplechat.utils.Constants;
import com.oleksii.simplechat.utils.IRest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoomsListViewModel extends ViewModel {

    public Socket mSocket;
    private IRest IRest;
    private static final String TAG = "RoomsListViewModel";
    private ChatApplication app;
    public final MutableLiveData<ArrayList<ListRoom>> availableRooms = new MutableLiveData<>();

    public RoomsListViewModel(ChatApplication app) {
        availableRooms.setValue(new ArrayList<>());
        this.app = app;

        init();
    }

    private void init() {
        availableRooms.setValue(new ArrayList<>());
        mSocket = app.getSocket();
        mSocket.on("register", onSomething);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.CHAT_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IRest = retrofit.create(IRest.class);
        Call<ArrayList<ListRoom>> call = IRest.getAvailableRooms(
                FirebaseAuth.getInstance().getUid() + "/getRooms");

        call.enqueue(new Callback<ArrayList<ListRoom>>() {
            @Override
            public void onResponse(Call<ArrayList<ListRoom>> call, Response<ArrayList<ListRoom>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                ArrayList<ListRoom> rooms = response.body();
                availableRooms.setValue(rooms);
            }

            @Override
            public void onFailure(Call<ArrayList<ListRoom>> call, Throwable t) {

            }
        });

    }

    private Emitter.Listener onSomething = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Log.i(TAG, "works");
        }
    };
}
