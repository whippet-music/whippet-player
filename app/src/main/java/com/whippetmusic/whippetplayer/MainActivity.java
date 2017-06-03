package com.whippetmusic.whippetplayer;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.whippetmusic.whippetplayer.model.LogisticRegression;
import com.whippetmusic.whippetplayer.model.LogisticRegressionFactory;
import com.whippetmusic.whippetplayer.model.MetaData;
import com.whippetmusic.whippetplayer.model.Vote;
import com.whippetmusic.whippetplayer.network.RetrofitFactory;
import com.whippetmusic.whippetplayer.service.MetaDataService;
import com.whippetmusic.whippetplayer.service.VoteService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private VoteService voteService;
    private MetaDataService metaDataService;
    private ArrayList<Vote> votes;
    private Map<Integer, Integer> voteMap;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Callback<List<MetaData>> metaDataCallback = new Callback<List<MetaData>>() {
        @Override
        public void onResponse(Call<List<MetaData>> call, Response<List<MetaData>> response) {
            LogisticRegression regression = LogisticRegressionFactory.getLogisticRegression(getBaseContext());
            float[][] features = new float[response.body().size()][Constants.WEIGHTS_LENGTH];
            int[] voteLabels = new int[response.body().size()];

            for (int i = 0; i < response.body().size(); i++) {
                features[i] = response.body().get(i).getFeatureVector();
                voteLabels[i] = voteMap.get(response.body().get(i).getTrackId());
            }

            regression.trainMany(features, voteLabels);
            System.out.print("elo");
            regression.logWeights();
        }

        @Override
        public void onFailure(Call<List<MetaData>> call, Throwable t) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        voteService = RetrofitFactory.create(this).create(VoteService.class);
        metaDataService = RetrofitFactory.create(this).create(MetaDataService.class);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        train(); // TODO: train only once
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MixTab();
                case 1:
                    return new ExploreTab();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MIX";
                case 1:
                    return "EXPLORE";
            }
            return null;
        }
    }

    private void train() {
        LogisticRegression regression = LogisticRegressionFactory.getLogisticRegression(this);
        regression.clearWeights();

        final Call<List<Vote>> voteCall = voteService.votesForUser();
        voteCall.enqueue(new Callback<List<Vote>>() {
            @Override
            public void onResponse(Call<List<Vote>> call, Response<List<Vote>> response) {
                int size = response.body().size();
                int currentIndex = 0;
                int step = 100;

                voteMap = new HashMap<Integer, Integer>();
                votes = (ArrayList<Vote>) response.body();

                while (currentIndex < size) {
                    ArrayList<Integer> trackIds = new ArrayList<>();
                    int bound = currentIndex + step;

                    for (int i = currentIndex; i < bound && i < size; i++) {
                        Vote vote = votes.get(i);
                        trackIds.add(vote.getTrackId());
                        voteMap.put(vote.getTrackId(), vote.getVoteFlag());
                    }
                    currentIndex += step;

                    Call<List<MetaData>> metaDataCall = metaDataService.metadataForUser(trackIds);
                    metaDataCall.enqueue(metaDataCallback);
                }

            }

            @Override
            public void onFailure(Call<List<Vote>> call, Throwable t) {
                int i;
            }
        });
    }
}
