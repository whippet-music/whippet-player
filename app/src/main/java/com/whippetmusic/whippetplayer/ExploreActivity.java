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

import com.whippetmusic.whippetplayer.client.RecommendationClient;
import com.whippetmusic.whippetplayer.client.TrackClient;
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

public class ExploreActivity extends AppCompatActivity {
    private TrackClient trackClient;
    private ArrayList<String> trackNames;
    private ArrayAdapter<String> adapter;

    private Handler responseHandler = new Handler() {
        public void handleMessage(Message message) {
            trackNames.addAll(message.getData().getStringArrayList("tracks"));
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        trackNames = new ArrayList<>();

        initializeListView();
        initializeTrackClient();
        fetchTracks();
    }

    private void initializeListView() {
        ListView tracksListView = (ListView) findViewById(R.id.exploreTracksListView);

        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, trackNames);
        tracksListView.setAdapter(adapter);
    }

    private void initializeTrackClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(MainActivity.API_URL).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();
        trackClient = retrofit.create(TrackClient.class);
    }

    private void fetchTracks() {
        Call<List<Track>> call = trackClient.tracksForUser();

        call.enqueue(new Callback<List<Track>>() {
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
            public void onFailure(Call<List<Track>> call, Throwable t) {

            }
        });
    }

}
