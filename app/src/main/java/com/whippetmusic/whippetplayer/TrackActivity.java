package com.whippetmusic.whippetplayer;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.whippetmusic.whippetplayer.model.LogisticRegression;
import com.whippetmusic.whippetplayer.model.LogisticRegressionFactory;
import com.whippetmusic.whippetplayer.model.MetaData;
import com.whippetmusic.whippetplayer.model.Track;
import com.whippetmusic.whippetplayer.network.RetrofitFactory;
import com.whippetmusic.whippetplayer.request.VoteRequest;
import com.whippetmusic.whippetplayer.service.MetaDataService;
import com.whippetmusic.whippetplayer.service.TrackService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TrackActivity extends AppCompatActivity {
    private Track track;
    private MetaData metaData;
    private TrackService trackService;
    private MetaDataService metaDataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        Retrofit retrofit = RetrofitFactory.create(this);
        trackService = retrofit.create(TrackService.class);
        metaDataService = retrofit.create(MetaDataService.class);

        track = getIntent().getParcelableExtra("track");

        ArrayList<Integer> trackIds = new ArrayList<>();
        System.out.println(track.getId());
        trackIds.add(track.getId());
        Call<List<MetaData>> metadataCall = metaDataService.metadataForUser(trackIds);
        metadataCall.enqueue(fetchMetadataCallback);
    }

    private Callback<List<MetaData>> fetchMetadataCallback = new Callback<List<MetaData>>() {
        @Override
        public void onResponse(Call<List<MetaData>> call, Response<List<MetaData>> response) {
            metaData = response.body().get(0);
            setTrackInfo();
            initializeButtons();
        }

        @Override
        public void onFailure(Call<List<MetaData>> call, Throwable t) {}
    };

    private void setTrackInfo() {
        TextView titleTextView = (TextView) findViewById(R.id.titleTextView);
        TextView artistNameTextView = (TextView) findViewById(R.id.artistNameTextView);
        TextView releaseTextView = (TextView) findViewById(R.id.releaseTextView);
        TextView yearTextView = (TextView) findViewById(R.id.yearTextView);
        TextView stopTextVIew = (TextView) findViewById(R.id.stopTextView);

        titleTextView.setText(track.getTitle());
        artistNameTextView.setText(track.getArtistName());
        releaseTextView.setText(track.getRelease());
        int year = (int) metaData.getYear();
        yearTextView.setText(String.valueOf(year));
        int minutes = (int) metaData.getDuration() / 60;
        int seconds = (int) metaData.getDuration() % 60;
        stopTextVIew.setText(String.format("%d:%02d", minutes, seconds));
    }

    private void initializeButtons() {
        final ImageView likeImageView = (ImageView) findViewById(R.id.likeImageView);
        final ImageView dislikeImageView = (ImageView) findViewById(R.id.dislikeImageView);

        likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeImageView.setColorFilter(Color.parseColor("#ff4081"));
                dislikeImageView.setColorFilter(Color.GRAY);
                likeImageView.setOnClickListener(null);
                dislikeImageView.setOnClickListener(null);
                vote(1);
            }
        });
        dislikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeImageView.setColorFilter(Color.GRAY);
                dislikeImageView.setColorFilter(Color.parseColor("#ff4081"));
                likeImageView.setOnClickListener(null);
                dislikeImageView.setOnClickListener(null);
                vote(0);
            }
        });
    }

    private void vote(int voteFlag) {
        Call<ResponseBody> voteCall = trackService.vote(track.getId(), new VoteRequest(voteFlag));
        LogisticRegression regression = LogisticRegressionFactory.getLogisticRegression(getBaseContext());
        regression.train(metaData.getFeatureVector(), voteFlag);

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
