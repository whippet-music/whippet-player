package com.whippetmusic.whippetplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.whippetmusic.whippetplayer.service.UserService;
import com.whippetmusic.whippetplayer.model.User;
import com.whippetmusic.whippetplayer.network.RetrofitFactory;
import com.whippetmusic.whippetplayer.request.UserAuthRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private UserService userService;
    private SharedPreferences settings;

    // UI references.
    private EditText usernameEditText;
    private EditText passwordEditText;

    private Callback<User> loginCallback = new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.TOKEN_KEY, response.body().getAccessToken());
            editor.commit();
            Intent exploreIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(exploreIntent);
            LoginActivity.this.finish();
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

        settings = getPreferences(0);
        if (settings.getString(Constants.TOKEN_KEY, null) != null) {
            Intent exploreIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(exploreIntent);
            this.finish();
        }
        else {
            userService = RetrofitFactory.create(this).create(UserService.class);

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
    }

    private void attemptLogin() {
        // Reset errors.
        usernameEditText.setError(null);
        passwordEditText.setError(null);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Call<User> loginCall = userService.login(new UserAuthRequest(username, password));
        loginCall.enqueue(loginCallback);
    }
}
