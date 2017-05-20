package com.whippetmusic.whippetplayer.client;

import com.whippetmusic.whippetplayer.model.Track;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maciej on 20.05.17.
 */

public interface TrackClient {
    @GET("/tracks")
    Call<List<Track>> tracksForUser();

    @GET("/tracks")
    Call<List<Track>> tracksForUser(@Query("trackIds") ArrayList<Integer> trackIds);
}
