package com.whippetmusic.whippetplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciej on 20.05.17.
 */

public class User {
    @SerializedName(value = "access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
