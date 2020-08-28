package com.oleksii.simplechat.data.net;

import com.oleksii.simplechat.models.ExactRoom;
import com.oleksii.simplechat.models.ListRoom;
import com.oleksii.simplechat.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChatAPI {

    @GET("{userId}/getUsers")
    Single<List<User>> getAvailableUsers(@Path("userId") String userId);

    @GET("{userId}/getRooms")
    Single<List<ListRoom>> getAvailableRooms(@Path("userId") String userId);

    @GET("{userId}/{roomId}/getExactRoom")
    Single<ExactRoom> getExactRoomInfo(@Path("userId") String userId, @Path("roomId") long roomId);
}
