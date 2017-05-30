package com.whippetmusic.whippetplayer.service;

import com.whippetmusic.whippetplayer.model.Track;
import com.whippetmusic.whippetplayer.request.VoteRequest;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by maciej on 20.05.17.
 */

public interface TrackService {
    @GET("/tracks")
    Call<List<Track>> tracksForUser();

    @GET("/tracks")
    Call<List<Track>> tracksForUser(@Query("id") ArrayList<Integer> trackIds);

    @POST("/tracks/{id}/vote")
    Call<ResponseBody> vote(@Path("id") int id, @Body VoteRequest body);
}
