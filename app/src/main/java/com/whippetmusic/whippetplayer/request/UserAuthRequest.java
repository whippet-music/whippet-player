package com.whippetmusic.whippetplayer.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciej on 22.05.17.
 */

public class UserAuthRequest {
    @SerializedName(value = "username")
    final String username;

    @SerializedName(value = "password")
    final String password;

    public UserAuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
