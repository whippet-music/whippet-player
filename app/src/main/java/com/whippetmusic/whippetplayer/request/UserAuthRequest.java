package com.whippetmusic.whippetplayer.request;

/**
 * Created by maciej on 22.05.17.
 */

public class UserAuthRequest {
    final String username;
    final String password;

    public UserAuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
