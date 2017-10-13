package com.example.aalap.aalapsreddit.Models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
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


                    } else
                        throw new RuntimeException("Login error:" + response.errorBody().string());
                })
                .doOnComplete(() -> Log.d(TAG, "attemptLogin: completed"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    if (context instanceof LoginActivity) {
                        context.startActivity(new Intent(context, FeedsActivity.class));
                        Toasty.success(context, "Login successful").show();
                    } else {
                        eventBus.register(this);
                        eventBus.post(new EventMsg("LOGIN"));
                        eventBus.unregister(this);
                    }

                }, throwable -> {
                    Log.d(TAG, "login: " + throwable.getMessage());
                    Toasty.error(context, throwable.getMessage()).show();
                });
    }

    public void postComment(String parentId, String commentText) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("User-Agent", preference.getUserName());
        hashMap.put("X-Modhash", preference.getMogHash());
        hashMap.put("cookie", "reddit_session=" + preference.getCookie());

//        Log.d(TAG, "postComment: hashMap "+hashMap);
//        Log.d(TAG, "postComment: cookie "+preference.getCookie());
//        Log.d(TAG, "postComment: user "+preference.getUserName());
//        Log.d(TAG, "postComment: modHash"+preference.getMogHash());
//        Log.d(TAG, "postComment: parentId"+parentId);
//        Log.d(TAG, "postComment: comment "+commentText);

        RedditApp.getRetrofit().postComment(hashMap, "comment", parentId, commentText)
                .map(response -> {
                    if (!response.isSuccessful())
                        throw new RuntimeException("Posting error:" + response.errorBody().string());
                    else {
                        String stringResopnse = response.body().string();
                        JSONObject jsonObject = new JSONObject(stringResopnse);
                        Log.d(TAG, "postComment: " + stringResopnse);
                        return jsonObject.getBoolean("success");
                    }
                })
                .doOnError(throwable -> {
                    Log.d(TAG, "postComment:onError ");
                    throw new RuntimeException("Posting error:" + throwable.getMessage());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    Log.d(TAG, "postComment: sub res ");
                    eventBus.register(this);
                    eventBus.post(new EventMsg(result ? "COMMENT_POSTED" : "ERROR_COMMENT"));
                    eventBus.unregister(this);
                }, throwable -> Log.d(TAG, "postComment: sub err"));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMsg msg) {
        Log.d(TAG, "onEvent: have to register");
    }
}
