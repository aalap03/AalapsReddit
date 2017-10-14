package com.example.aalap.aalapsreddit.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.Models.Feed;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Utils.RedditApp;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SubRedditsActivity extends AppCompatActivity {

    private static final String TAG = "SubRedditsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_reddits);

        RedditApp.getRetrofit().getSubReddits()
                .flatMap(result -> {
                    if (result.isSuccessful()) {
                        Log.d(TAG, "onCreate: " + result.body().toString());

                        return Observable.empty();
                    } else {
                        String error = result.errorBody().string();
                        Log.d(TAG, "onCreate: "+error);
                        throw new RuntimeException(error);
                    }
                })
                .doOnError(throwable -> Log.d(TAG, "onCreate: onError"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                }, throwable -> Log.d(TAG, "onCreate: sub " + throwable.getMessage()));
    }
}
