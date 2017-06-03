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

import com.whippetmusic.whippetplayer.service.TrackService;
import com.whippetmusic.whippetplayer.model.Track;
import com.whippetmusic.whippetplayer.network.RetrofitFactory;
import com.whippetmusic.whippetplayer.utils.TrackAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by maciej on 22.05.17.
 */

public class ExploreTab extends Fragment {
    private TrackService trackService;
    private ArrayList<Track> tracks;
    private TrackAdapter adapter;
    private View rootView;

    private Handler responseHandler = new Handler() {
        public void handleMessage(Message message) {
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

        tracks = new ArrayList<>();
        trackService = RetrofitFactory.create(getActivity()).create(TrackService.class);

        initializeListView();
        fetchTracks();
    }

    private void initializeListView() {
        ListView tracksListView = (ListView) rootView.findViewById(R.id.exploreTracksListView);

        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = tracks.get(position);
                Intent trackIntent = new Intent(getContext(), TrackActivity.class);
                trackIntent.putExtra("track", track);
                getContext().startActivity(trackIntent);
            }
        });

        adapter = new TrackAdapter(this.getContext(), tracks);
        tracksListView.setAdapter(adapter);
    }

    private void fetchTracks() {
        Call<List<Track>> call = trackService.tracksForUser();

        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                tracks.addAll(response.body());

                Message message = responseHandler.obtainMessage();
                responseHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {

            }
        });
    }


}
