package com.whippetmusic.whippetplayer.client;

import com.whippetmusic.whippetplayer.model.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by maciej on 20.05.17.
 */

public interface TrackClient {
    @GET("/tracks")
    Call<List<Track>> tracksForUser();
}
