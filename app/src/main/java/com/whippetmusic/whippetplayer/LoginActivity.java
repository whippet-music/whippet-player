package com.whippetmusic.whippetplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.whippetmusic.whippetplayer.client.UserClient;
import com.whippetmusic.whippetplayer.model.User;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private UserClient userClient;

    // UI references.
    private EditText usernameEditText;
    private EditText passwordEditText;

    private Callback<User> loginCallback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            Intent exploreIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(exploreIntent);
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {
            usernameEditText.setError("Wrong username");
            passwordEditText.setError("Wrong password");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUserClient();

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }

    private void initializeUserClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(MainActivity.API_URL).addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(httpClient.build()).build();

        userClient = retrofit.create(UserClient.class);
    }

    private void attemptLogin() {
        // Reset errors.
        usernameEditText.setError(null);
        passwordEditText.setError(null);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Call<User> loginCall = userClient.login(username, password);
        loginCall.enqueue(loginCallback);
    }
}
