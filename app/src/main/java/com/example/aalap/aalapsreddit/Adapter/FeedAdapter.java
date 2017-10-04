package com.example.aalap.aalapsreddit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aalap.aalapsreddit.Activities.CommentsActivity;
import com.example.aalap.aalapsreddit.Models.Entry;
import com.example.aalap.aalapsreddit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.aalap.aalapsreddit.Activities.FeedsActivity.BASE_URL;

/**
 * Created by Aalap on 2017-10-01.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    List<Entry> entries;
    Activity activity;

    private static final String TAG = "FeedAdapter";
    public static final String TITLE="title";
    public static final String AUTHOR="auhtor";
    public static final String UPDATED="updated";
    public static final String IMAGE="image";
    public static final String COMMENT_LINK="link";

    public FeedAdapter(List<Entry> entries, Activity activity) {
        this.entries = entries;
        this.activity = activity;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedViewHolder(LayoutInflater.from(activity).inflate(R.layout.card_items, parent, false));
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.updatedAt.setText(entry.getUpdated());
        holder.title.setText(entry.getTitle());
        holder.authorName.setText(entry.getAuthor().getName().replace("/u/",""));

        if (!entry.getImageLink().isEmpty())
            Picasso.with(activity)
                    .load(entry.getImageLink())
                    .error(R.drawable.reddit_default)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.imageView);
        else{
            Picasso.with(activity)
                    .load(R.drawable.reddit_default)
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(v->holder.bindClick(entry));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title, authorName, updatedAt;

        public FeedViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            authorName = itemView.findViewById(R.id.author_name);
            updatedAt = itemView.findViewById(R.id.updated);
            imageView = itemView.findViewById(R.id.feed_image);
        }

        public void bindClick(Entry entry){
            Intent intent = new Intent(activity, CommentsActivity.class);
            android.support.v4.util.Pair<View, String> pair1 = android.support.v4.util.Pair.create(title, "title");
            android.support.v4.util.Pair<View, String> pair2 = android.support.v4.util.Pair.create(authorName, "author");
            android.support.v4.util.Pair<View, String> pair3 = android.support.v4.util.Pair.create(updatedAt, "updated");
            android.support.v4.util.Pair<View, String> pair4 = android.support.v4.util.Pair.create(imageView, "image");
            intent.putExtra(TITLE, entry.getTitle());
            intent.putExtra(AUTHOR, entry.getAuthor().getName());
            intent.putExtra(IMAGE, entry.getImageLink());
            intent.putExtra(UPDATED, entry.getUpdated());
            intent.putExtra(COMMENT_LINK, entry.getCommentLink());

            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pair1, pair2, pair3, pair4);

            if(optionsCompat!=null){
                activity.startActivity(intent, optionsCompat.toBundle());
            }else
                activity.startActivity(intent);
        }
    }
}
