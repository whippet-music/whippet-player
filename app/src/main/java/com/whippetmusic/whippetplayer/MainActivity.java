package com.whippetmusic.whippetplayer;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String API_URL = "http://10.0.2.2:5000";
    private OkHttpClient okHttpClient;
    private Request request;

    private Button pingButton;
    private EditText responseEditText;

    private Handler responseHandler = new Handler() {
        public void handleMessage(Message message) {
            String body = message.getData().getString("body");
            responseEditText.setText(body);
        }
    };

    private View.OnClickListener pingButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            okHttpClient = new OkHttpClient();
            request = new Request.Builder().url(API_URL + "/ping").build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("ping", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Message message = responseHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("body", response.body().string());
                    message.setData(bundle);
                    responseHandler.sendMessage(message);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pingButton = (Button) findViewById(R.id.pingButton);
        responseEditText = (EditText) findViewById(R.id.responseEditText);

        pingButton.setOnClickListener(pingButtonListener);
    }
}
