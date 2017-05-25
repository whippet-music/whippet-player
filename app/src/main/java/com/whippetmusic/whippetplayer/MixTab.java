package com.whippetmusic.whippetplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.whippetmusic.whippetplayer.service.MetaDataService;
import com.whippetmusic.whippetplayer.service.RecommendationService;
import com.whippetmusic.whippetplayer.service.TrackService;
import com.whippetmusic.whippetplayer.model.MetaData;
import com.whippetmusic.whippetplayer.model.Recommendation;
import com.whippetmusic.whippetplayer.model.Track;
import com.whippetmusic.whippetplayer.network.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by maciej on 22.05.17.
 */

public class MixTab extends Fragment {
    private RecommendationService recommendationService;
    private MetaDataService metaDataService;
    private TrackService trackService;
    private ArrayList<Track> tracks;
    private ArrayAdapter<Track> adapter;
    private View rootView;

    private Handler responseHandler = new Handler() {
        public void handleMessage(Message message) {
            adapter.notifyDataSetChanged();
        }
    };

    private Callback<List<Track>> fetchTracksCallback = new Callback<List<Track>>() {
        @Override
        public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
            tracks.addAll(response.body());

            Message message = responseHandler.obtainMessage();
            responseHandler.sendMessage(message);
        }

        @Override
        public void onFailure(Call<List<Track>> call, Throwable t) {}
    };

    private Callback<List<MetaData>> fetchMetadataCallback = new Callback<List<MetaData>>() {
        @Override
        public void onResponse(Call<List<MetaData>> call, Response<List<MetaData>> response) {
            ArrayList<Integer> trackIds = new ArrayList<>();

            for (MetaData metaData : response.body()) {
                trackIds.add(metaData.getTrackId());
            }

            Call<List<Track>> trackCall = trackService.tracksForUser(trackIds);
            trackCall.enqueue(fetchTracksCallback);
        }

        @Override
        public void onFailure(Call<List<MetaData>> call, Throwable t) {}
    };

    private Callback<List<Recommendation>> fetchRecommendationsCallback = new Callback<List<Recommendation>>() {
        @Override
        public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
            ArrayList<Integer> trackIds = new ArrayList<>();

            for (Recommendation recommendation : response.body()) {
                trackIds.add(recommendation.getTrackId());
            }

            Call<List<MetaData>> metadataCall = metaDataService.metadataForUser(trackIds);
            metadataCall.enqueue(fetchMetadataCallback);
        }

        @Override
        public void onFailure(Call<List<Recommendation>> call, Throwable t) {}
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab_mix, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tracks = new ArrayList<>();
        initializeListView();

        Retrofit retrofit = RetrofitFactory.create(getActivity());
        recommendationService = retrofit.create(RecommendationService.class);
        metaDataService = retrofit.create(MetaDataService.class);
        trackService = retrofit.create(TrackService.class);
        fetchRecommendations();
    }

    private void initializeListView() {
        ListView tracksListView = (ListView) rootView.findViewById(R.id.mixTracksListView);

        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = tracks.get(position);
                Intent trackIntent = new Intent(getContext(), TrackActivity.class);
                trackIntent.putExtra("track", track);
                getContext().startActivity(trackIntent);
            }
        });

        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, tracks);
        tracksListView.setAdapter(adapter);
    }

    private void fetchRecommendations() {
        Call<List<Recommendation>> call = recommendationService.recommendationsForUser();
        call.enqueue(fetchRecommendationsCallback);
    }
}
