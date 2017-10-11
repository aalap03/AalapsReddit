package com.example.aalap.aalapsreddit.Models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.aalap.aalapsreddit.Activities.FeedsActivity;
import com.example.aalap.aalapsreddit.Activities.LoginActivity;
import com.example.aalap.aalapsreddit.Service.RedditService;
import com.example.aalap.aalapsreddit.Utils.EventMsg;
import com.example.aalap.aalapsreddit.Utils.ExtractData;
import com.example.aalap.aalapsreddit.Utils.Preference;
import com.example.aalap.aalapsreddit.Utils.RedditApp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by aalap on 2017-10-03.
 */

public class RedditStore {

    Context context;
    private static final String TAG = "RedditStore";
    Preference preference;
    EventBus eventBus = EventBus.getDefault();

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
        preference.setUserName(user);
        RedditApp.getRetrofit().getUser(header, user, user, password, "json")
                .map(response -> {
                    if (response.isSuccessful()) {
                        JSONObject json = new JSONObject(response.body().string());
                        Log.d(TAG, "login: " + json);
                        JSONObject json1 = json.getJSONObject("json");
                        JSONArray errors = json1.getJSONArray("errors");

                        if (errors.length() > 0) {
                            throw new RuntimeException("Invalid credentials");
                        } else {
                            JSONObject data = json1.getJSONObject("data");
                            String modhash = data.getString("modhash");
                            String cookie = data.getString("cookie");
                            preference.setModHash(modhash);
                            preference.setCookie(cookie);
                            return Observable.empty();
                        }


                    } else {
                        Log.d(TAG, "attemptLogin: error " + response.errorBody().string());
                        throw new RuntimeException("Login error:" + response.errorBody().string());
                    }
                })
                .doOnComplete(() -> Log.d(TAG, "attemptLogin: completed"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    if (context instanceof LoginActivity) {
                        context.startActivity(new Intent(context, FeedsActivity.class));
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        eventBus.register(this);
                        eventBus.post(new EventMsg("LOGIN"));
                        eventBus.unregister(this);
                    }

                }, throwable -> {
                    Log.d(TAG, "login: " + throwable.getMessage());
                    Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void postComment(String parentId, String commentText) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", preference.getUserName());
        hashMap.put("X-Modhash", preference.getMogHash());
        hashMap.put("cookie", "reddit_session=" + preference.getCookie());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/api/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        RedditService redditService = retrofit.create(RedditService.class);


        RedditApp.getRetrofit().postComment(hashMap, "comment", parentId, commentText)
                .map(response -> {
                    if (!response.isSuccessful())
                        throw new RuntimeException("Posting error:" + response.errorBody().string());
                    else{
                        return Observable.empty();
                    }
                })
                .doOnError(throwable -> {
                    Log.d(TAG, "postComment:onError ");
                    throw new RuntimeException("Posting error:"+throwable.getMessage());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, "postComment: sub res ");
                    eventBus.register(this);
                    eventBus.post("COMMENT_POSTED");
                    eventBus.unregister(this);
                }, throwable -> Log.d(TAG, "postComment: sub err"));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMsg msg) {
        Log.d(TAG, "onEvent: have to register");
    }
}
