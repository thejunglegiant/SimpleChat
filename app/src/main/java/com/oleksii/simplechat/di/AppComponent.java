package com.oleksii.simplechat.di;

import com.github.nkzawa.socketio.client.Socket;
import com.oleksii.simplechat.activities.MainActivity;
import com.oleksii.simplechat.viewmodels.ExactRoomViewModel;
import com.oleksii.simplechat.viewmodels.NewGroupViewModel;
import com.oleksii.simplechat.viewmodels.RoomsListViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = { RemoteModule.class })
@Singleton
public interface AppComponent {

    Socket getSocket();

    void inject(MainActivity mainActivity);
    void inject(ExactRoomViewModel exactRoomViewModel);
    void inject(NewGroupViewModel newGroupViewModel);
    void inject(RoomsListViewModel roomsListViewModel);
}
