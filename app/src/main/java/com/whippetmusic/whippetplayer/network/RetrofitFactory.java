package com.whippetmusic.whippetplayer.network;

import android.app.Activity;

import com.whippetmusic.whippetplayer.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maciej on 24.05.17.
 */

public class RetrofitFactory {
    private RetrofitFactory() {}

    public static Retrofit create(Activity activity) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new RequestInterceptor(activity))
                .addInterceptor(new ResponseInterceptor(activity));

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create());

        return builder.client(httpClient.build()).build();
    }
}
