package com.example.aalap.aalapsreddit.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aalap.aalapsreddit.Adapter.FeedAdapter;
import com.example.aalap.aalapsreddit.Models.Comments;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Utils.DialogUtils;
import com.example.aalap.aalapsreddit.Utils.EventMsg;
import com.example.aalap.aalapsreddit.Utils.Preference;
import com.example.aalap.aalapsreddit.Utils.RedditApp;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.aalap.aalapsreddit.Activities.FeedsActivity.BASE_URL;
import static com.example.aalap.aalapsreddit.Adapter.FeedAdapter.ID;

public class CommentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    View view;
    private static final String TAG = "CommentsActivity:";
    Intent intent;
    RedditStore store;
    List<Comments> comments = new ArrayList<>();
    Disposable subscribe;
    ViewGroup parent;
    Preference preference;
    EventBus eventBus = EventBus.getDefault();
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        store = new RedditStore(this);
        preference = new Preference(getApplicationContext());

        parent = findViewById(R.id.comment_screen_root);
        recyclerView = findViewById(R.id.comment_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setItemPrefetchEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        intent = getIntent();
        view = findViewById(R.id.current_item);

        TextView author = view.findViewById(R.id.author_name);
        TextView updated = view.findViewById(R.id.updated);
        TextView title = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.feed_image);
        Button reply = view.findViewById(R.id.reply_button);
        reply.setVisibility(View.VISIBLE);
        reply.setOnClickListener(v -> openDialog());

        author.setText(intent.getStringExtra(FeedAdapter.AUTHOR));
        updated.setText(intent.getStringExtra(FeedAdapter.UPDATED));
        title.setText(intent.getStringExtra(FeedAdapter.TITLE));

        if (!intent.getStringExtra(FeedAdapter.IMAGE).isEmpty())
            Picasso.with(this)
                    .load(intent.getStringExtra(FeedAdapter.IMAGE))
                    .error(R.drawable.reddit_default_background)
                    .into(imageView);
        else
            Picasso.with(this)
                    .load(R.drawable.reddit_default_background)
                    .into(imageView);


        String link = intent.getStringExtra(FeedAdapter.COMMENT_LINK).replace(BASE_URL, "");

        subscribe = RedditApp.getRetrofit().getCommentFeeds(link)
                .map(response -> {
                    if (response.isSuccessful()) {
                        return store.getEntryList(response);
                    } else {
                        Log.d(TAG, "onCreate: res err " + response.errorBody().string());
                        throw new RuntimeException("Error while fetching comments, code: " + response.code());
                    }
                })
                .doOnNext(entries -> {
                    comments = new ArrayList<>();
                    for (Entry entry : entries) {
                        Log.d(TAG, "content: " + entry.getContent());
                        String[] firstSplit = entry.getContent().split("<p>");
                        String commentText = "NONE";
                        if (firstSplit != null && firstSplit.length > 1) {
                            commentText = firstSplit[1].split("</p>")[0].replace("&#39", "'");
                        }
                        Comments comment = new Comments(commentText
                                , entry.getUpdated()
                                , entry.getAuthor() == null ? "Author" : entry.getAuthor().getName().replace("/u/", "")
                                , entry.getId());
                        comments.add(comment);
                        Log.d(TAG, "commentId: " + entry.getId());
                    }
                })
                .doOnError(throwable -> {
                    throw new RuntimeException(throwable.getMessage());
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    FeedAdapter adapter = new FeedAdapter(comments, this, true);
                    recyclerView.setAdapter(adapter);
                })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void openDialog() {
        AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(this);
        DialogUtils dialogUtils = new DialogUtils(this);

        if (!preference.getMogHash().isEmpty())
            dialogUtils.openCommentDialog(this, parent, getIntent().getStringExtra(ID), dialoBuilder);
        else
            dialogUtils.openLoginDialog(dialoBuilder, parent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe != null && !subscribe.isDisposed())
            subscribe.dispose();

        if (eventBus.isRegistered(this))
            eventBus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMsg eventMsg) {

        if (eventMsg.getMsg() != null) {
            if (alertDialog != null)
                alertDialog.dismiss();
            if (eventMsg.getMsg().equalsIgnoreCase("LOGIN"))
                Toasty.success(this, "Logged in successfully").show();
            else if (eventMsg.getMsg().equalsIgnoreCase("COMMENT_POSTED"))
                Toasty.success(this, "Comment posted successfully").show();
            else if (eventMsg.getMsg().equalsIgnoreCase("ERROR_COMMENT"))
                Toasty.error(this, "Could not post comment: are you signed in?").show();

        }
    }
}
