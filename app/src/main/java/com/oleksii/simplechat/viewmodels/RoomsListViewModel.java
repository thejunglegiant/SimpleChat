package com.oleksii.simplechat.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.oleksii.simplechat.constants.NetworkConstants;
import com.oleksii.simplechat.di.AppComponent;
import com.oleksii.simplechat.di.DaggerAppComponent;
import com.oleksii.simplechat.models.ListRoom;
import com.oleksii.simplechat.utils.ChatAPI;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RoomsListViewModel extends ViewModel {

    private static final String TAG = RoomsListViewModel.class.getName();
    private final MutableLiveData<List<ListRoom>> availableRooms = new MutableLiveData<>();
    @Inject Socket mSocket;
    @Inject ChatAPI chatAPI;
    @Inject Retrofit retrofit;

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
        chatAPI.getAvailableRooms(FirebaseAuth.getInstance().getUid())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<ListRoom>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onSuccess(List<ListRoom> listRooms) {
                        availableRooms.postValue(listRooms);
                    }

                    @Override
                    public void onError(Throwable e) { }
                });
    }

    private Emitter.Listener onInfoChanged = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            updateRoomsList();
        }
    };

    public LiveData<List<ListRoom>> getAvailableRooms() {
        return this.availableRooms;
    }
}
