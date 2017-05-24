package com.whippetmusic.whippetplayer.service;

import com.whippetmusic.whippetplayer.model.Recommendation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by maciej on 20.05.17.
 */

public interface RecommendationService {
    @GET("/recommendations")
    Call<List<Recommendation>> recommendationsForUser();
}
