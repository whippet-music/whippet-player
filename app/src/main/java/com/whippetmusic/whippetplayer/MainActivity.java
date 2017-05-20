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

    private EditText responseEditText;

    private RecommendationClient recommendationClient;

    private Handler responseHandler = new Handler() {
        public void handleMessage(Message message) {
            String body = message.getData().getString("body");
            responseEditText.setText(body);
        }
    };

    private View.OnClickListener getRecommendationsButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Call<List<Recommendation>> call = recommendationClient.recommendationsForUser();

            call.enqueue(new Callback<List<Recommendation>>() {
                @Override
                public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
                    Message message = responseHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("body", "ok");
                    message.setData(bundle);
                    responseHandler.sendMessage(message);
                }

                @Override
                public void onFailure(Call<List<Recommendation>> call, Throwable t) {
                    Log.e("recommendationRequest", t.getMessage());
                }
            });
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
        initializeRecommendationClient();
        setContentView(R.layout.activity_main);

        Button getRecommendationsButton = (Button) findViewById(R.id.getRecommendationsButton);
        Button exploreButton = (Button) findViewById(R.id.exploreButton);
        responseEditText = (EditText) findViewById(R.id.responseEditText);

        getRecommendationsButton.setOnClickListener(getRecommendationsButtonListener);
        exploreButton.setOnClickListener(exploreButtonListener);
    }

    private void initializeRecommendationClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient.build()).build();

        recommendationClient = retrofit.create(RecommendationClient.class);
    }
}
