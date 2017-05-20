package com.whippetmusic.whippetplayer;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.whippetmusic.whippetplayer.client.RecommendationClient;
import com.whippetmusic.whippetplayer.model.Recommendation;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL = "http://10.0.2.2:5000";

    private View.OnClickListener getRecommendationsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent mixIntent = new Intent(MainActivity.this, MixActivity.class);
            MainActivity.this.startActivity(mixIntent);
        }
    };

    private View.OnClickListener exploreButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent exploreIntent = new Intent(MainActivity.this, ExploreActivity.class);
            MainActivity.this.startActivity(exploreIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button getRecommendationsButton = (Button) findViewById(R.id.getRecommendationsButton);
        Button exploreButton = (Button) findViewById(R.id.exploreButton);

        getRecommendationsButton.setOnClickListener(getRecommendationsButtonListener);
        exploreButton.setOnClickListener(exploreButtonListener);
    }
}
