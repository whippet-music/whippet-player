package com.whippetmusic.whippetplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciej on 20.05.17.
 */

public class Recommendation {
    @SerializedName(value = "track_id")
    private int trackId;

    public int getTrackId() {
        return trackId;
    }
}
