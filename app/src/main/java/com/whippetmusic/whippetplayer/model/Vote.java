package com.whippetmusic.whippetplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciej on 03.06.17.
 */

public class Vote {
    @SerializedName(value = "id")
    private int id;

    @SerializedName(value = "track_id")
    private int trackId;

    @SerializedName(value = "user_id")
    private int userId;

    @SerializedName(value = "vote_flag")
    private int voteFlag;

    public int getTrackId() {
        return trackId;
    }

    public int getVoteFlag() {
        return voteFlag;
    }
}