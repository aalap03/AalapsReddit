package com.example.aalap.aalapsreddit.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aalap.aalapsreddit.Activities.CommentsActivity;
import com.example.aalap.aalapsreddit.Models.Comments;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.R;
import com.example.aalap.aalapsreddit.Utils.DialogUtils;
import com.example.aalap.aalapsreddit.Utils.RedditApp;
import com.squareup.picasso.Picasso;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Aalap on 2017-10-01.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    List<?> items;
    Activity activity;
    boolean isComments;
    ViewGroup dialogContainer;

    private static final String TAG = "FeedAdapter";
    public static final String TITLE = "title";
    public static final String AUTHOR = "auhtor";
    public static final String UPDATED = "updated";
    public static final String IMAGE = "image";
    public static final String ID = "id";
    public static final String COMMENT_LINK = "link";

    public FeedAdapter(List<?> items, Activity activity, boolean isComments) {
        this.items = items;
        this.activity = activity;
        this.isComments = isComments;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        dialogContainer = parent;
        return new FeedViewHolder(LayoutInflater.from(activity).inflate(R.layout.card_items, parent, false));
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {

        holder.imageView.setVisibility(isComments ? GONE : VISIBLE);

        if(!isComments){
            Entry entry = (Entry) items.get(position);
            DateTime dateTime = new DateTime(entry.getUpdated(), DateTimeZone.getDefault());
            Log.d(TAG, "onBindViewHolder: "+dateTime);
            holder.updatedAt.setText(entry.getUpdated());
            holder.title.setText(entry.getTitle());
            if (entry.getAuthor() != null)
                holder.authorName.setText(entry.getAuthor().getName().replace("/u/", ""));
            else
                holder.authorName.setText("Aalap Patel");

            if (!entry.getImageLink().isEmpty())
                Picasso.with(activity)
                        .load(entry.getImageLink())
                        .error(R.drawable.reddit_default_background)
                        .placeholder(R.mipmap.ic_launcher_round)
                        .into(holder.imageView);
            else
                Picasso.with(activity)
                        .load(R.drawable.reddit_default_background)
                        .into(holder.imageView);

            holder.itemView.setOnClickListener(v -> holder.bindClick(entry));
        }
        else{
            Comments comment = (Comments) items.get(position);
            holder.authorName.setText(comment.getAuthor());
            holder.title.setText(comment.getComment());
            holder.updatedAt.setText(comment.getUpdatedAt());
            holder.itemView.setOnClickListener(v -> holder.commentClick(comment));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, authorName, updatedAt;
        Button reply;

        public FeedViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            authorName = itemView.findViewById(R.id.author_name);
            updatedAt = itemView.findViewById(R.id.updated);
            imageView = itemView.findViewById(R.id.feed_image);
            reply = itemView.findViewById(R.id.reply_button);
        }

        public void bindClick(Entry entry) {
            Intent intent = new Intent(activity, CommentsActivity.class);
            android.support.v4.util.Pair<View, String> pair1 = android.support.v4.util.Pair.create(title, "title");
            android.support.v4.util.Pair<View, String> pair2 = android.support.v4.util.Pair.create(authorName, "author");
            android.support.v4.util.Pair<View, String> pair3 = android.support.v4.util.Pair.create(updatedAt, "updated");
            android.support.v4.util.Pair<View, String> pair4 = android.support.v4.util.Pair.create(imageView, "image");
            intent.putExtra(TITLE, entry.getTitle());
            intent.putExtra(AUTHOR, entry.getAuthor().getName());
            intent.putExtra(IMAGE, entry.getImageLink());
            intent.putExtra(UPDATED, entry.getUpdated());
            intent.putExtra(ID, entry.getId());
            intent.putExtra(COMMENT_LINK, entry.getCommentLink());

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pair1, pair2, pair3, pair4);

            if (optionsCompat != null) {
                activity.startActivity(intent, optionsCompat.toBundle());
            } else
                activity.startActivity(intent);
        }

        public void commentClick(Comments comment){

            AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(activity);
            DialogUtils dialogUtils = new DialogUtils(activity);

            if (!RedditApp.getPreference(activity.getApplicationContext()).getMogHash().isEmpty())
                dialogUtils.openCommentDialog(activity, dialogContainer, comment.getId(), dialoBuilder);
            else
                dialogUtils.openLoginDialog(dialoBuilder, dialogContainer);
        }

    }
}
