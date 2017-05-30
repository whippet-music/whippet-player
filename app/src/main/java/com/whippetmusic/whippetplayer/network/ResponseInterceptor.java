package com.whippetmusic.whippetplayer.network;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import com.whippetmusic.whippetplayer.Constants;
import com.whippetmusic.whippetplayer.LoginActivity;
import com.whippetmusic.whippetplayer.MainActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by maciej on 30.05.17.
 */

public class ResponseInterceptor implements Interceptor {
    private SharedPreferences settings;
    private Activity currentActivity;

    public ResponseInterceptor(Activity currentActivity) {
        this.currentActivity = currentActivity;

        settings = currentActivity.getSharedPreferences(Constants.SHARED_PREFERENCES, 0);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (response.code() == 401) {
            String token = settings.getString(Constants.TOKEN_KEY, null);

            if (token != null) {
                SharedPreferences.Editor editor = settings.edit();
                editor.remove(Constants.TOKEN_KEY);
                editor.commit();

                Intent loginIntent = new Intent(currentActivity, LoginActivity.class);
                currentActivity.startActivity(loginIntent);
                currentActivity.finish();
            }
        }

        return response;
    }
}
