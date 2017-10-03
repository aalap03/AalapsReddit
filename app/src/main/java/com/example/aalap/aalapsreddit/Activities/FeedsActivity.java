package com.example.aalap.aalapsreddit.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.aalap.aalapsreddit.Adapter.FeedAdapter;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.Models.Feed;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.RedditApp;
import com.example.aalap.aalapsreddit.Service.RedditService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class FeedsActivity extends AppCompatActivity {

    private static final String TAG = "FeedsActivity:";
    RecyclerView recyclerView;
    EditText feedName;

    Button load;
    List<Entry> entries = new ArrayList<>();
    public static final String BASE_URL = "https://www.reddit.com/";//"funny/.rss";

    RedditStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_screen);
        store = new RedditStore(this);
        recyclerView = findViewById(R.id.recyclerview);
        feedName = findViewById(R.id.feed_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        load = findViewById(R.id.load_button);
        load.setOnClickListener(v -> loadFeed());
    }

    private void loadFeed() {

        entries = new ArrayList<>();

        RedditApp.getRetrofit().getFeedsO(feedName.getText().toString())
                .map( feedResponse -> {
                    if (feedResponse.isSuccessful()) {
                        return store.getEntryList(feedResponse);
                    } else {
                        throw new RuntimeException("Error while loading feed:" + feedResponse.code());
                    }
                })
                .doOnNext(resultEntries -> entries.addAll(resultEntries))
                .doOnError(throwable -> Log.d(TAG, "loadFeed: onError " + throwable.getMessage()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    FeedAdapter adapter = new FeedAdapter(entries, this);
                    recyclerView.setAdapter(adapter);
                })
                .subscribeOn(Schedulers.io())
                .subscribe();

    }


}
