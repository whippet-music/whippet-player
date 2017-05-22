package com.whippetmusic.whippetplayer.client;

import com.whippetmusic.whippetplayer.model.User;
import com.whippetmusic.whippetplayer.request.UserAuthRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by maciej on 20.05.17.
 */

public interface UserClient {
    @POST("/auth")
    Call<User> login(@Body UserAuthRequest body);
}
