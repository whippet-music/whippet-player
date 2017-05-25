package com.whippetmusic.whippetplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whippetmusic.whippetplayer.model.Track;
import com.whippetmusic.whippetplayer.network.RetrofitFactory;
import com.whippetmusic.whippetplayer.request.VoteRequest;
import com.whippetmusic.whippetplayer.service.TrackService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActivity extends AppCompatActivity {
    private Track track;
    private TrackService trackService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        trackService = RetrofitFactory.create(this).create(TrackService.class);

        track = getIntent().getParcelableExtra("track");
        setTrackInfo();
        initializeButtons();
    }

    private void setTrackInfo() {
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView artistNameTextView = (TextView) findViewById(R.id.artistNameTextView);
        TextView releaseTextView = (TextView) findViewById(R.id.releaseTextView);

        titleTextView.setText(track.getTitle());
        artistNameTextView.setText(track.getArtistName());
        releaseTextView.setText(track.getRelease());
    }

    private void initializeButtons() {
        Button likeButton = (Button) findViewById(R.id.likeButton);
        Button dislikeButton = (Button) findViewById(R.id.dislikeButton);

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote(1);
            }
        });
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote(0);
            }
        });
    }

    private void vote(int voteFlag) {
        Call<ResponseBody> voteCall = trackService.vote(track.getId(), new VoteRequest(voteFlag));
        voteCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
