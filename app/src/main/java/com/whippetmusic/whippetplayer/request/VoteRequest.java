package com.whippetmusic.whippetplayer.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciej on 25.05.17.
 */

public class VoteRequest {
    @SerializedName(value = "vote_flag")
    final int voteFlag;

    public VoteRequest(int voteFlag) {
        this.voteFlag = voteFlag;
    }
}
