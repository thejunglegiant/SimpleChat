package com.oleksii.simplechat.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.constants.NetworkConstants;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.models.ListRoom;
import com.oleksii.simplechat.utils.IRest;

import java.util.LinkedList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RoomsListViewModel extends ViewModel {

    private static final String TAG = RoomsListViewModel.class.getName();
    public final MutableLiveData<LinkedList<ListRoom>> availableRooms = new MutableLiveData<>();
    @Inject Retrofit retrofit;
    @Inject Socket mSocket;

    public RoomsListViewModel() {
        init();
    }

    private void init() {
        availableRooms.setValue(new LinkedList<>());

        AppComponent appComponent = DaggerAppComponent.create();
        appComponent.inject(this);

        mSocket.on(NetworkConstants.NEW_MESSAGE_EVENT_ID, onInfoChanged);
        mSocket.on(NetworkConstants.NEW_GROUP_EVENT_ID, onInfoChanged);

        updateRoomsList();
    }

    private void updateRoomsList() {
        IRest IRest = retrofit.create(com.oleksii.simplechat.utils.IRest.class);
        Call<LinkedList<ListRoom>> call = IRest.getAvailableRooms(
                FirebaseAuth.getInstance().getUid() + "/getRooms");

        call.enqueue(new Callback<LinkedList<ListRoom>>() {
            @Override
            public void onResponse(Call<LinkedList<ListRoom>> call, Response<LinkedList<ListRoom>> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                LinkedList<ListRoom> rooms = response.body();
                availableRooms.setValue(rooms);
            }

            @Override
            public void onFailure(Call<LinkedList<ListRoom>> call, Throwable t) {

            }
        });
    }

    private Emitter.Listener onInfoChanged = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateRoomsList();
        }
    };
}
