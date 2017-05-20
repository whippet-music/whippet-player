package com.whippetmusic.whippetplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.whippetmusic.whippetplayer.client.MetadataClient;
import com.whippetmusic.whippetplayer.client.RecommendationClient;
import com.whippetmusic.whippetplayer.client.TrackClient;
import com.whippetmusic.whippetplayer.model.Metadata;
import com.whippetmusic.whippetplayer.model.Recommendation;
import com.whippetmusic.whippetplayer.model.Track;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MixActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RecommendationClient recommendationClient;
    private MetadataClient metadataClient;
    private TrackClient trackClient;
    private ArrayList<String> trackNames;
    private ArrayAdapter<String> adapter;

    private Handler responseHandler = new Handler() {
        public void handleMessage(Message message) {
            trackNames.addAll(message.getData().getStringArrayList("tracks"));
            adapter.notifyDataSetChanged();
        }
    };

    private Callback<List<Track>> fetchTracksCallback = new Callback<List<Track>>() {
        @Override
        public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
            ArrayList<String> trackNames = new ArrayList<>();

            for(Track track : response.body()) {
                trackNames.add(track.getTitle());
            }

            Message message = responseHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("tracks", trackNames);
            message.setData(bundle);
            responseHandler.sendMessage(message);
        }

        @Override
        public void onFailure(Call<List<Track>> call, Throwable t) {}
    };

    private Callback<List<Metadata>> fetchMetadataCallback = new Callback<List<Metadata>>() {
        @Override
        public void onResponse(Call<List<Metadata>> call, Response<List<Metadata>> response) {
            ArrayList<Integer> trackIds = new ArrayList<>();

            for (Metadata metadata : response.body()) {
                trackIds.add(metadata.getTrackId());
            }

            Call<List<Track>> trackCall = trackClient.tracksForUser(trackIds);
            trackCall.enqueue(fetchTracksCallback);
        }

        @Override
        public void onFailure(Call<List<Metadata>> call, Throwable t) {}
    };

    private Callback<List<Recommendation>> fetchRecommendationsCallback = new Callback<List<Recommendation>>() {
        @Override
        public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
            ArrayList<Integer> trackIds = new ArrayList<>();

            for (Recommendation recommendation : response.body()) {
                trackIds.add(recommendation.getTrackId());
            }

            Call<List<Metadata>> metadataCall = metadataClient.metadataForUser(trackIds);
            metadataCall.enqueue(fetchMetadataCallback);
        }

        @Override
        public void onFailure(Call<List<Recommendation>> call, Throwable t) {}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);

        trackNames = new ArrayList<>();

        initializeListView();
        initializeRetrofit();
        recommendationClient = retrofit.create(RecommendationClient.class);
        metadataClient = retrofit.create(MetadataClient.class);
        trackClient = retrofit.create(TrackClient.class);
        fetchRecommendations();
    }

    private void initializeListView() {
        ListView tracksListView = (ListView) findViewById(R.id.mixTracksListView);

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, trackNames);
        tracksListView.setAdapter(adapter);
    }

    private void initializeRetrofit() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(MainActivity.API_URL).addConverterFactory(GsonConverterFactory.create());

        retrofit = builder.client(httpClient.build()).build();
    }

    private void fetchRecommendations() {
        Call<List<Recommendation>> call = recommendationClient.recommendationsForUser();
        call.enqueue(fetchRecommendationsCallback);
    }
}
