package com.example.aalap.aalapsreddit.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aalap.aalapsreddit.Adapter.FeedAdapter;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Utils.RedditApp;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FeedsActivity extends AppCompatActivity {

    private static final String TAG = "FeedsActivity:";
    RecyclerView recyclerView;
    EditText feedName;

    Button load;
    List<Entry> entries = new ArrayList<>();
    public static final String BASE_URL = "https://www.reddit.com/";
    //  /api/login/userName?user=userName&passwd=Password&api_type=json

    RedditStore store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        store = new RedditStore(this);
        recyclerView = findViewById(R.id.recyclerview);
        feedName = findViewById(R.id.feed_name);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        load = findViewById(R.id.load_button);
        load.setOnClickListener(v -> loadFeed());
    }

    private void loadFeed() {

        Log.d(TAG, "loadFeed: ");
        entries = new ArrayList<>();

        RedditApp.getRetrofit().getFeedsO(feedName.getText().toString().trim())
                .map( feedResponse -> {
                    if (feedResponse.isSuccessful()) {
                        Log.d(TAG, "loadFeed: "+feedResponse.body().toString());
                        return store.getEntryList(feedResponse);
                    } else {
                        throw new RuntimeException("Error while loading feed:" + feedResponse.code());
                    }
                })
                .doOnNext(resultEntries -> entries.addAll(resultEntries))
                .doOnError(throwable -> Log.d(TAG, "loadFeed: onError " + throwable.getMessage()))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    Log.d(TAG, "loadFeed: "+entries.size());
                    FeedAdapter adapter = new FeedAdapter(entries, this, false);
                    recyclerView.setAdapter(adapter);
                })
                .subscribeOn(Schedulers.io())
                .subscribe(entries1 -> {}, throwable -> Toasty.error(this, throwable.getMessage()).show());
    }

}
