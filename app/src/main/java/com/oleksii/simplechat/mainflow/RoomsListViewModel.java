package com.oleksii.simplechat.mainflow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.oleksii.simplechat.objects.ChatObject;
import com.oleksii.simplechat.objects.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomsListViewModel extends ViewModel {

    public final MutableLiveData<ArrayList<ChatObject>> rooms = new MutableLiveData<>();

    public RoomsListViewModel() {
        init();
    }

    private void init() {
        rooms.setValue(new ArrayList<>());
    }

    public void setCurrentUser(ChatObject room) {
        ArrayList<ChatObject> newList = rooms.getValue();
        newList.add(room);
        rooms.setValue(newList);
    }
}
