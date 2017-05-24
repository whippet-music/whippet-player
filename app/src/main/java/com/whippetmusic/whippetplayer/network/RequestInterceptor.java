package com.whippetmusic.whippetplayer.network;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.whippetmusic.whippetplayer.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maciej on 24.05.17.
 */

public class RequestInterceptor implements Interceptor {
    private SharedPreferences settings;

    public RequestInterceptor(Activity currentActivity) {
        settings = currentActivity.getPreferences(0);
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request().newBuilder().addHeader("Authorization", authHeader()).build();
        return chain.proceed(request);
    }

    private String authHeader() {
        String accessToken = settings.getString(Constants.TOKEN_KEY, null);
        return "jwt " + accessToken;
    }
}
