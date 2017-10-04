package com.example.aalap.aalapsreddit.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aalap.aalapsreddit.Adapter.FeedAdapter;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.Models.Feed;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Service.RedditService;
import com.example.aalap.aalapsreddit.Utils.RedditApp;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.aalap.aalapsreddit.Activities.FeedsActivity.BASE_URL;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    View view;
    private static final String TAG = "CommentsActivity:";
    Intent intent;
    RedditStore store;
    List<Entry> entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        store = new RedditStore(this);
        recyclerView = findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        intent = getIntent();
        view = findViewById(R.id.current_item);

        TextView author = view.findViewById(R.id.author_name);
        TextView updated = view.findViewById(R.id.updated);
        TextView title = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.feed_image);

        author.setText(intent.getStringExtra(FeedAdapter.AUTHOR));
        updated.setText(intent.getStringExtra(FeedAdapter.UPDATED));
        title.setText(intent.getStringExtra(FeedAdapter.TITLE));

        if (!intent.getStringExtra(FeedAdapter.IMAGE).isEmpty())
            Picasso.with(this)
                    .load(intent.getStringExtra(FeedAdapter.IMAGE))
                    .error(R.drawable.reddit_default)
                    .into(imageView);
        else
            Picasso.with(this)
                    .load(R.drawable.reddit_default)
                    .into(imageView);


        String link = intent.getStringExtra(FeedAdapter.COMMENT_LINK).replace(BASE_URL, "");
        Log.d(TAG, "onCreate: " + link);

        RedditApp.getRetrofit().getCommentFeeds(link)
                .map(feedResponse -> {
                    if(feedResponse.isSuccessful()){
                        return store.getEntryList(feedResponse);
                    }else{
                        Log.d(TAG, "onCreate: res err "+feedResponse.errorBody().string());
                        throw new RuntimeException("Error while getting comments:"+feedResponse.code());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(entries -> {
                    entryList = new ArrayList<>();
                    entryList.addAll(entries);
                })
                .doOnError(throwable -> Log.d(TAG, "onCreate: onError "+throwable.getMessage()))
                .doOnComplete(() -> {
                    FeedAdapter adapter = new FeedAdapter(entryList, this);
                    recyclerView.setAdapter(adapter);
                })
                .subscribeOn(Schedulers.io())
                .subscribe();

    }
}
