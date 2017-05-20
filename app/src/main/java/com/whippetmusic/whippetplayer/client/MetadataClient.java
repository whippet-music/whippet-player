package com.whippetmusic.whippetplayer.client;

import com.whippetmusic.whippetplayer.model.Metadata;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maciej on 20.05.17.
 */

public interface MetadataClient {
    @GET("/metadata")
    Call<List<Metadata>> metadataForUser(@Query("trackIds") ArrayList<Integer> trackIds);
}
