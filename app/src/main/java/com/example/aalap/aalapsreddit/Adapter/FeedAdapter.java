package com.example.aalap.aalapsreddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    Context context;

    private static final String TAG = "FeedAdapter";

    public FeedAdapter(List<Entry> entries, Context context) {
        this.entries = entries;
        this.context = context;
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FeedViewHolder(LayoutInflater.from(context).inflate(R.layout.card_items, parent, false));
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        Entry entry = entries.get(position);
        holder.updatedAt.setText(entry.getUpdated());
        holder.title.setText(entry.getTitle());
        holder.authorName.setText(entry.getAuthor().getName());

        if (!entry.getImageLink().isEmpty())
            Picasso.with(context)
                    .load(entry.getImageLink())
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.imageView);
        else{
            Picasso.with(context)
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
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra("link", entry.getAuthor().getUri().replace(BASE_URL, ""));
            intent.putExtra("title", entry.getTitle());
            intent.putExtra("image", entry.getImageLink());
            context.startActivity(intent);
        }
    }
}
