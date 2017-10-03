package com.example.aalap.aalapsreddit.Models;

import android.content.Context;
import android.util.Log;

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
            Log.d(TAG, "content: " + entry1.getContent());
            String[] split = entry1.getContent().split("<img src=\"");
            if (split != null) {
                if (split.length > 1) {
                    String[] split1 = split[1].split("\"");
                    entry1.setImageLink(split1[0]);
                } else
                    entry1.setImageLink("");
            } else {
                entry1.setImageLink("");
            }
        }

        return entry;
    }

}
