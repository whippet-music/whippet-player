package com.whippetmusic.whippetplayer.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by maciej on 20.05.17.
 */

public class MetaData {
    @SerializedName(value = "track_id")
    private int trackId;

    @SerializedName(value = "year")
    private float year;

    @SerializedName(value = "artist_familiarity")
    private float artistFamiliarity;

    @SerializedName(value = "artist_hotness")
    private float artistHotness;

    @SerializedName(value = "artist_latitude")
    private float artistLatitude;

    @SerializedName(value = "artist_longitude")
    private float artistLongitude;

    @SerializedName(value = "duration")
    private float duration;

    @SerializedName(value = "end_of_fade_in")
    private float endOfFadeIn;

    @SerializedName(value = "key")
    private float key;

    @SerializedName(value = "key_confidence")
    private float keyConfidence;

    @SerializedName(value = "loudness")
    private float loudness;

    @SerializedName(value = "mode")
    private float mode;

    @SerializedName(value = "mode_confidence")
    private float modeConfidence;

    @SerializedName(value = "song_hotness")
    private float songHotness;

    @SerializedName(value = "start_of_fade_out")
    private float startOfFadeOut;

    @SerializedName(value = "tempo")
    private float tempo;

    @SerializedName(value = "time_signature")
    private float timeSignature;

    @SerializedName(value = "time_signature_confidence")
    private float timeSignatureConfidence;

    public int getTrackId() {
        return trackId;
    }
    public float[] getFeatureVector() {
        return new float[] {
            this.year,
            this.artistFamiliarity,
            this.artistHotness,
            this.artistLatitude,
            this.artistLongitude,
            this.duration,
            this.endOfFadeIn,
            this.key,
            this.keyConfidence,
            this.loudness,
            this.mode,
            this.modeConfidence,
            this.songHotness,
            this.startOfFadeOut,
            this.tempo,
            this.timeSignature,
            this.timeSignatureConfidence,
        };
    }
}
