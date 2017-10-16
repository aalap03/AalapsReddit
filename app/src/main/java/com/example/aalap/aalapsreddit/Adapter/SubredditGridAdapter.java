package com.example.aalap.aalapsreddit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.aalap.aalapsreddit.Activities.FeedsActivity;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Aalap on 2017-10-15.
 */

public class SubredditGridAdapter extends RecyclerView.Adapter<SubredditGridAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> subReddits;
    RedditStore store;
    private static final String TAG = "SubredditGridAdapter";


    public SubredditGridAdapter(Context context, ArrayList<String> subReddits) {
        this.context = context;
        this.subReddits = subReddits;
        store = new RedditStore(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_reddit_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.subReddit.setText(subReddits.get(position));
        holder.subReddit.setBackground(shape(holder.subReddit));
        holder.subReddit.setOnClickListener(v -> holder.bindClick(subReddits.get(position)));
    }

    @Override
    public int getItemCount() {
        return subReddits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subReddit;

        public MyViewHolder(View itemView) {
            super(itemView);
            subReddit = itemView.findViewById(R.id.subReddit);
        }

        void bindClick(String subReddit) {
            Intent intent = new Intent(context, FeedsActivity.class);
            intent.putExtra("feed", subReddit);
            context.startActivity(intent);
        }
    }

    Drawable shape(TextView textView) {
        Drawable background = textView.getBackground();
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        ((GradientDrawable) background).setColor(color);
        return background;
    }
}
