package com.example.aalap.aalapsreddit.Models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.aalap.aalapsreddit.Activities.FeedsActivity;
import com.example.aalap.aalapsreddit.Activities.LoginActivity;
import com.example.aalap.aalapsreddit.Utils.EventMsg;
import com.example.aalap.aalapsreddit.Utils.ExtractData;
import com.example.aalap.aalapsreddit.Utils.Preference;
import com.example.aalap.aalapsreddit.Utils.RedditApp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
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
    Preference preference;

    public RedditStore(Context context) {
        this.context = context;
        preference = new Preference(context.getApplicationContext());
    }

    public List<Entry> getEntryList(Response<Feed> response) {
        List<Entry> entry = response.body().getEntry();
        for (Entry entry1 : entry) {
            ExtractData extractCommentLink = new ExtractData(entry1.getContent(), "<a href=");
            ExtractData extractJpg = new ExtractData(entry1.getContent(), "<img src=");
            entry1.setImageLink(extractJpg.extract());
            entry1.setCommentLink(extractCommentLink.extract());
        }
        return entry;
    }

    public void login(Map<String, String> header, String user, String password) {
        RedditApp.getRetrofit().getUser(header, user, user, password, "json")
                .map(response -> {
                    if (response.isSuccessful()) {
                        JSONObject json = new JSONObject(response.body().string());
                        Log.d(TAG, "login: " + json);
                        JSONObject json1 = json.getJSONObject("json");
                        JSONArray errors = json1.getJSONArray("errors");
                        JSONObject data = null;
                        try {
                            data = json1.getJSONObject("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            throw new RuntimeException("Invalid credentials");
                        }
                        String modhash = data.getString("modhash");
                        String cookie = data.getString("cookie");
                        preference.setModHash(modhash);
                        preference.setCookie(cookie);

                    } else {
                        Log.d(TAG, "attemptLogin: error " + response.errorBody().string());
                        throw new RuntimeException("Login error:"+response.errorBody().string());
                    }
                    return Observable.empty();
                })
                .doOnError(throwable -> {
                    Log.d(TAG, "attemptLogin: onError " + throwable.getMessage());
                    throw new RuntimeException(throwable.getMessage());
                })
                .doOnComplete(() -> Log.d(TAG, "attemptLogin: completed"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    if (context instanceof LoginActivity){
                        context.startActivity(new Intent(context, FeedsActivity.class));
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context, "Not loginActivity instnace", Toast.LENGTH_SHORT).show();
                        EventBus eventBus = EventBus.getDefault();
                        eventBus.register(this);
                        eventBus.post(new EventMsg("LOGIN"));
                        eventBus.unregister(this);
                    }

                }, throwable -> Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMsg msg){
        Log.d(TAG, "onEvent: have to register");
    }
}
