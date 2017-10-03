package com.example.aalap.aalapsreddit.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.RedditApp;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    View view;
    private static final String TAG = "CommentsActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        recyclerView = findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        view = findViewById(R.id.current_item);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link") + "/comments";
        Log.d(TAG, "onCreate: "+link);

        RedditApp.getRetrofit().getFeedComments(link).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                    try {//
                        Log.d(TAG, "onResponse: "+response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        Log.d(TAG, "onResponse: error "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });

    }
}
