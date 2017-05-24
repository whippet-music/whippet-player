package com.whippetmusic.whippetplayer.service;

import com.whippetmusic.whippetplayer.model.MetaData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by maciej on 20.05.17.
 */

public interface MetaDataService {
    @GET("/meta_data")
    Call<List<MetaData>> metadataForUser(@Query("trackIds") ArrayList<Integer> trackIds);
}
