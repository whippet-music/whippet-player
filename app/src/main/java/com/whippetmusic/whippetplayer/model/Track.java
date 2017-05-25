package com.whippetmusic.whippetplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maciej on 20.05.17.
 */

public class Track implements Parcelable {
    private int id;
    private String artistName;
    private String title;
    private String release;

    private Track(Parcel in) {
        id = in.readInt();
        artistName = in.readString();
        title = in.readString();
        release = in.readString();
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getRelease() {
        return release;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(artistName);
        dest.writeString(title);
        dest.writeString(release);
    }

    @Override
    public String toString() {
        return title;
    }
}
