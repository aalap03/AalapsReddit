package com.example.aalap.aalapsreddit.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.aalap.aalapsreddit.Adapter.FeedAdapter;
import com.example.aalap.aalapsreddit.Adapter.SubredditGridAdapter;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Utils.RedditApp;
import java.util.ArrayList;
import java.util.List;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;

public class FeedsActivity extends AppCompatActivity {

    private static final String TAG = "FeedsActivity:";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<Entry> entries = new ArrayList<>();
    public static final String BASE_URL = "https://www.reddit.com/";
    RedditStore store;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        store = new RedditStore(this);
        progressBar = findViewById(R.id.progress);
        view = findViewById(R.id.subreddit_title);

        TextView feedTitle = view.findViewById(R.id.subReddit);
        feedTitle.setText(getIntent().getStringExtra("feed"));
        feedTitle.setBackground(SubredditGridAdapter.shape(feedTitle, getIntent().getIntExtra("color",0)));

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFeed(getIntent().getStringExtra("feed"));
    }

    private void loadFeed(String feedName) {
        entries = new ArrayList<>();
        RedditApp.getRetrofit().getFeedsO(feedName)
                .map(feedResponse -> {
                    if (feedResponse.isSuccessful()) {
                        Log.d(TAG, "loadFeed: " + feedResponse.body().toString());
                        return store.getEntryList(feedResponse);
                    } else {
                        throw new RuntimeException("Error while loading feed:" + feedResponse.code());
                    }
                })
                .doOnNext(resultEntries -> entries.addAll(resultEntries))
                .doOnError(throwable -> Log.d(TAG, "loadFeed: onError " + throwable.getMessage()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(entries1 -> {
                    Log.d(TAG, "loadFeed: " + entries.size());
                    FeedAdapter adapter = new FeedAdapter(entries, this, false);
                    recyclerView.setAdapter(adapter);
                    progressBar.setVisibility(GONE);
                }, throwable -> Toasty.error(this, throwable.getMessage()).show());
    }

}
