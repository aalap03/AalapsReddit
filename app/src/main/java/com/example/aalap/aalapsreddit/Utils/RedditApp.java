package com.example.aalap.aalapsreddit.Utils;

import android.app.Application;
import android.content.Context;

import com.example.aalap.aalapsreddit.Service.RedditService;

import net.danlew.android.joda.JodaTimeAndroid;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import static com.example.aalap.aalapsreddit.Activities.FeedsActivity.BASE_URL;

/**
 * Created by Aalap on 2017-10-02.
 */

public class RedditApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(getApplicationContext());
    }

    public static RedditService getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        RedditService redditService = retrofit.create(RedditService.class);

        return redditService;
    }

    public static Preference getPreference(Context context){
        return new Preference(context);
    }
}
