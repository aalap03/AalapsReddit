package com.example.aalap.aalapsreddit.Models;

import android.content.Context;
import android.util.Log;

import com.example.aalap.aalapsreddit.Utils.ExtractData;
import com.example.aalap.aalapsreddit.Utils.RedditApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by aalap on 2017-10-03.
 */

public class RedditStore {

    Context context;
    private static final String TAG = "RedditStore";

    public RedditStore(Context context) {
        this.context = context;
    }

    public List<Entry> getEntryList(Response<Feed> response){
        List<Entry> entry = response.body().getEntry();
        for (Entry entry1 : entry) {
            ExtractData extractCommentLink = new ExtractData(entry1.getContent(), "<a href=");
            ExtractData extractJpg = new ExtractData(entry1.getContent(), "<img src=");
            entry1.setImageLink(extractJpg.extract());
            entry1.setCommentLink(extractCommentLink.extract());
        }
        return entry;
    }

    public void login(Map<String, String> header, String user, String password){
        RedditApp.getRetrofit().getUser(header, user, user, password, "json")
                .map(response -> {
                    if (response.isSuccessful()) {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONObject json1 = json.getJSONObject("json");
                        JSONArray errors = json1.getJSONArray("errors");
                        JSONObject data = json1.getJSONObject("data");

                        if (errors.length() != 0) {
                            String modhash = data.getString("modhash");
                            String cookie = data.getString("cookie");
                        } else{

                        }

                    } else {
                        Log.d(TAG, "attemptLogin: error " + response.errorBody().string());
                    }
                    return Observable.empty();
                })
                .doOnError(throwable -> Log.d(TAG, "attemptLogin: onError " + throwable.getMessage()))
                .doOnComplete(() -> Log.d(TAG, "attemptLogin: completed"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}
