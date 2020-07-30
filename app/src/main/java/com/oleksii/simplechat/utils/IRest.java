package com.oleksii.simplechat.utils;

import com.oleksii.simplechat.objects.ListRoom;
import com.oleksii.simplechat.objects.Message;
import com.oleksii.simplechat.objects.NewRoom;
import com.oleksii.simplechat.objects.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IRest {

    @GET
    Call<ArrayList<User>> getAvailableUsers(@Url String url);

    @GET
    Call<ArrayList<ListRoom>> getAvailableRooms(@Url String url);

    @GET
    Call<ArrayList<Message>> getAllRoomMessages(@Url String url);
}
