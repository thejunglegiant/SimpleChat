package com.oleksii.simplechat.utils;

import com.oleksii.simplechat.objects.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IUserRest {

    @POST("register")
    Call<User> registerUser(@Body User user);
}
