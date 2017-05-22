package com.whippetmusic.whippetplayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.whippetmusic.whippetplayer.client.TrackClient;
import com.whippetmusic.whippetplayer.model.Track;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maciej on 22.05.17.
 */

public class ExploreTab extends Fragment {
    private TrackClient trackClient;
    private ArrayList<String> trackNames;
    private ArrayAdapter<String> adapter;
    private View rootView;

    private Handler responseHandler = new Handler() {
        public void handleMessage(Message message) {
            trackNames.addAll(message.getData().getStringArrayList("tracks"));
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_explore, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        trackNames = new ArrayList<>();

        initializeListView();
        initializeTrackClient();
        fetchTracks();
    }

    private void initializeListView() {
        ListView tracksListView = (ListView) rootView.findViewById(R.id.exploreTracksListView);

        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, trackNames);
        tracksListView.setAdapter(adapter);
    }

    private void initializeTrackClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Constants.API_URL).addConverterFactory(GsonConverterFactory.create());

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
