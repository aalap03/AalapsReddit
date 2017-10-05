package com.example.aalap.aalapsreddit.Activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aalap.aalapsreddit.Adapter.FeedAdapter;
import com.example.aalap.aalapsreddit.Models.Comments;
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
import io.reactivex.disposables.Disposable;
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
    List<Comments> comments = new ArrayList<>();
    Disposable subscribe;
    ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        store = new RedditStore(this);
        parent = findViewById(R.id.comment_screen_root);
        recyclerView = findViewById(R.id.comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        intent = getIntent();
        view = findViewById(R.id.current_item);

        TextView author = view.findViewById(R.id.author_name);
        TextView updated = view.findViewById(R.id.updated);
        TextView title = view.findViewById(R.id.title);
        ImageView imageView = view.findViewById(R.id.feed_image);
        Button reply = view.findViewById(R.id.reply_button);
        reply.setVisibility(View.VISIBLE);
        reply.setOnClickListener(v -> openCOmmentDialog());

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

        subscribe = RedditApp.getRetrofit().getCommentFeeds(link)
                .map(response -> {
                    if (response.isSuccessful()) {
                        return store.getEntryList(response);
                    } else {
                        Log.d(TAG, "onCreate: res err " + response.errorBody().string());
                        throw new RuntimeException("Error while fetching comments, code: " + response.code());
                    }
                })
                .map(entries -> {
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
                    }
                    return comments;
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

    private void openCOmmentDialog() {

        
        View dialogView = LayoutInflater.from(this).inflate(R.layout.post_comment_dialog, parent, false);
        EditText comment = dialogView.findViewById(R.id.comment);
        Button cancel = dialogView.findViewById(R.id.button_cancel_comment);
        Button post = dialogView.findViewById(R.id.button_post_comment);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        dialog.show();

        String commentValue = comment.getText().toString().trim();
        cancel.setOnClickListener(v -> dialog.dismiss());
        post.setOnClickListener(v -> postComment(commentValue));
    }

    private void postComment(String commentValue) {
        if (commentValue != null && !commentValue.isEmpty()){

        }

        else
            Toast.makeText(CommentsActivity.this, "No comment to post", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscribe != null && !subscribe.isDisposed())
            subscribe.dispose();
    }
}
