package com.example.aalap.aalapsreddit.Models;

import android.content.Context;
import android.util.Log;

import com.example.aalap.aalapsreddit.Utils.ExtractData;

import java.util.List;

import retrofit2.Response;

/**
 * Created by aalap on 2017-10-03.
 */

public class RedditStore {

    Context context;
    private static final String TAG = "RedditStore";

    public RedditStore(Context context) {
        this.context = context;
    }

    public List<Entry> getEntryList(Response<Feed> response){
        List<Entry> entry = response.body().getEntry();
        for (Entry entry1 : entry) {
            ExtractData extractCommentLink = new ExtractData(entry1.getContent(), "<a href=");
            ExtractData extractJpg = new ExtractData(entry1.getContent(), "<img src=");
            entry1.setImageLink(extractJpg.extract());
            entry1.setCommentLink(extractCommentLink.extract());
        }
        return entry;
    }
}
