package com.whippetmusic.whippetplayer.model;

/**
 * Created by maciej on 20.05.17.
 */

public class MetaData {
    private int trackId;
    private float year;
    private float artistFamiliarity;
    private float artistHotttnesss;
    private float artistLatitude;
    private float artistLongitude;
    private float duration;
    private float endOfFadeIn;
    private float key;
    private float keyConfidence;
    private float loudness;
    private float mode;
    private float modeConfidence;
    private float songHotttnesss;
    private float startOfFadeOut;
    private float tempo;
    private float timeSignature;
    private float timeSignatureConfidence;

    public int getTrackId() {
        return trackId;
    }
    public float[] getFeatureVector() {
        return new float[] {
                this.year,
                this.artistFamiliarity,
                this.artistHotttnesss,
                this.artistLatitude,
                this.artistLongitude,
                this.duration,
                this.endOfFadeIn,
                this.key,
                this.keyConfidence,
                this.loudness,
                this.mode,
                this.modeConfidence,
                this.songHotttnesss,
                this.startOfFadeOut,
                this.tempo,
                this.timeSignature,
                this.timeSignatureConfidence,
        };
    }
}
