package com.whippetmusic.whippetplayer.service;

import com.whippetmusic.whippetplayer.model.Vote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by maciej on 03.06.17.
 */

public interface VoteService {
    @GET("/votes")
    Call<List<Vote>> votesForUser();
}
