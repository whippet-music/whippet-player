package com.whippetmusic.whippetplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciej on 20.05.17.
 */

public class User {
    // FIXME: Temporary fix due to python api limitations
    @SerializedName(value = "accessToken", alternate = {"access_token"})
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }
}
