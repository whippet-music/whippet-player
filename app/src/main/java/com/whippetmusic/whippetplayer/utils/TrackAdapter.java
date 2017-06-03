package com.whippetmusic.whippetplayer.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import com.whippetmusic.whippetplayer.model.Track;

import java.util.ArrayList;

/**
 * Created by maciej on 03.06.17.
 */

public class TrackAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Track> tracks;

    public TrackAdapter(Context context, ArrayList<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Object getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoLineListItem twoLineListItem;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = (TwoLineListItem) inflater.inflate(
                    android.R.layout.simple_list_item_2, null);
        } else {
            twoLineListItem = (TwoLineListItem) convertView;
        }

        TextView text1 = twoLineListItem.getText1();
        TextView text2 = twoLineListItem.getText2();
        text2.setTextColor(Color.GRAY);

        text1.setText(tracks.get(position).getTitle());
        text2.setText(tracks.get(position).getArtistName());

        return twoLineListItem;
    }
}