package com.oleksii.simplechat.roomslistfragment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.models.ListRoom;
import com.oleksii.simplechat.utils.Constants;
import com.oleksii.simplechat.utils.IRest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoomsListViewModel extends ViewModel {

    private Socket mSocket;
    private IRest IRest;
    private static final String TAG = "RoomsListViewModel";
    public final MutableLiveData<ArrayList<ListRoom>> availableRooms = new MutableLiveData<>();

    public RoomsListViewModel(Socket socket) {
        availableRooms.setValue(new ArrayList<>());
        this.mSocket = socket;

        init();
    }

    private void init() {
        availableRooms.setValue(new ArrayList<>());

        mSocket.on("onNewGroupAdded", onNewGroupAdded);
        mSocket.on("onNewMessageReceived", onNewMessageReceived);

        updateRoomsList();
    }

    private void updateRoomsList() {
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

    private Emitter.Listener onNewGroupAdded = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateRoomsList();
        }
    };

    private Emitter.Listener onNewMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateRoomsList();
        }
    };
}
