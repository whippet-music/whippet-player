package com.whippetmusic.whippetplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.whippetmusic.whippetplayer.model.Track;

public class TrackActivity extends AppCompatActivity {
    private Track track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        track = getIntent().getParcelableExtra("track");
        setTrackInfo();
    }

    private void setTrackInfo() {
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView artistNameTextView = (TextView) findViewById(R.id.artistNameTextView);
        TextView releaseTextView = (TextView) findViewById(R.id.releaseTextView);

        titleTextView.setText(track.getTitle());
        artistNameTextView.setText(track.getArtistName());
        releaseTextView.setText(track.getRelease());
    }
}
