package com.example.aalap.aalapsreddit.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntegerRes;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aalap.aalapsreddit.Activities.FeedsActivity;
import com.example.aalap.aalapsreddit.Models.RedditStore;
import com.example.aalap.aalapsreddit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Aalap on 2017-10-15.
 */

public class SubredditGridAdapter extends RecyclerView.Adapter<SubredditGridAdapter.MyViewHolder> {

    Activity context;
    ArrayList<String> subReddits;
    RedditStore store;
    private static final String TAG = "SubredditGridAdapter";


    public SubredditGridAdapter(Activity context, ArrayList<String> subReddits) {
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
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.subReddit.setBackground(shape(holder.subReddit, color));
        holder.subReddit.setOnClickListener(v -> holder.bindClick(subReddits.get(position), color));
    }

    @Override
    public int getItemCount() {
        return subReddits.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView subReddit;
        ViewGroup rootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            subReddit = itemView.findViewById(R.id.subReddit);
            rootView = itemView.findViewById(R.id.root_view);
        }

        void bindClick(String subReddit, int color) {

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context, rootView, "sub_reddit");

            Intent intent = new Intent(context, FeedsActivity.class);
            intent.putExtra("feed", subReddit);
            intent.putExtra("color", color);

            if (options != null)
                context.startActivity(intent, options.toBundle());
            else
                context.startActivity(intent);
        }
    }

    public static Drawable shape(TextView textView, int color) {
        Drawable background = textView.getBackground();
        ((GradientDrawable) background).setColor(color);
        return background;
    }
}
