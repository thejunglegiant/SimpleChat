package com.oleksii.simplechat.utils;

import com.oleksii.simplechat.models.ListRoom;
import com.oleksii.simplechat.models.Message;
import com.oleksii.simplechat.models.User;

import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IRest {

    @GET
    Call<LinkedList<User>> getAvailableUsers(@Url String url);

    @GET
    Call<LinkedList<ListRoom>> getAvailableRooms(@Url String url);

    @GET
    Call<LinkedList<Message>> getAllRoomMessages(@Url String url);
}
