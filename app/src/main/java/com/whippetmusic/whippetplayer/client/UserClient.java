package com.whippetmusic.whippetplayer.client;

import com.whippetmusic.whippetplayer.model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

/**
 * Created by maciej on 20.05.17.
 */

public interface UserClient {
    @POST("/sessions")
    Call<User> login(@Field("username") String username, @Field("password") String password);
}
