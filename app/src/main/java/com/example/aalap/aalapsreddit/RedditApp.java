package com.example.aalap.aalapsreddit;

import android.app.Application;

import com.example.aalap.aalapsreddit.Service.RedditService;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static com.example.aalap.aalapsreddit.Activities.FeedsActivity.BASE_URL;

/**
 * Created by Aalap on 2017-10-02.
 */

public class RedditApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static RedditService getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        RedditService redditService = retrofit.create(RedditService.class);

        return redditService;
    }
}
